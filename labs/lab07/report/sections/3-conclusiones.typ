#import "../components/lab-section.typ": lab-section

#lab-section(title: "CONCLUSIONES")[
  
  - *Eficiencia y simplicidad del paradigma gráfico declarativo:* La adopción del Composable `Canvas` y la API de `DrawScope` en Jetpack Compose demostró ser notablemente superior a la arquitectura clásica de vistas (`View.onDraw`). Se eliminó la sobrecarga de mantener instancias pesadas de `Paint` y sincronizaciones manuales con `invalidate()`, logrando un pipeline gráfico limpio, donde cualquier mutación en el estado reactivo gatilla un redibujado instantáneo de la superficie de dibujo.

  - *Dominio del sistema de coordenadas y detección matemática de colisiones:* Al tratarse de una única superficie de dibujo continua sin elementos hijos en el árbol semántico, la interacción táctil exige el cálculo matemático explícito de coordenadas locales. La implementación combinada del cálculo de distancia euclidiana ($d <= r$) para pines circulares y la prueba de límites en cajas alineadas con los ejes (AABB) para salas rectangulares permitió estructurar un sistema de interacción táctil con prioridades bien delimitadas.

  - *Madurez arquitectónica mediante ViewModel y deserialización local:* Externalizar el plano arquitectónico hacia un archivo estructurado `plano_epis.json` dentro de `assets/` y centralizar la gestión de eventos en `PlanoViewModel` representó una mejora determinante sobre el enfoque guiado inicial. Este desacoplamiento cumple con el patrón Unidirectional Data Flow (UDF), facilitando el mantenimiento a largo plazo de la aplicación, ya que añadir nuevos salones o modificar coordenadas no requiere alterar el código fuente de Kotlin.

  - *Integración armónica en flujos multi-pantalla:* La vinculación del plano arquitectónico como una pantalla secundaria accesible desde una tarjeta destacada en `MapaCampusScreen` completó el ciclo de navegación iniciado en el Laboratorio 05. El uso de rutas selladas en `Screen` y la integración fluida en un contenedor `Scaffold` con `NavigationBar` consolidan una arquitectura móvil modular, robusta y escalable.
]
