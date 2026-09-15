=== Ejercicio 3: Validación de Login mediante archivo plano

El tercer ejercicio consistía en modificar la validación estática de la pantalla de inicio de sesión para que el acceso se determinara contrastando las credenciales únicamente contra el archivo `cuentas.txt` y ya no mediante una lista en memoria.

Para ello, implementamos la función `obtenerCuentasRegistradas()` en nuestro `AccountManager`. Esta función abre el archivo utilizando `context.openFileInput(FILE_NAME)`, lo lee línea por línea con un BufferedReader y divide cada línea mediante el delimitador configurado para reconstruir una lista de objetos tipo `Pair<String, String>`.

Posteriormente, en la función de control principal `validarCuenta()`, verificamos si las credenciales coinciden iterando sobre las tuplas obtenidas.

#figure(
  image("../../img/report/ej3_codigo.png", width: 80%),
  caption: [Función validarCuenta contrastando credenciales],
)

En la capa de presentación (`LoginScreen.kt`), dependiendo de la respuesta booleana devuelta por el manager, enrutamos al usuario hacia `HomeScreen` o pintamos el texto de error "Cuenta no encontrada o credenciales incorrectas".

#figure(
  image("../../img/report/ej3_ejecucion.png", width: 30%),
  caption: [Transición erronea tras iniciar sesión validando con archivo],
)
