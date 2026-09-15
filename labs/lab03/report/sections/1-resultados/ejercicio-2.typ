=== Ejercicio 2: Persistencia de datos en Registro

La implementación de la persistencia de datos en registro consistirá en los siugientes pasos: al presionar "Aceptar" en la pantalla de registro, guardáramos las credenciales del usuario en un archivo plano de texto (`cuentas.txt`) dentro del almacenamiento interno de la app utilizando `MODE_APPEND`.

Para mantener un proyecto ordenado y seguir buenas prácticas de la arquitectura en Android, decidimos crear un gestor de persistencia que actuara como única fuente de la verdad (Single Source of Truth). Implementamos el objeto `AccountManager` en un nuevo paquete `data/`.

Dentro de `AccountManager.kt`, creamos la función `guardarCuenta()`, la cual recibe el Contexto para acceder al almacenamiento y escribe el binomio usuario-contraseña concatenado con un delimitador.

#figure(
  image("../../img/report/ej2_codigo.png", width: 80%),
  caption: [Función guardarCuenta con MODE_APPEND],
)

Luego, en `RegistroScreen.kt`, recuperamos el contexto de Compose mediante `LocalContext.current` y lo utilizamos para llamar a nuestra función de guardado antes de navegar hacia atrás (`navController.popBackStack()`). Comprobamos que esto soluciona el requerimiento sin acoplar la lógica de escritura directamente en la vista.

#figure(
  image("../../img/report/ej2_ejecucion.png", width: 30%),
  caption: [Toast confirmando registro exitoso],
)
