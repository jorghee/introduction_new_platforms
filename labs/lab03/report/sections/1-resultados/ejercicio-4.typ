=== Ejercicio 4: Reto Opcional (Validación de campos vacíos)

Finalmente, asumimos el reto opcional que requería agregar una validación de campos incompletos para los formularios de ambas pantallas antes de procesar cualquier información o intentar interactuar con el sistema de archivos.

Lo resolvimos agregando una validación reactiva empleando los estados gestionados por Jetpack Compose. Implementamos el chequeo `if (usuario.isBlank() || password.isBlank())` tanto en el evento `onClick` del botón "Ingresar" (`LoginScreen`) como en el botón "Aceptar" (`RegistroScreen`).

Si la evaluación resulta verdadera, interrumpimos el flujo con un `return@Button` y asignamos el mensaje "Por favor, llene todos los campos." a la variable mutable de estado `mensajeError`, lo que hace que Compose re-dibuje la interfaz instantáneamente mostrando la advertencia de color rojo al usuario sin hacer crashear la aplicación.

**[CAPTURA REQUERIDA - Código]**
Capturar el archivo `LoginScreen.kt` o `RegistroScreen.kt`, mostrando el bloque `onClick` del botón principal donde se evidencia la validación `isBlank()` y el retorno anticipado.

**[CAPTURA REQUERIDA - Ejecución]**
Ejecutar la aplicación en el emulador, dejar los campos de texto completamente vacíos, presionar el botón "Ingresar" y tomar una captura evidenciando el mensaje de error de validación mostrado en pantalla.
