=== Pregunta 1

*¿Cuál es la diferencia principal entre una aplicación nativa, una aplicación web y una aplicación híbrida? Menciona en qué situación elegirías cada una.*

La diferencia principal radica en su tecnología de desarrollo y cómo interactúan con el dispositivo:
- *Aplicación Nativa:* Se desarrolla específicamente para el lenguaje del sistema operativo (por ejemplo, Swift para iOS, Kotlin/Java para Android). Ofrecen el mejor rendimiento y acceso total al hardware. Podemos utilizarlo cuando se requiere un alto rendimiento (como en videojuegos), animaciones complejas, o cuando es indispensable el acceso profundo a los recursos de hardware del dispositivo.
- *Aplicación Web Móvil:* Es un sitio web adaptado a pantallas pequeñas (Responsive Design) que se ejecuta en el navegador del dispositivo, sin necesidad de instalación. Podedmos usarlo cuando el presupuesto es limitado, se desea un desarrollo rápido multiplataforma sin depender de las tiendas de aplicaciones y no se necesitan funciones avanzadas del hardware.
- *Aplicación Híbrida:* Se desarrolla utilizando tecnologías web (HTML, CSS, JavaScript) o frameworks multiplataforma (Flutter, React Native) pero se encapsula en un contenedor nativo para ser instalada como una app normal. Podemos usarla cuando se desea abarcar tanto Android como iOS con un mismo código fuente (reduciendo costos y tiempo de desarrollo), y el rendimiento requerido es estándar (aplicaciones de gestión, noticias, catálogos).

#figure(image("../../img/lab/app_nativa_web_hibrida.png", width: 50%), caption: [Diferencia entre los conceptos])
