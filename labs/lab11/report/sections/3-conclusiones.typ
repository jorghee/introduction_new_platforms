#import "../components/lab-section.typ": lab-section

#lab-section(title: "CONCLUSIONES")[

  1. *Resiliencia de Procesos Multimedia frente al Low Memory Killer (LMK):*
     La implementación de un _Foreground Service_ de tipo `mediaPlayback` demostró ser indispensable para garantizar la reproducción continua e ininterrumpida de audio en dispositivos Android modernos. Al vincular el servicio a una notificación persistente mediante `startForeground()`, el kernel de Linux eleva la prioridad del proceso (reduciendo drásticamente su `oom_score_adj`), protegiéndolo de la terminación agresiva por parte del Low Memory Killer incluso en situaciones de alta presión de memoria RAM en el sistema.

  2. *Descentralización del Control Operativo con NotificationCompat.MediaStyle:*
     El empleo de `MediaStyle` en combinación con `PendingIntent.getService` permite extender la funcionalidad de la aplicación más allá de la ventana gráfica tradicional. Al exponer botones de acción multimedia interactivos ("Pausar/Reanudar", "Siguiente edificio" y "Detener") directamente en la cortina de notificaciones y en la pantalla de bloqueo, se proporciona al usuario un mecanismo accesible y ergonómico para controlar la audio-guía del campus sin necesidad de desbloquear o conmutar de aplicación.

  3. *Conciencia Contextual del Ciclo de Vida mediante ProcessLifecycleOwner:*
     A diferencia de los observadores de ciclo de vida atados a una única `Activity`, `ProcessLifecycleOwner` proporciona una visión holística y reactiva del estado operativo de toda la aplicación. La detección precisa de los eventos `ON_START` (retorno a primer plano) y `ON_STOP` (paso a segundo plano) permite sincronizar dinámicamente indicadores de estado, optimizar recursos computacionales y coordinar la UI declarativa de Jetpack Compose con los servicios del sistema sin provocar fugas de memoria ni desincronización de flujos.

  4. *Sincronización Reactiva de Estado entre Servicios y Jetpack Compose:*
     La integración de `StateFlow` como canal de comunicación unidireccional entre `AudioGuiaService`, `AudioGuiaViewModel` y los componentes Composable demuestra ser la solución arquitectónica idónea frente a patrones heredados como `BroadcastReceiver` locales o `Bound Services` con interfaces AIDL complejas. El ViewModel recolecta de manera segura los estados emitidos por el reproductor (`posicionMs`, `duracionMs`, `estaReproduciendo`, `edificioActual`), asegurando recomposiciones eficientes y fluidas en la interfaz gráfica.

  5. *Persistencia Local Determinística y Recuperación de Sesión con Room Database:*
     La persistencia automática del último punto de interés visitado en una base de datos relacional SQLite gestionada por Room (`audioguia_campus.db`) garantiza una experiencia de usuario continua. Al iniciar la aplicación en frío, el flujo reactivo de Room (`Flow<UltimoEdificioEntity?>`) expone inmediatamente el registro persistido, permitiendo al usuario retomar la audio-guía mediante un único toque con el botón "Continuar", consolidando una arquitectura desacoplada, tolerante a fallos y alineada con las mejores prácticas de la ingeniería de software móvil.
]
