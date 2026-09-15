=== Ejercicio 1: Creación de `HomeScreen` y paso de parámetros

Para este ejercicio, necesitábamos implementar una tercera pantalla (`HomeScreen`) en el grafo de navegación que fuera capaz de recibir dinámicamente el nombre del usuario y mostrarlo como mensaje de bienvenida tras un inicio de sesión exitoso.

Primero, agregamos la ruta `"home/{usuario}"` dentro de nuestro `NavHost` en el archivo `MainActivity.kt`. Para recibir el parámetro de forma segura, declaramos la variable a través de `navArgument("usuario")` especificando que sería del tipo `StringType`.

Una vez extraído el parámetro del `backStackEntry`, se lo inyectamos directamente al composable `HomeScreen`.

Además de esto, observamos que para mantener una navegación coherente y evitar que el usuario vuelva a la pantalla de "Login" presionando el botón físico de atrás en su dispositivo, debíamos destruir esa ruta de la pila. Para ello, utilizamos `popUpTo("login") { inclusive = true }`.

#figure(
  image("../../img/report/ej1_codigo_mainactivity.png", width: 80%),
  caption: [Código del NavHost en MainActivity],
)

Posteriormente, desarrollamos la vista `HomeScreen.kt`, la cual recibe la cadena del usuario y la concatena en un texto utilizando MaterialTheme.

#figure(
  image("../../img/report/ej1_codigo_homescreen.png", width: 80%),
  caption: [Código del screen HomeScreen],
)

Realizamos un ejemplo donde el usuario realiza un registro de cuenta y luego puede realizar el inicio de sesión. Podremos observar que los datos son pasados con éxito.

#figure(
  image("../../img/report/ej1_ejecucion_register.png", width: 80%),
  caption: [Pantalla de Registro de cuenta],
)

#figure(
  image("../../img/report/ej1_ejecucion_login.png", width: 80%),
  caption: [Pantalla de Inicion de sesión],
)

#figure(
  image("../../img/report/ej1_ejecucion_home.png", width: 80%),
  caption: [Pantalla de HomeScreen con los datos dinámicos],
)
