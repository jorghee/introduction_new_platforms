=== Ejercicio 3: Validación de Login mediante archivo plano

El tercer ejercicio consistía en modificar la validación estática de la pantalla de inicio de sesión para que el acceso se determinara contrastando las credenciales únicamente contra el archivo `cuentas.txt` y ya no mediante una lista en memoria.

Para ello, implementamos la función `obtenerCuentasRegistradas()` en nuestro `AccountManager`. Esta función abre el archivo utilizando `context.openFileInput(FILE_NAME)`, lo lee línea por línea con un BufferedReader y divide cada línea mediante el delimitador configurado para reconstruir una lista de objetos tipo `Pair<String, String>`.

Posteriormente, en la función de control principal `validarCuenta()`, verificamos si las credenciales coinciden iterando sobre las tuplas obtenidas. 

**[CAPTURA REQUERIDA - Código]**
Capturar el archivo `AccountManager.kt`, mostrando específicamente el método `validarCuenta()` donde se aprecia que lee desde el archivo y retorna el resultado booleano usando `cuentas.any { ... }`.

En la capa de presentación (`LoginScreen.kt`), dependiendo de la respuesta booleana devuelta por el manager, enrutamos al usuario hacia `HomeScreen` o pintamos el texto de error "Cuenta no encontrada o credenciales incorrectas".

**[CAPTURA REQUERIDA - Ejecución]**
Ejecutar la aplicación. Tratar de ingresar con una cuenta inválida (y capturar el error). Luego, ingresar con la cuenta recién registrada en el Ejercicio 2 y capturar el momento de la transición exitosa al Home.
