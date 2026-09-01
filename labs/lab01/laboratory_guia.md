Aquí tienes la transcripción completa y fiel del documento en formato Markdown:

---

# UNIVERSIDAD NACIONAL DE SAN AGUSTÍN
### FACULTAD DE INGENIERÍA DE PRODUCCIÓN Y SERVICIOS
### ESCUELA PROFESIONAL DE INGENIERÍA DE SISTEMAS

**Formato:** Guía de Práctica de Laboratorio / Talleres / Centros de Simulación  
**Aprobación:** 2022/03/01 | **Código:** GUIA-PRLD-001 | **Página:** 1

---

## GUÍA DE LABORATORIO
**(formato docente)**

### INFORMACIÓN BÁSICA

| | |
| :--- | :--- |
| **ASIGNATURA:** | INTRODUCCIÓN AL DESARROLLO DE NUEVAS PLATAFORMAS |
| **TÍTULO DE LA PRÁCTICA:** | Introducción al desarrollo de aplicaciones móviles |
| **NÚMERO DE PRÁCTICA:** | 1 |
| **AÑO LECTIVO:** | 2026B |
| **NRO. SEMESTRE:** | VIII |
| **TIPO DE PRÁCTICA:** | INDIVIDUAL [ ] | **GRUPAL [ X ]** | **MÁXIMO DE ESTUDIANTES:** 2 |
| **FECHA INICIO:** | 01/09/2026 | **FECHA FIN:** | 05/09/2026 | **DURACIÓN:** 100 minutos |
| **RECURSOS A UTILIZAR:** | Computador personal, navegador web (Chrome, Firefox o Edge), editor de texto o código (Visual Studio Code o similar), conexión a internet. |
| **DOCENTE(s):** | Roxana Evelyn Limache Calatayud |

---

### OBJETIVOS/TEMAS Y COMPETENCIAS

**OBJETIVOS:**
* Comprender qué es una aplicación móvil y su importancia en las nuevas plataformas.
* Identificar los elementos básicos que forman parte de una aplicación móvil.
* Reconocer las diferencias entre aplicaciones móviles, web y de escritorio.
* Analizar ejemplos de aplicaciones móviles según su propósito y funcionamiento.

**TEMAS:**
* Introducción al desarrollo de aplicaciones móviles
* Nuevas plataformas de desarrollo
* Características de las aplicaciones móviles
* Tipos de aplicaciones móviles
* Estructura general de una aplicación móvil

**COMPETENCIAS:**
* **C.a** El estudiante reconoce las características fundamentales del desarrollo en nuevas plataformas y analiza la estructura básica de una aplicación móvil.
* **C.b** Investiga nuevos modelos, metodologías, técnicas, herramientas y tecnologías por ser necesarias para mantener la vigencia en el desempeño profesional.
* **C.c**
* **C.d**

---

### CONTENIDO DE LA GUÍA

**Página: 2**

#### I. MARCO CONCEPTUAL

