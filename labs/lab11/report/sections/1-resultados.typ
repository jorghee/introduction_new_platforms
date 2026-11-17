#import "../components/lab-section.typ": lab-section

#lab-section(title: "RESULTADOS DE LAS ACTIVIDADES")[

  #v(0.2em)
  #highlight(fill: rgb("fff176"))[*Link del repositorio GitHub*]: #link("https://github.com/jorghee/introduction_new_platforms/tree/main/labs/lab11")[https://github.com/jorghee/introduction_new_platforms/tree/main/labs/lab11]\
  #highlight(fill: rgb("fff176"))[*Link del video de demostración*]: #link("https://drive.google.com/file/d/1BQiStlu0v0iqLExplTjkHP83Erxem55e/view?usp=drive_link")[https://drive.google.com/file/d/1BQiStlu0v0iqLExplTjkHP83Erxem55e/view?usp=drive_link]
  #v(0.6em)

  == Arquitectura Modular y Separación de Responsabilidades

  En el desarrollo de aplicaciones móviles profesionales bajo Android y Jetpack Compose, concentrar la lógica de servicios en segundo plano, persistencia local y presentación en un único archivo (como `MainActivity.kt`) representa un antipatrón que degrada la mantenibilidad, dificulta las pruebas unitarias y propicia fugas de memoria. La solución implementada estructura el proyecto aplicando estrictamente el principio de responsabilidad única (SRP) y una clara separación de capas:

  - *Capa de Presentación (`ui/`)*: Subdividida en `screens/` (pantalla orquestadora `AudioGuiaScreen`), `components/` (componentes modulares reutilizables como `TarjetaEdificioActual`, `ControlesPrincipales`, `BotonContinuarUltimo`, `ListaEdificiosCampus` y `BannerCicloVida`) y `viewmodel/` (`AudioGuiaViewModel`), garantizando una interfaz de usuario puramente declarativa, minimalista, con bordes refinados y reactiva a flujos de estado inmutables.
  - *Capa de Servicio (`service/`)*: `AudioGuiaService` opera de manera autónoma como un _Foreground Service_ de tipo `mediaPlayback` @android_foreground_services. Gestiona el ciclo de vida del reproductor nativo `MediaPlayer` @android_mediaplayer, vincula la sesión multimedia con `MediaSessionCompat`, solicita foco de audio mediante `AudioManager.requestAudioFocus`, interactúa con el `NotificationManager` mediante estilos multimedia (`MediaStyle`) @android_media_notification y publica su estado operativo a través de `StateFlow<AudioPlaybackState>`.
  - *Capa de Datos y Persistencia (`data/`)*: Aloja el modelo de dominio (`Edificio`), el repositorio (`AudioGuiaRepository`) y la capa Room (`AppDatabase`, `UltimoEdificioDao`, `UltimoEdificioEntity`) que interactúa con la base de datos relacional SQLite `audioguia_campus.db` @room_persistence.

  #figure(
    image("../img/report/diagrama_arquitectura.png", width: 95%),
    caption: [Diagrama de arquitectura modular: interacción reactiva entre Jetpack Compose, ProcessLifecycleOwner, Foreground Service y Room Database.],
  )

  #v(0.8em)
  == Ejercicio Guiado y Ejercicio 4: Reproducción Real de Audio con MediaPlayer

  El objetivo central consistió en transformar la simulación básica en un reproductor multimedia real y robusto capaz de ejecutarse en segundo plano continuo sin ser interrumpido por el recolector de memoria del sistema operativo (_Low Memory Killer_).

  Se incorporó el archivo de audio (`audio.mp3`) en el directorio de recursos brutos de la aplicación (`app/src/main/res/raw/audioguia_epis.mp3`). Dentro de `AudioGuiaService`, la instancia de `MediaPlayer` se gestiona con configuración explícita de `AudioAttributes` (`USAGE_MEDIA`, `CONTENT_TYPE_MUSIC`), solicitud de foco de audio con `AudioFocusRequest`, vinculación con `MediaSessionCompat` para el subsistema de audio de Android 14 y liberación de recursos en `onDestroy()`:

  ```kotlin
  private fun iniciarAudioNuevo(edificioId: Int) {
      val edificio = EdificiosData.obtenerPorId(edificioId)
      liberarMediaPlayer()
      try {
          solicitarAudioFocus()
          val audioAttributes = AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_MEDIA)
              .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
              .build()

          val afd = resources.openRawResourceFd(edificio.audioResId)
          mediaPlayer = MediaPlayer().apply {
              setAudioAttributes(audioAttributes)
              setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
              afd.close()
              prepare()
              setVolume(1.0f, 1.0f)
              isLooping = false
              setOnCompletionListener {
                  actualizarEstado(servicioActivo = true, estaReproduciendo = false,
                      estaPausado = true, edificio = edificio,
                      posicionMs = duration, duracionMs = duration)
                  actualizarNotificacion(edificio, estaReproduciendo = false)
              }
              start()
          }
          val duracion = mediaPlayer?.duration ?: 0
          actualizarEstado(servicioActivo = true, estaReproduciendo = true,
              estaPausado = false, edificio = edificio, posicionMs = 0, duracionMs = duracion)
          
          val notificacion = construirNotificacion(edificio, estaReproduciendo = true)
          startForeground(ID_NOTIFICACION, notificacion)
          persistirUltimoEdificioEnRoom(edificio)
          iniciarMonitoreoProgreso()
      } catch (e: Exception) { e.printStackTrace() }
  }
  ```

  #figure(
    image("../img/report/studio_audioguiaservice.png", width: 95%),
    caption: [Implementación de AudioGuiaService en Android Studio: configuración de MediaPlayer con AudioAttributes, foco de audio y startForeground.],
  )

  #figure(
    image("../img/report/evidencia_1_reproduccion_foreground.png", width: 42%),
    caption: [Interfaz de usuario en ejecución: reproducción multimedia con barra de progreso reactiva y diseño minimalista.],
  )

  #v(0.8em)
  == Ejercicio 1: Ciclo de Edificaciones del Campus y Actualización Dinámica

  El Ejercicio 1 implementa la alternancia cíclica entre diferentes edificaciones del campus universitario mediante el control "Siguiente", actualizando en tiempo real la información visual tanto en la interfaz de usuario como en el título de la notificación persistente emitida por el Foreground Service.

  Se diseñó el catálogo de datos inmutables `EdificiosData` conteniendo cinco ubicaciones estratégicas del campus universitario (Pabellón EPIS, Biblioteca Central, Auditorio Magno, Laboratorio de IA y Pabellón de Ingenierías). La navegación circular se calcula determinísticamente a través del método `obtenerSiguiente(idActual)`:

  ```kotlin
  fun obtenerSiguiente(idActual: Int): Edificio {
      val indiceActual = listaEdificios.indexOfFirst { it.id == idActual }
      val siguienteIndice = if (indiceActual != -1) {
          (indiceActual + 1) % listaEdificios.size
      } else 0
      return listaEdificios[siguienteIndice]
  }
  ```

  Al disparar la acción `ACCION_SIGUIENTE` (ya sea desde el botón en pantalla o desde el botón integrado en la notificación del sistema), el servicio detiene la pista actual, carga la descripción del siguiente edificio, reinicia el `MediaPlayer` y refresca la notificación activa mediante `NotificationManager.notify()`, garantizando que el usuario observe la transición de "Pabellón EPIS" hacia "Biblioteca Central" de manera instantánea.

  #figure(
    image("../img/report/studio_audioguiascreen.png", width: 95%),
    caption: [Estructura de AudioGuiaScreen en Android Studio: suscripción del observador de ciclo de vida con ProcessLifecycleOwner.],
  )

  #v(0.8em)
  == Ejercicio 2: Notificación Interactiva con Acciones Multimedia (MediaStyle)

  La notificación persistente del Foreground Service incorpora controles de acción integrados que permiten interactuar con el reproductor directamente desde la barra de estado y pantalla de bloqueo de Android, sin requerir que la aplicación esté abierta.

  Se configuraron tres acciones mediante `PendingIntent.getService` con las banderas obligatorias `FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT`:
  1. *Pausar / Reanudar*: Conmuta el estado de reproducción y actualiza el icono y texto dinámicamente.
  2. *Siguiente*: Ejecuta la acción `ACCION_SIGUIENTE` avanzando a la siguiente parada del campus.
  3. *Detener*: Ejecuta la acción `ACCION_DETENER`, liberando el `MediaPlayer` y finalizando el servicio con `stopForeground(STOP_FOREGROUND_REMOVE)` y `stopSelf()`.

  Asimismo, se empleó `androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken).setShowActionsInCompactView(0, 1, 2)`, permitiendo que los tres botones aparezcan agrupados y visibles incluso en la vista compacta de la notificación.

  #figure(
    image("../img/report/studio_service_notification.png", width: 95%),
    caption: [Construcción de la notificación interactiva en Android Studio: PendingIntent para acciones y estilo MediaStyle.],
  )

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    figure(
      image("../img/report/evidencia_2_notificacion_background.png", width: 95%),
      caption: [Notificación persistente en cortina del sistema: controles multimedia interactivos (Pausar/Reanudar, Siguiente, Detener).],
    ),
    figure(
      image("../img/report/evidencia_3_notificacion_siguiente_edificio.png", width: 95%),
      caption: [Actualización dinámica del título de la notificación a 'Biblioteca Central' tras la acción interactiva de siguiente edificación.],
    )
  )

  #figure(
    image("../img/report/studio_controles_layout.png", width: 95%),
    caption: [Definición de ControlesPrincipales en Android Studio: botones con padding compacto y restricción estricta de una sola línea.],
  )

  #v(0.8em)
  == Ejercicio 3: Persistencia de Estado con Room Database y Reanudación

  El Ejercicio 3 almacena de manera permanente el último edificio reproducido para que, al cerrar la aplicación y volver a abrirla en el futuro, el usuario cuente con un botón interactivo "Continuar" que le permita retomar la visita guiada desde el punto exacto donde la dejó.

  Se implementó la entidad Room `UltimoEdificioEntity` con clave primaria fija (`id = 1`) para actuar como registro singleton de la última sesión:

  ```kotlin
  @Entity(tableName = "ultimo_edificio")
  data class UltimoEdificioEntity(
      @PrimaryKey val id: Int = 1,
      val edificioId: Int,
      val nombreEdificio: String,
      val categoria: String,
      val timestamp: Long
  )
  ```

  El DAO `UltimoEdificioDao` expone un flujo reactivo `Flow<UltimoEdificioEntity?>` que `AudioGuiaViewModel` recolecta y convierte en estado de interfaz mediante `stateIn()`. Cada vez que el servicio inicia la reproducción de cualquier edificio, ejecuta de manera asíncrona en una corrutina de `Dispatchers.IO` la inserción con reemplazo (`OnConflictStrategy.REPLACE`).

  #figure(
    image("../img/report/studio_room_dao.png", width: 95%),
    caption: [Interfaz UltimoEdificioDao en Android Studio: persistencia y observación reactiva del último punto de interés con Room.],
  )

  #figure(
    image("../img/report/evidencia_4_app_biblioteca_central.png", width: 42%),
    caption: [Interfaz de usuario tras reinicio: tarjeta de reanudación y punto de interés activo mediante persistencia local.],
  )
]
