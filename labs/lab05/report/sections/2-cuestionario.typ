#import "../components/lab-section.typ": lab-section

#lab-section(title: "CUESTIONARIO")[

  === 1. ¿Lograste resolver completamente el Ejercicio/Problema Resuelto por el Docente (pantallas Home, Edificios y Mapa navegables mediante NavigationBar, donde el edificio seleccionado en Edificios se refleja en Home)? Adjunta una captura de pantalla del emulador mostrando el resultado como evidencia.

  Sí, se logró implementar por completo la estructura solicitada de Navigation Compose. La comunicación fluye correctamente y el `HomeScreen` refleja la selección generada en `EdificiosScreen`.

  #figure(
    image("../img/report/captura07_evidencia_home.png", width: 40%),
    caption: [Pantalla Home reflejando el edificio seleccionado],
  )

  === 2. En tu propia implementación, ¿qué diferencia encontraste entre comunicar Composables mediante una función lambda y hacerlo mediante un ViewModel compartido? ¿Cuál de las dos alternativas consideras una mejor práctica? Explica brevemente los motivos.

  La principal diferencia es la *escalabilidad*. Con funciones lambda (State Hoisting básico), la información seleccionada debe "viajar" forzosamente como parámetro de vuelta hacia el Composable raíz (`MainScreen`) y luego descender hacia el otro Composable receptor. A medida que la app crece, este pasaje manual se vuelve insostenible (conocido como _prop drilling_).
  Por su parte, el *ViewModel compartido* proporciona un único punto de verdad ("Single Source of Truth"). Al inyectar la instancia de ViewModel a los Composables interesados, estos leen y escriben el estado de forma directa, reactiva y limpia. Definitivamente, el ViewModel es una mejor práctica para datos transversales o complejos dentro del scope de un flujo de navegación.

  === 3. En forma individual, explica qué ventaja adicional ofrece un ViewModel frente a otras formas de mantener el estado al navegar entre pantallas.

  La ventaja más significativa del `ViewModel` es su *conciencia sobre el ciclo de vida*. A diferencia de una variable global (`object` singleton) o un simple `remember`, el ViewModel sobrevive a los cambios de configuración como las rotaciones de pantalla, pero se destruye automáticamente cuando el ciclo de vida asociado (por ejemplo, el NavGraph de la Activity actual) es destruido definitivamente. Esto impide de forma efectiva las fugas de memoria y reinicios molestos de datos al rotar un teléfono, todo ello sin tener que sobrecargar la pila de navegación intentando serializar objetos gigantes a través de los argumentos de ruta del NavController.
]
