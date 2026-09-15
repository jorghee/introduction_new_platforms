=== Ejercicio 1: Creación de `HomeScreen` y paso de parámetros

Para este ejercicio, necesitábamos implementar una tercera pantalla (`HomeScreen`) en el grafo de navegación que fuera capaz de recibir dinámicamente el nombre del usuario y mostrarlo como mensaje de bienvenida tras un inicio de sesión exitoso.

Primero, agregamos la ruta `"home/{usuario}"` dentro de nuestro `NavHost` en el archivo `MainActivity.kt`. Para recibir el parámetro de forma segura, declaramos la variable a través de `navArgument("usuario")` especificando que sería del tipo `StringType`. 

Una vez extraído el parámetro del `backStackEntry`, se lo inyectamos directamente al composable `HomeScreen`. 

Además de esto, observamos que para mantener una navegación coherente y evitar que el usuario vuelva a la pantalla de "Login" presionando el botón físico de atrás en su dispositivo, debíamos destruir esa ruta de la pila. Para ello, utilizamos `popUpTo("login") { inclusive = true }`.

**[CAPTURA REQUERIDA - Código]**
Capturar el archivo `MainActivity.kt`, mostrando específicamente el bloque del `NavHost` donde se define el `composable("home/{usuario}")` y cómo se extraen sus argumentos para llamar a `HomeScreen`.

Posteriormente, desarrollamos la vista `HomeScreen.kt`, la cual recibe la cadena del usuario y la concatena en un texto utilizando MaterialTheme. 

**[CAPTURA REQUERIDA - Ejecución]**
Ejecutar la aplicación en el emulador, realizar un inicio de sesión y capturar la pantalla donde se observa el mensaje "Bienvenido [Tu_Usuario]" dentro de `HomeScreen`.