Revisar el siguiente recurso antes de la sesión:
* **Video:** "Tipos de apps. Diferencias entre nativas, híbridas y web apps" — [https://www.youtube.com/watch?v=9cOzoGkUnVM](https://www.youtube.com/watch?v=9cOzoGkUnVM)
* **Android Developers**, introducción al desarrollo de apps: [https://developer.android.com/](https://developer.android.com/ )

**Nuevas plataformas de desarrollo**  
El avance de la tecnología ha dado lugar a nuevas plataformas sobre las cuales se pueden desarrollar aplicaciones, como los teléfonos inteligentes, tabletas, relojes inteligentes, televisores inteligentes y otros dispositivos conectados. Estas plataformas permiten crear soluciones más cercanas al usuario, orientadas a la movilidad, la conectividad y la interacción constante.

**Aplicaciones móviles**  
Una aplicación móvil es un software diseñado para ejecutarse en dispositivos móviles como celulares o tabletas. Estas aplicaciones permiten realizar múltiples tareas, como enviar mensajes, consultar información, registrar datos, entretenerse, comprar productos o gestionar actividades personales y académicas.

**Importancia del desarrollo móvil**  
El desarrollo de aplicaciones móviles es importante porque los dispositivos móviles forman parte de la vida diaria de las personas. Muchas necesidades actuales se resuelven mediante aplicaciones, por ejemplo:
* Comunicación
* Educación
* Banca
* Salud
* Transporte
* Comercio electrónico

**Características de una aplicación móvil**  
Entre las características más comunes se encuentran:
* Interfaz adaptada a pantallas pequeñas
* Interacción mediante toques o gestos
* Posibilidad de trabajar con conexión o sin conexión
* Acceso a recursos del dispositivo
* Rapidez en tareas específicas

**Tipos de aplicaciones móviles**  
Las aplicaciones pueden clasificarse, de manera general, en tres tipos: **nativas**, desarrolladas específicamente para un sistema operativo (Android o iOS); **web móviles**, que funcionan directamente desde el navegador sin necesidad de instalación; e **híbridas**, que combinan características de ambas. La Figura 1 resume sus diferencias principales.

---

**Página: 3**

**Figura 1. Tipos de aplicaciones móviles: nativas, web e híbridas (elaboración propia).**

*   **Nativa:** Se desarrolla para un sistema operativo específico (Android o iOS). Máximo rendimiento y acceso total al hardware. *(Ej: apps de banca)*
*   **Web móvil:** Se ejecuta desde el navegador. No requiere instalación; depende de conexión a internet. *(Ej: catálogos online)*
*   **Híbrida:** Combina un núcleo web con un contenedor nativo. Se instala como app pero reutiliza código web. *(Ej: apps con Flutter/Compose Multiplatform)*

**Estructura básica de una aplicación móvil**  
Una aplicación móvil suele estar compuesta por cinco elementos principales, que le permiten responder a las acciones del usuario y cumplir su función específica, tal como se observa en la Figura 2.

**Figura 2. Estructura básica de una aplicación móvil (elaboración propia).**
1.  **Interfaz de usuario:** Pantallas y componentes con los que interactúa el usuario.
2.  **Lógica de funcionamiento:** Reglas y procesos internos que definen el comportamiento de la app.
3.  **Manejo de eventos:** Respuestas a acciones del usuario: toques, gestos, entradas de texto.
4.  **Navegación entre pantallas:** Flujo que sigue el usuario de una pantalla a otra.
5.  **Almacenamiento de datos:** Guarda información en el dispositivo o en la nube para su uso posterior.

---

**Página: 4**

#### II. EJERCICIO/PROBLEMA RESUELTO POR EL DOCENTE

A continuación se presenta un análisis resuelto, a modo de ejemplo, que ilustra el tipo de razonamiento y de estructura que se espera en los ejercicios propuestos. También puede apoyarse en el siguiente video como referencia introductoria:
[https://www.youtube.com/watch?v=9cOzoGkUnVM](https://www.youtube.com/watch?v=9cOzoGkUnVM)

**Ejemplo resuelto: análisis de la aplicación WhatsApp**

*   **Paso 1. Identificar el tipo de aplicación.** WhatsApp es una aplicación híbrida: reutiliza un núcleo de lógica común entre plataformas, pero se distribuye e instala como una app nativa en Android e iOS, con acceso a funciones del dispositivo como la cámara, los contactos y las notificaciones.
*   **Paso 2. Describir su interfaz de usuario.** Está compuesta por la lista de chats, la ventana de conversación, el teclado y los botones de envío; una interfaz simple, adaptada a pantallas pequeñas.
*   **Paso 3. Describir su lógica de funcionamiento.** Se encarga de cifrar, enviar y sincronizar los mensajes entre los dispositivos de los usuarios que participan en una conversación.
*   **Paso 4. Describir el manejo de eventos.** La aplicación responde a acciones como tocar un chat para abrirlo, deslizar para archivarlo o mantenerlo presionado para seleccionar varios.
*   **Paso 5. Describir la navegación entre pantallas.** El usuario pasa de la lista de chats a una conversación específica, y desde ahí puede acceder a los ajustes o al perfil de un contacto.
*   **Paso 6. Describir el almacenamiento de datos.** Los mensajes, contactos y archivos multimedia se guardan tanto en el propio dispositivo como en una copia de respaldo en la nube.
*   **Paso 7. Organizar el análisis en un esquema que resuma los cinco elementos identificados, como se observa en la Figura 3.**

**Figura 3. Estructura básica de una aplicación móvil aplicada al ejemplo de WhatsApp (elaboración propia).**
*   **WhatsApp (app híbrida de mensajería)**
    *   **Interfaz de usuario:** Lista de chats, ventana de conversación, teclado y botones de envío.
    *   **Lógica de funcionamiento:** Cifra, envía y sincroniza los mensajes entre los dispositivos.
    *   **Manejo de eventos:** Detecta toques en un chat, deslizar para archivar, mantener presionado.
    *   **Navegación entre pantallas:** Pasa de la lista de chats a una conversación, a ajustes o al perfil.
    *   **Almacenamiento de datos:** Guarda mensajes, contactos y multimedia en el dispositivo y en la nube.

Este mismo procedimiento —identificar el tipo de aplicación y describir sus cinco elementos estructurales— es el que debe aplicarse en el Ejercicio 3 de los ejercicios propuestos.

---

**Página: 5**

#### III. EJERCICIOS/PROBLEMAS PROPUESTOS

Para el desarrollo de los siguientes ejercicios, ten en cuenta las siguientes indicaciones: redacta las respuestas con tus propias palabras (no copiar directamente de páginas web ni de herramientas de inteligencia artificial), procura comprender y poder explicar los conceptos investigados, incluye diagramas o esquemas propios que faciliten la comprensión, y presenta el documento con un formato ordenado. El docente podrá realizar preguntas sobre el contenido durante la exposición.

*   **Ejercicio 1.** Investiga y explica con tus propias palabras: ¿Qué es una aplicación móvil? ¿Qué se entiende por nuevas plataformas de desarrollo? ¿Por qué es importante el desarrollo móvil en la actualidad?
*   **Ejercicio 2.** Elabora un cuadro comparativo entre aplicación móvil, aplicación web y aplicación de escritorio, considerando al menos: dispositivo donde se ejecuta, conexión a internet, instalación, acceso a recursos del equipo y ejemplos.
*   **Ejercicio 3.** Explica cuáles son las partes básicas de una aplicación móvil (interfaz, lógica, eventos, navegación, datos), siguiendo el mismo procedimiento del ejercicio resuelto por el docente. Acompaña tu explicación con un esquema o diagrama propio.
*   **Ejercicio 4.** Elige seis aplicaciones móviles de uso frecuente y completa una tabla con: nombre de la aplicación, finalidad, tipo de usuario, funciones principales y elementos de interfaz que utiliza.
*   **Ejercicio 5.** Plantea una idea sencilla de aplicación móvil que ayude a resolver una necesidad real, indicando: nombre de la app, problema que busca resolver, usuario al que está dirigida, funciones básicas y posible estructura de pantallas. Este ejercicio servirá como base para el trabajo de las siguientes unidades.

**Entregable:** informe (formato estudiante) con las respuestas desarrolladas, los cuadros y tablas solicitados, y los esquemas o diagramas elaborados.

#### IV. CUESTIONARIO
1. ¿Cuál es la diferencia principal entre una aplicación nativa, una aplicación web y una aplicación híbrida? Menciona en qué situación elegirías cada una.
2. De los cinco elementos que conforman la estructura básica de una aplicación móvil (interfaz, lógica, eventos, navegación, datos), ¿cuál consideras más crítico para que la app funcione correctamente? Justifica tu respuesta.

#### V. REFERENCIAS Y BIBLIOGRAFÍA RECOMENDADAS:
* [1] Android Developers, "Introducción al desarrollo de aplicaciones Android". Disponible: [https://developer.android.com/](https://developer.android.com/ )
* [2] Material y apuntes de Jetpack Compose | Android Studio – GitHub. Disponible: [https://github.com/empm/JetpackCompose](https://github.com/empm/JetpackCompose)
* [3] Android Developers, "Create your first Android app". Disponible: [https://developer.android.com/training/basics/firstapp](https://developer.android.com/training/basics/firstapp)
* [4] Material Design, "Introduction". Disponible: [https://m3.material.io/](https://m3.material.io/)
* [5] SwiftUI Learning – GitHub. Disponible: [https://github.com/slekens/SwiftUI-Learning](https://github.com/slekens/SwiftUI-Learning)

---

**Página: 6**

* [6] "Tipos de apps. Diferencias entre nativas, híbridas y web apps", video. Disponible: [https://www.youtube.com/watch?v=9cOzoGkUnVM](https://www.youtube.com/watch?v=9cOzoGkUnVM)

### TÉCNICAS E INSTRUMENTOS DE EVALUACIÓN

| TÉCNICAS: | INSTRUMENTOS: |
| :--- | :--- |
| Ejercicios propuestos | Rúbrica de evaluación |

**CRITERIOS DE EVALUACIÓN**
* Cumplimiento de los objetivos y ejercicios propuestos en la guía.
* Correcta aplicación de los conceptos y herramientas del tema desarrollado.
* Funcionamiento correcto de lo implementado (compilación/ejecución sin errores).
* Informe (formato estudiante) completo, con evidencias y cuestionario resuelto.
* Entrega dentro del plazo establecido.
