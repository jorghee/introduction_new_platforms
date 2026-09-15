=== 1. ¿Qué ventajas tiene usar Navigation Compose (una sola Activity con varias pantallas) frente al enfoque tradicional de múltiples Activities comunicadas por Intent?

La adopción de una arquitectura de una sola Activity (Single-Activity Architecture) impulsada por *Navigation Compose* nos ofreció varias ventajas importantes frente al enfoque clásico:

1. *Menor sobrecarga del sistema (Overhead):* Crear y destruir Activities es un proceso pesado para el sistema operativo Android. Al usar Composables, simplemente estamos re-dibujando funciones en memoria sobre el mismo lienzo, lo que hace que la transición entre pantallas sea significativamente más rápida y fluida.
2. *Gestión centralizada del estado:* En lugar de tener que serializar objetos pesados mediante `Intents` o `Bundles` para pasarlos entre Activities, en Compose gran parte de la jerarquía comparte el mismo contexto. 
3. *Flujo declarativo:* Al definir todo en un solo `NavHost`, tuvimos visibilidad completa de todo el grafo de navegación del aplicativo en un único archivo (`MainActivity.kt`), lo que nos facilita escalar y mantener el código.
