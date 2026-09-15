=== Ejercicio 4: Reto Opcional (Validación de campos vacíos)

Finalmente, asumimos el reto opcional que requería agregar una validación de campos incompletos para los formularios de ambas pantallas antes de procesar cualquier información o intentar interactuar con el sistema de archivos.

Lo resolvimos agregando una validación reactiva empleando los estados gestionados por Jetpack Compose. Implementamos el chequeo `if (usuario.isBlank() || password.isBlank())` tanto en el evento `onClick` del botón "Ingresar" (`LoginScreen`) como en el botón "Aceptar" (`RegistroScreen`).

Si la evaluación resulta verdadera, interrumpimos el flujo con un `return@Button` y asignamos el mensaje "Por favor, llene todos los campos." a la variable mutable de estado `mensajeError`, lo que hace que Compose re-dibuje la interfaz instantáneamente mostrando la advertencia de color rojo al usuario sin hacer crashear la aplicación.

#figure(
  image("../../img/report/ej4_codigo.png", width: 80%),
  caption: [Retorno anticipado ante campos vacíos],
)

#figure(
  image("../../img/report/ej4_ejecucion.png", width: 30%),
  caption: [Mensaje de error reactivo en interfaz],
)
