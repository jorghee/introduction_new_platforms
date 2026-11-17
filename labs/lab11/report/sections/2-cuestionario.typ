#import "../components/lab-section.typ": lab-section

#lab-section(title: "CUESTIONARIO")[

  === 1. ¿Lograste resolver completamente el Ejercicio/Problema Resuelto por el Docente (reproductor de audio-guía en Foreground Service, con notificación visible al pasar la app a segundo plano)? Adjunta una captura de pantalla del emulador mostrando el resultado como evidencia.

  Sí, el problema resuelto se implementó y validó de forma íntegra en el entorno de pruebas bajo Android 14 (API 34).

  El flujo completo se desarrolló cumpliendo con los estándares de arquitectura de Android:

  1. *Declaración de Permisos y Tipo de Servicio en el Manifiesto:*
     En `AndroidManifest.xml` se especificaron los permisos `FOREGROUND_SERVICE` y el permiso específico requerido a partir de Android 14 `FOREGROUND_SERVICE_MEDIA_PLAYBACK`. Asimismo, se declaró la etiqueta `android:foregroundServiceType="mediaPlayback"` en el servicio `AudioGuiaService`, previniendo excepciones en tiempo de ejecución por políticas de tipado estricto.
  2. *Solicitud Dinámica de Permisos en Tiempo de Ejecución:*
     En `MainActivity.kt` se implementó la solicitud dinámica del permiso `android.permission.POST_NOTIFICATIONS` mediante `rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission())`, asegurando que en dispositivos con Android 13+ el sistema cuente con la autorización explícita del usuario para emitir notificaciones en la cortina del sistema.
  3. *Inicialización del Canal de Notificaciones:*
     Se creó el canal de notificaciones persistente con `NotificationChannel(ID_CANAL, "Reproductor Multimedia", NotificationManager.IMPORTANCE_DEFAULT)`, asociándolo al servicio.
  4. *Promoción a Foreground Service mediante `startForeground()`:*
     Al accionar la reproducción de una pista, el servicio invoca:
     ```kotlin
     val notificacion = construirNotificacion(edificio, estaReproduciendo = true)
     startForeground(ID_NOTIFICACION, notificacion)
     ```
  5. *Detección de Segundo Plano con `ProcessLifecycleOwner`:*
     Se integró `ProcessLifecycleOwner.get().lifecycle.addObserver(...)` con un `LifecycleEventObserver`. Cuando el usuario presiona el botón Home o conmuta a otra aplicación (`Lifecycle.Event.ON_STOP`), la aplicación detecta el paso a segundo plano, actualiza el banner informativo y delega la continuidad ininterrumpida de la narración al `AudioGuiaService`. Al regresar a la aplicación (`Lifecycle.Event.ON_START`), el banner se restablece a primer plano manteniendo sincronizado el progreso del audio en tiempo real.

  #figure(
    image("../img/report/studio_manifest_service.png", width: 95%),
    caption: [Configuración de AndroidManifest.xml en Android Studio: permisos de servicio en primer plano y tipo mediaPlayback.],
  )

  #figure(
    image("../img/report/evidencia_2_notificacion_background.png", width: 42%),
    caption: [Notificación activa del Foreground Service desplegada en la barra de estado al situar la aplicación en segundo plano.],
  )

  === 2. ¿Por qué es obligatorio mostrar una notificación en un Foreground Service, a diferencia de un started Service como el del Laboratorio 10? Explica qué problema busca evitar esta restricción del sistema operativo.

  La obligatoriedad de vincular una notificación persistente e inamovible (_non-dismissible notification_) a un _Foreground Service_ responde a razones fundamentales de arquitectura del kernel de Linux, optimización de recursos energéticos y transparencia hacia el usuario final.

  ==== A. Gestión de Memoria y Low Memory Killer (LMK)

  En el sistema operativo Android, la gestión de memoria RAM está gobernada por el demonio del kernel denominado *Low Memory Killer (LMK)*. El LMK clasifica cada proceso del sistema mediante una puntuación de ajuste de memoria fuera de límite (*`oom_score_adj`*), que varía típicamente entre $-1000$ (procesos nativos críticos del sistema) y $+1000$ (procesos en caché vacíos):

  - *Started Service convencional:* Un servicio iniciado tradicional que corre en segundo plano sin notificación recibe un puntaje `oom_score_adj` elevado (generalmente entre $500$ y $900$). Cuando el dispositivo experimenta presión de memoria RAM, el LMK destruye inmediatamente estos procesos sin previo aviso para liberar memoria.
  - *Foreground Service:* Al ejecutar `startForeground()`, el sistema operativo otorga al proceso un puntaje `oom_score_adj` privilegiado (cercano a $0$ o valores negativos como $-800$), equivalente al nivel de prioridad de una aplicación visible con la que el usuario está interactuando activamente. De este modo, el sistema se compromete a no eliminar el proceso salvo en situaciones de fallo catastrófico de memoria.

  #table(
    columns: (1.4fr, 1.3fr, 1.3fr, 1.2fr),
    align: (left, center, center, center),
    table.header(
      [*Tipo de Servicio*],
      [*Nivel de Prioridad LMK*],
      [*Visibilidad al Usuario*],
      [*Riesgo de Terminación*]
    ),
    [*Started Service*],
    [Bajo (`oom_score_adj` 500-900)],
    [Invisible en barra de estado],
    [Alto (ante presión de RAM)],

    [*Foreground Service*],
    [Alto (`oom_score_adj` 0-50)],
    [Obligatorio (Notificación activa)],
    [Prácticamente nulo]
  )

  ==== B. Problemas que busca evitar esta restricción

  1. *Prevención del Abuso de Batería y CPU (Wakelocks Ocultos):*
     En versiones tempranas de Android, aplicaciones con código deficiente creaban servicios en segundo plano infinitos que mantenían activos los núcleos del procesador (_Wakelocks_) y el módem celular sin que el usuario lo supiera. La notificación persistente asegura que si una aplicación está consumiendo recursos críticos del hardware en segundo plano, el usuario tenga conocimiento visual inmediato y pueda decidir si mantenerla o cerrarla.
  2. *Transparencia, Privacidad y Soberanía del Usuario:*
     Un servicio con privilegios de primer plano tiene acceso continuo al hardware de sonido, micrófono o conectividad. Sin una notificación obligatoria, una aplicación podría realizar operaciones acústicas o de red continuas de forma encubierta. La notificación garantiza el principio de consentimiento informado y control del dispositivo.
  3. *Evolución de Restricciones del Sistema:*
     A partir de Android 8.0 (API 26), Google restringió drásticamente la ejecución indiscriminada de servicios en segundo plano (_Background Execution Limits_). A partir de Android 14 (API 34), es obligatorio declarar el atributo `android:foregroundServiceType` en el manifiesto. Si una aplicación intenta llamar a `startForeground()` sin una notificación válida o sin un tipo declarado compatible con su actividad, el sistema lanza inmediatamente una excepción `SecurityException` o `InvalidForegroundServiceTypeException`, abortando la ejecución.

  #colbreak()
  === 3. En forma individual, comenta en el video de referencia qué diferencia encuentras entre la forma de mostrar la notificación en el video y el control de visibilidad basado en `ProcessLifecycleOwner` implementado en este laboratorio.

  Se realizó la visualización y análisis crítico del video de referencia (#link("https://www.youtube.com/watch?v=-45jGLtbhPQ")[https://www.youtube.com/watch?v=-45jGLtbhPQ]), contrastando la técnica convencional de instanciación estática de notificaciones en Foreground Services frente al control reactivo de visibilidad basado en `ProcessLifecycleOwner`. Cada integrante publicó su análisis técnico de manera individual:

  #v(0.4em)
  ==== Jorge Luis Mamani Huarsaya

  #figure(
    image("../img/report/jorge_comment.png", width: 92%),
    caption: [Evidencia del comentario técnico publicado por Jorge Luis Mamani Huarsaya en el video de referencia.],
  )

  #v(0.6em)
  ==== Yordano Hernan Boza Portilla

  #figure(
    image("../img/report/yordano_comment.jpeg", width: 92%),
    caption: [Evidencia del comentario técnico publicado por Yordano Hernan Boza Portilla en el video de referencia.],
  )
]
