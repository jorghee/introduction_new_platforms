#import "../components/lab-section.typ": lab-section

#lab-section(title: "CUESTIONARIO")[

  === 1. ¿Lograste resolver completamente el Ejercicio/Problema Resuelto por el Docente (plano de un ambiente dibujado con Canvas, donde tocar un marcador abre un diálogo con su información)? Adjunta una captura de pantalla del emulador mostrando el resultado como evidencia.

  Sí, se resolvió de forma íntegra y rigurosa el ejercicio propuesto por la docente. Para evidenciar su exactitud y permitir una evaluación comparativa inmediata frente a los ejercicios propuestos ampliados, se implementó la pantalla dedicada `DocentePlanoScreen.kt` (accesible desde la tercera pestaña de la barra de navegación inferior).

  Esta implementación replica de manera exacta los Pasos 1 al 11 de la guía:
  - Creación de la estructura de datos `Marcador` con atributos `offset`, `radio`, `titulo` y `descripcion`.
  - Dibujo de los dos salones rectangulares (`Salón 1` en azul claro `#BBDEFB` y `Salón 2` en verde menta `#C8E6C9`) mediante `drawRect`.
  - Trazado del tabique o muro divisorio central en gris oscuro mediante `drawLine`.
  - Renderizado de dos marcadores circulares de radio $r = 40" px"$ en rojo mediante `drawCircle`.
  - Detección de toques con `Modifier.pointerInput(Unit) { detectTapGestures { ... } }`, calculando la distancia euclidiana `(offset - marcador.offset).getDistance() <= marcador.radio`.
  - Apertura del componente modal `AlertDialog` con el título y la descripción del salón pulsado al cumplirse la condición de contacto.

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    figure(
      image("../img/report/captura05_docente_base.png", width: 85%),
      caption: [Plano base del ejercicio docente con 2 salones, muro divisorio y marcadores interactivos],
    ),
    figure(
      image("../img/report/captura08_dialogo_modulo_norte.png", width: 85%),
      caption: [Diálogo modal informativo `AlertDialog` abierto tras tocar el marcador de la Sala A],
    )
  )

  === 2. En tu propia implementación, ¿cómo calculaste si un toque cayó dentro de un marcador circular? ¿Qué otra estrategia se te ocurre para detectar toques sobre formas no circulares, como un rectángulo o un polígono?

  *Cálculo implementado para marcadores circulares:*
  En nuestra solución, el evento de toque entregado por el callback `detectTapGestures` suministra un vector de posición $P_("toque") = (x_t, y_t)$ en píxeles locales del Canvas. Para verificar si este punto se encuentra en el interior del marcador de centro $C = (x_c, y_c)$ y radio $r$, se empleó el principio de la *Distancia Euclidiana*:
  $ d = ||P_("toque") - C|| = sqrt((x_t - x_c)^2 + (y_t - y_c)^2) $

  En Jetpack Compose, la clase `Offset` implementa la sobrecarga del operador de resta (`-`), retornando un vector diferencial, sobre el cual se invoca la función de extensión `.getDistance()`. Si la condición $d <= r$ se satisface, el toque colisiona con el marcador. 
  
  _Optimización computacional:_ En entornos de alto rendimiento o con cientos de marcadores, el cálculo de la raíz cuadrada puede omitirse comparando directamente las distancias al cuadrado para reducir ciclos de reloj en la CPU:
  $ (x_t - x_c)^2 + (y_t - y_c)^2 <= r^2 $

  *Estrategias de detección para formas no circulares:*
  1. *Formas Rectangulares (Axis-Aligned Bounding Box - AABB):*
     Para salones o recintos rectangulares paralelos a los ejes de coordenadas (definidos por su punto superior izquierdo $T = (x_0, y_0)$, ancho $W$ y alto $H$), se verifica si el punto cae dentro del intervalo cerrado de ambos ejes cartesianos:
     $ x_0 <= x_t <= (x_0 + W) quad and quad y_0 <= y_t <= (y_0 + H) $
     Esta estrategia fue implementada en nuestra clase `Ambiente` mediante la función `contienePunto(punto: Offset)`.
  2. *Polígonos Arbitrarios (Algoritmo de Ray Casting / Regla Par-Impar):*
     Si el plano contiene salas trapezoidales, polígonos irregulares o patios diagonales, se traza un rayo virtual horizontal infinito que parte desde el punto de toque $(x_t, y_t)$ hacia la derecha ($x -> oo$). Se cuentan cuántas veces dicho rayo interseca los segmentos de arista del polígono:
     - Si el número de intersecciones es *impar*, el punto se encuentra *dentro* del polígono.
     - Si el número de intersecciones es *par* o cero, el punto se encuentra *fuera*.
  3. *Uso de Rutas Vectoriales (`Path`) y Regiones Gráficas:*
     Otra alternativa en Android consiste en construir un objeto `androidx.compose.ui.graphics.Path` que encierre el contorno del polígono y evaluar la contención mediante `android.graphics.Region`, asociando una máscara rasterizada o empleando la función `Path.asAndroidPath()` junto a `Region.setPath(...)` para consultar si `region.contains(x, y)` es verdadero.

  === 3. En forma individual, comenta en el video de referencia qué otra forma se te ocurre para lograr un dibujo interactivo similar sin depender directamente de Canvas y DrawScope. (Captura del comentario; uno por integrante del grupo).

  *Propuesta del estudiante Jorge Luis Mamani Huarsaya:*
  #quote[
    "Una alternativa muy potente en Jetpack Compose para construir este tipo de planos sin depender de `Canvas` y `DrawScope` consiste en emplear una arquitectura de *Composición Jerárquica basada en `Box` con modificadores de posicionamiento absoluto o porcentual* (`Modifier.offset` o `Modifier.layout`). En lugar de pintar primitivas gráficas en un único lienzo indivisible, cada ambiente y marcador puede representarse como un Composable nativo independiente (por ejemplo, `Surface`, `Card` o `IconButton`). 
    
    Esta técnica ofrece dos ventajas críticas: en primer lugar, se aprovecha de forma directa el sistema nativo de eventos y clics de Compose (`Modifier.clickable`), eliminando por completo la necesidad de programar manualmente funciones matemáticas de colisión geométrica euclidiana. En segundo lugar, los componentes conservan propiedades inherentes de accesibilidad (soporte nativo para lectores de pantalla _TalkBack_ y semántica de UI), algo que un `Canvas` estándar pierde al ser tratado como una caja negra de píxeles."
  ]

  #figure(
    image("../img/report/captura_comentario_jorge.png", width: 75%),
    caption: [Evidencia del comentario publicado por Jorge Luis Mamani Huarsaya en el video tutorial de referencia],
  )

  *Propuesta del estudiante Yordano Hernan Boza Portilla:*
  #quote[
    "Otra alternativa sumamente escalable para lograr un plano interactivo sin depender directamente del Composable `Canvas` es la integración de *Gráficos Vectoriales Escalables (SVG) interactivos a través de `ImageVector` o bibliotecas de renderizado dinámico como AndroidSVG o Coil-SVG*.
    
    Bajo este enfoque, el plano del campus se modela previamente en herramientas de diseño vectorial (como Figma o Inkscape), asignando identificadores semánticos (`id="sala_101"`, `id="marcador_redes"`) a las etiquetas `<path>` y `<circle>` del archivo SVG. Al cargar el documento en la aplicación, la biblioteca parsea la jerarquía del DOM vectorial; mediante un interceptor de eventos táctiles, se captura el nodo XML tocado evaluando directamente la ruta vectorial del objeto. Esto permite un escalado vectorial infinito sin pérdida de nitidez, soporte nativo para zoom/pellizco (`pinch-to-zoom`) y estilos gráficos complejos (degradados, texturas y sombras) sin tener que escribir cientos de líneas de código de trazado manual en `DrawScope`."
  ]

  #figure(
    image("../img/report/captura_comentario_yordano.png", width: 75%),
    caption: [Evidencia del comentario publicado por Yordano Hernan Boza Portilla en el video tutorial de referencia],
  )
]
