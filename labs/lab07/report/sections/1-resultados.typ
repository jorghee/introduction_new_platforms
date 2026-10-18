#import "../components/lab-section.typ": lab-section

#lab-section(title: "RESULTADOS DE LOS EJERCICIOS PROPUESTOS")[

  #highlight(fill: rgb("fff176"))[Link del repositorio GitHub]: #link("https://github.com/jorghee/introduction_new_platforms/tree/main/labs/lab07")[https://github.com/jorghee/introduction_new_platforms/tree/main/labs/lab07]

  == Desarrollo de la Solución

  Partiendo del ejercicio base guiado por la docente, la solución se expandió arquitecturalmente para superar las limitaciones de mantener datos "cableados" (_hardcoded_) y estados locales efímeros. Se diseñó un sistema desacoplado, modular y escalable, estructurado sobre cinco fases clave de ingeniería.

  === 1. Del Enfoque Tradicional de Vistas al Canvas Declarativo de Compose

  En el sistema clásico de vistas de Android (`android.view.View`), la creación de componentes gráficos personalizados requería crear subclases de `View`, sobrecargar el método imperativo `onDraw(canvas: android.graphics.Canvas)` y gestionar de forma manual y compleja los eventos táctiles en `onTouchEvent(event: MotionEvent)` @compose_graphics. Este paradigma implicaba lidiar con objetos mutables como `Paint`, coordinar llamadas manuales a `invalidate()` y mapear manualmente los punteros táctiles, lo que dificultaba la sincronización entre el estado interno y la pantalla.

  Con Jetpack Compose, dicho flujo se simplifica drásticamente mediante el Composable `Canvas`. Este componente no hereda de ninguna clase de vista ni retiene estado interno pesado; en su lugar, expone un ámbito de dibujo (`DrawScope`) puramente declarativo. Al mutar cualquier dato de estado observado, Compose redibuja automáticamente el lienzo sin intervención manual. La detección de toques se traslada de forma homogénea a la cadena de modificadores mediante `Modifier.pointerInput`, aprovechando la función de suspensión `detectTapGestures` @compose_pointer_input.

  #figure(
    image("../img/report/diagrama_secuencia_interaccion.png", width: 100%),
    caption: [Diagrama de secuencia UML: Interacción temporal del toque físico, propagación al ViewModel y recomposición reactiva],
  )

  === 2. Modelado de Ambientes del Campus y Renderizado Vectorial (Ejercicio Propuesto 1)

  El primer ejercicio propuesto requirió ampliar el plano para representar la visualización interior de al menos 4 ambientes reales del campus universitario, integrando rectángulos, muros divisorios y etiquetas legibles visibles en el propio plano. Se seleccionó el primer piso del Pabellón EPIS (UNSA), modelando cuatro zonas críticas:
  - *Lab 101 - Redes y Telecomunicaciones:* Dotado de racks y equipamiento de infraestructura de red.
  - *Lab 102 - Desarrollo de Software:* Equipado con estaciones de alta gama para desarrollo móvil y computación en la nube.
  - *Aula Magna / Sala de Grados:* Auditorio principal para defensas de tesis y conferencias magistrales.
  - *Patio Central y Área de Conectividad:* Zona de libre esparcimiento académico con cobertura institucional de Eduroam.

  Para cumplir con la exigencia de que cada ambiente cuente con su etiqueta visible sobre el plano, se utilizó la API moderna de tipografía en Canvas introducida en Jetpack Compose: `rememberTextMeasurer()` y `drawText()` @compose_text_measurer. En lugar de superponer torpemente Composables `Text` fuera del lienzo, `drawText` permite rasterizar texto vectorial directamente sobre las coordenadas relativas del `DrawScope`. Se calculó dinámicamente la posición del texto para garantizar su centrado horizontal dentro del tercio superior de cada rectángulo delimitador:

  ```kotlin
  val textLayout = textMeasurer.measure(
      text = ambiente.etiqueta,
      style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E1E22))
  )
  val labelX = ambiente.topLeft.x + (ambiente.size.width - textLayout.size.width) / 2f
  drawText(textLayoutResult = textLayout, topLeft = Offset(labelX, ambiente.topLeft.y + 12f))
  ```

  #figure(
    image("../img/report/captura01_canvas_pabellon.png", width: 45%),
    caption: [Visualización en el emulador del plano interactivo del Pabellón EPIS con 4 ambientes y etiquetas vectoriales visibles],
  )

  #figure(
    image("../img/report/captura10_canvas_codigo.png", width: 95%),
    caption: [Entorno de desarrollo Android Studio: Implementación del `Canvas` y renderizado de primitivas en `PlanoInteractivoScreen.kt` junto al emulador activo],
  )

  === 3. Persistencia Desacoplada y Fuente de Datos en Assets (Ejercicio Propuesto 2)

  Mantener las coordenadas, dimensiones, colores y fichas informativas de los ambientes escritas de forma estática en el código fuente de Kotlin representa una mala práctica de desarrollo que dificulta el mantenimiento del plano. Por ello, en concordancia con el patrón de persistencia visto en el Laboratorio 03, se externalizó toda la información arquitectónica hacia el archivo local `app/src/main/assets/plano_epis.json`.

  Para acceder a esta fuente de datos, se construyó la clase `PlanoRepository`. Este componente interactúa con el `AssetManager` de Android para abrir el flujo de entrada (`InputStream`), leer el documento estructurado y deserializar los arreglos de ambientes, líneas y marcadores utilizando `org.json.JSONObject`. Esta estrategia garantiza que cualquier cambio en la distribución del campus o en la ubicación de los salones pueda corregirse modificando únicamente el archivo JSON, sin necesidad de recompilar la lógica visual ni alterar los Composables.

  #figure(
    image("../img/report/diagrama_clases_arquitectura.png", width: 60%),
    caption: [Diagrama de clases UML: Arquitectura estructural, modelos de dominio y desacoplamiento mediante el repositorio],
  )

  #figure(
    image("../img/report/captura06_json_assets.png", width: 95%),
    caption: [Estructura del archivo `plano_epis.json` en `app/src/main/assets` en Android Studio junto a la ejecución de la aplicación],
  )

  === 4. Centralización Reactiva en PlanoViewModel y Algoritmo de Detección de Toques (Ejercicio Propuesto 3)

  En el ejercicio resuelto por la docente, el estado (`marcadores` y `marcadorSeleccionado`) residía internamente en el Composable mediante `remember` y `mutableStateOf`. Siguiendo los principios de arquitectura limpia y Unidirectional Data Flow (UDF) profundizados en el Laboratorio 05, se trasladó la totalidad del estado hacia un `ViewModel` denominado `PlanoViewModel` @android_viewmodel.

  La clase `PlanoViewModel` hereda de `AndroidViewModel`, lo que le permite acceder de manera segura al contexto de aplicación para el `PlanoRepository` sin ocasionar fugas de memoria (_memory leaks_). El estado se encapsula de forma inmutable a través de un `StateFlow<PlanoUiState>`. Al interactuar con el lienzo, el Composable captura las coordenadas físicas del toque y las despacha al ViewModel mediante la función `procesarToqueEnCanvas(offset)`.

  #figure(
    image("../img/report/captura07_viewmodel_state.png", width: 95%),
    caption: [Gestión del estado reactivo inmutable y emisión con `StateFlow` en `PlanoViewModel.kt` dentro de Android Studio],
  )

  El procesamiento del toque implementa un esquema de prioridad en dos niveles:
  1. *Colisión Euclidiana (Marcadores Circulares):* Se itera la colección de marcadores evaluando si la distancia entre el toque y el centro del círculo es menor o igual a su radio:
     $sqrt((x_("toque") - x_("centro"))^2 + (y_("toque") - y_("centro"))^2) <= r$
     Si existe colisión, se actualiza `marcadorSeleccionado` en el estado, lo que gatilla la apertura inmediata del `AlertDialog` con la información técnica del ambiente.
  2. *Colisión AABB (Recintos Rectangulares):* Si el toque no coincide con ningún marcador, se evalúa si las coordenadas caen dentro de los límites de algún ambiente (`x in [x1, x2]` e `y in [y1, y2]`). En caso afirmativo, se asigna `ambienteSeleccionado`, provocando un repintado con trazo de borde resaltado (`Stroke` de 3.5 dp) en el Canvas.

  #figure(
    image("../img/report/diagrama_deteccion_toques.png", width: 100%),
    caption: [Diagrama de flujo del algoritmo de resolución geométrica de colisiones (Distancia Euclidiana y límites AABB)],
  )

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    figure(
      image("../img/report/captura02_dialogo_marcador.png", width: 85%),
      caption: [Diálogo emergente `AlertDialog` mostrando la ficha técnica al interactuar con un marcador circular],
    ),
    figure(
      image("../img/report/captura03_ambiente_seleccionado.png", width: 85%),
      caption: [Resaltado perimetral y panel informativo contextual al pulsar sobre la superficie de un ambiente rectangular],
    )
  )

  #figure(
    image("../img/report/captura11_pruebas_unitarias.png", width: 95%),
    caption: [Pruebas unitarias automatizadas en `PlanoInteractivoUnitTest.kt` en Android Studio validando la detección euclidiana y AABB],
  )

  === 5. Integración Multi-pantalla y Accesibilidad desde el Mapa (Ejercicio Propuesto 4)

  El último ejercicio propuesto requirió vincular el plano interactivo como una nueva pantalla accesible desde el flujo de navegación de la aplicación (continuando la pantalla `MapaScreen` iniciada en el Laboratorio 05). Para ello, se configuró un `NavHost` central orquestado por un `Scaffold` con barra de navegación inferior (`NavigationBar`) @compose_navigation.

  Se diseñó la pantalla `MapaCampusScreen`, la cual ofrece una panorámica del campus de Ingenierías de la UNSA e incluye una tarjeta destacada interactiva para el Pabellón EPIS. Al pulsar el botón "Explorar Plano del Pabellón", el `NavController` ejecuta la transición fluida hacia `PlanoInteractivoScreen`, reutilizando la misma instancia del `PlanoViewModel` compartido. Adicionalmente, la barra inferior cuenta con pestañas dedicadas para alternar entre el mapa general, el plano EPIS, el módulo norte y los servicios del edificio.

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    figure(
      image("../img/report/captura04_mapa_acceso.png", width: 85%),
      caption: [Pantalla `MapaCampusScreen` con la tarjeta interactiva que da acceso directo al plano del pabellón],
    ),
    figure(
      image("../img/report/captura09_servicios_directorio.png", width: 85%),
      caption: [Pantalla `InfoScreen` con el directorio y catálogo de servicios institucionales del pabellón],
    )
  )

  #figure(
    image("../img/report/diagrama_estados_ui.png", width: 55%),
    caption: [Diagrama de estados UML: Ciclo de vida y transiciones de la interfaz gestionadas por `PlanoViewModel`],
  )
]
