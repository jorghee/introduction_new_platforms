#import "../components/lab-section.typ": lab-section

#lab-section(title: "CONCLUSIONES")[

  1. *Resiliencia de Procesos Multimedia frente al Low Memory Killer (LMK):*
     La implementación de un _Foreground Service_ de tipo `mediaPlayback` vincula el proceso a una notificación persistente mediante `startForeground()`. Esto eleva su prioridad operativa en el kernel de Linux (`oom_score_adj`), protegiéndolo de la terminación intempestiva por presión de memoria RAM y garantizando la reproducción ininterrumpida de audio.

  2. *Descentralización del Control Operativo con NotificationCompat.MediaStyle:*
     El empleo de `MediaStyle` con `PendingIntent` traslada los controles esenciales ("Pausar/Reanudar", "Siguiente" y "Detener") a la cortina de notificaciones y pantalla de bloqueo, permitiendo al usuario interactuar con la audio-guía de forma inmediata sin necesidad de mantener la aplicación abierta.

  3. *Conciencia Contextual del Ciclo de Vida mediante ProcessLifecycleOwner:*
     `ProcessLifecycleOwner` permite monitorear el ciclo de vida global de la aplicación (`ON_START` y `ON_STOP`). Esta conciencia contextual coordina la UI declarativa de Jetpack Compose con los servicios en segundo plano, adaptando los indicadores visuales y optimizando los recursos del sistema sin provocar fugas de memoria.

  4. *Sincronización Reactiva Unidireccional con StateFlow:*
     La comunicación entre `AudioGuiaService`, `AudioGuiaViewModel` y los componentes Composable a través de `StateFlow` desacopla la lógica multimedia de la capa visual. El ViewModel recolecta de forma segura el progreso (`posicionMs`, `duracionMs`) y estado operativo, garantizando recomposiciones fluidas.

  5. *Persistencia Determinística y Continuidad de Sesión con Room Database:*
     La persistencia local del último punto de interés en SQLite (`audioguia_campus.db`) mediante Room permite reanudar el recorrido tras cerrar la aplicación. La observación reactiva de `Flow<UltimoEdificioEntity?>` expone de inmediato el estado guardado, consolidando una arquitectura desacoplada, tolerante a fallos y orientada a la experiencia de usuario.
]
