#import "../components/lab-section.typ": lab-section

#lab-section(title: "RESULTADOS DE LOS EJERCICIOS PROPUESTOS")[

  == Desarrollo de la Implementación

  Para cumplir con los ejercicios propuestos en el laboratorio, se realizó una transición desde una arquitectura basada en el pase de funciones lambda (State Hoisting básico) hacia un modelo más maduro y escalable centralizado en el uso de un *Shared ViewModel*. A continuación, se detalla el proceso de diseño y las justificaciones técnicas detrás de la implementación a lo largo de cinco pasos fundamentales.

  === 1. Diseño del ViewModel Compartido

  En aplicaciones Compose, cuando múltiples pantallas necesitan acceder o modificar un mismo dato (en este caso, el nombre del edificio seleccionado), pasar este valor de ida y vuelta a través de lambdas produce un acoplamiento fuerte y vuelve el código difícil de mantener (un fenómeno conocido como _prop drilling_). Para solucionarlo, creamos la clase `SeleccionViewModel` que hereda de la clase `ViewModel` base de Android @android_viewmodel.

  Dentro de esta clase, se optó por implementar el estado mediante un `StateFlow` (`MutableStateFlow`) en lugar del clásico `mutableStateOf`. La razón técnica para preferir `StateFlow` es que pertenece de manera nativa a la librería de corrutinas de Kotlin @kotlin_stateflow, lo que permite un manejo asíncrono puro y facilita enormemente la futura aplicación de pruebas unitarias o la transformación de flujos complejos en un entorno Clean Architecture. Además, para garantizar el principio de encapsulamiento, el estado interno mutable (`_edificioSeleccionado`) se mantiene privado y se expone públicamente un flujo derivado de solo lectura.

  #figure(
    image("../img/report/diagrama_comparativa.png", width: 45%),
    caption: [Comparativa de flujos: _State Hoisting_ (lambdas) vs _Shared ViewModel_],
  )

  #figure(
    image("../img/report/captura02_viewmodel.png", width: 100%),
    caption: [Implementación encapsulada del estado en `SeleccionViewModel`],
  )
  === 2. Inyección de Estado a nivel de NavGraph

  El éxito de un *Shared ViewModel* radica en definir correctamente su ámbito de vida (_scope_). Si instanciáramos el ViewModel independientemente dentro de cada pantalla (es decir, creando uno nuevo para Edificios y otro nuevo para Home), obtendríamos dos espacios de memoria completamente distintos y la información no se compartiría.

  Para que `EdificiosScreen` y `HomeScreen` accedan exactamente al mismo objeto en memoria, la instancia única se solicitó en el nivel jerárquico superior dentro de `MainScreen.kt`, empleando la función generadora `viewModel()`. Al realizar esto en el Composable padre que contiene al `NavHost`, la instancia de `SeleccionViewModel` queda atada al ciclo de vida del *NavGraph*. Esto significa que el ViewModel sobrevivirá intacto a la rotación de la pantalla (evitando la pérdida de datos y reinicios molestos) y se destruirá automáticamente solo cuando el usuario destruya la Actividad, previniendo así de forma efectiva cualquier fuga de memoria (memory leaks).

  #figure(
    image("../img/report/diagram_viewmodel.png", width: 35%),
    caption: [Diagrama de inyección y flujo reactivo del Shared ViewModel],
  )

  #figure(
    image("../img/report/diagrama_ciclovida.png", width: 55%),
    caption: [Secuencia del ciclo de vida del ViewModel atado al _scope_ del NavHost],
  )

  #figure(
    image("../img/report/captura05a_mainscreen.png", width: 100%),
    caption: [Inyección global del Shared ViewModel atado al ciclo de vida en `MainScreen` (Parte 1)],
  )

  #figure(
    image("../img/report/captura05b_mainscreen.png", width: 100%),
    caption: [Inyección global del Shared ViewModel atado al ciclo de vida en `MainScreen` (Parte 2)],
  )

  === 3. Delegación de Escritura en EdificiosScreen

  A nivel de interfaz gráfica, el propósito de `EdificiosScreen` es simplemente pintar una lista de opciones. En la versión guiada por el docente, la vista enviaba el dato seleccionado llamando a la lambda `onEdificioSeleccionado`, la cual terminaba delegando la actualización hacia arriba.

  En nuestra implementación mejorada, `EdificiosScreen` recibe la instancia inyectada de nuestro `ViewModel`. Al pulsar el botón "Ver" dentro de la iteración de la `LazyColumn`, se invoca la función pública `seleccionarEdificio(nombre)`. Esta importante decisión de arquitectura transfiere por completo la responsabilidad y la lógica de mutar estados desde la interfaz hacia el ViewModel. La UI pasa a ser un componente netamente declarativo y "tonto" (_dumb component_) que solo sabe renderizarse a sí mismo y avisarle al orquestador qué evento acaba de ocurrir.

  #figure(
    image("../img/report/captura03_edificios.png", width: 100%),
    caption: [Transferencia de responsabilidad de escritura desde la UI hacia el orquestador],
  )

  === 4. Recomposición Reactiva en HomeScreen

  La verdadera fortaleza del paradigma declarativo y reactivo en Compose se aprecia en cómo `HomeScreen` lee la información. Para escuchar los cambios en tiempo real del edificio seleccionado, se empleó el operador `collectAsState()` sobre el flujo reactivo.

  La justificación técnica detrás de esto es que `collectAsState()` suscribe internamente a nuestro Composable al flujo asíncrono emitido por el ViewModel. Cada vez que `EdificiosScreen` altera el valor en memoria, el flujo emite un nuevo `String`; el estado interno se actualiza e instruye directamente al compilador de Jetpack Compose a gatillar una *recomposición* inteligente. `HomeScreen` vuelve a ejecutarse redibujando el nuevo nombre del edificio sin que nosotros hayamos escrito ninguna lógica manual del tipo `textView.setText(...)`, resultando en un comportamiento predecible y un código mucho más robusto que lidiar con funciones lambda de retorno.

  #figure(
    image("../img/report/captura04_home.png", width: 100%),
    caption: [Suscripción reactiva y repintado de vista mediante `collectAsState` en `HomeScreen`],
  )

  #figure(
    image("../img/report/captura07_evidencia_home.png", width: 35%),
    caption: [Pantalla `HomeScreen` reflejando de forma inmediata y automática el último cambio de estado dictado por la otra pestaña],
  )

  === 5. Integración Fluida de la Cuarta Pestaña (Perfil)

  Finalmente, el laboratorio solicitaba expandir las capacidades de enrutamiento agregando una cuarta sección a la barra de navegación de la aplicación. En el paradigma de _Navigation Compose_ @compose_navigation, este tipo de adiciones requieren intervenir exactamente tres puntos fuertemente estructurados por nuestro diseño:

  1. *El enrutador tipado:* Se añadió el objeto `Perfil` a la clase sellada `Screen.kt`. Esto es sumamente útil y preferible a pasar simples _Strings_ mágicos (`"perfil"`), ya que previene por completo los errores tipográficos fatales al intentar compilar rutas en el NavHost.
  2. *El componente visual:* Se definió el archivo `PerfilScreen.kt` conteniendo un Composable básico alineado al centro que funge temporalmente como marcador de posición.
  3. *El NavigationBarItem:* Se modificó la lista que iteraba la barra `bottomBar` para renderizar nuestro cuarto botón. Para esto, se reutilizó el diseño Material Design mediante el ícono pre-empaquetado `Icons.Default.Person`.

  Una de las mayores ventajas de haber construido todo esto dentro de un contenedor `Scaffold` puro de Compose es que la barra inferior recalculó geométricamente su espacio y se distribuyó por sí sola para acomodar cuatro íconos y etiquetas, algo que con el antiguo sistema de Views y XML habríamos tenido que rediseñar a mano en los layouts de Android.

  #figure(
    image("../img/report/captura01_screen.png", width: 100%),
    caption: [Definición estricta y segura de la nueva ruta en la clase sellada `Screen`],
  )

  #figure(
    image("../img/report/captura06_perfil_emulador.png", width: 40%),
    caption: [Visualización en tiempo real de la cuarta pestaña adaptándose fluidamente en el `NavigationBar`],
  )

  #figure(
    image("../img/report/diagrama_navegacion.png", width: 70%),
    caption: [Esquema de enrutamiento y accesibilidad mediante el NavigationBar],
  )
]
