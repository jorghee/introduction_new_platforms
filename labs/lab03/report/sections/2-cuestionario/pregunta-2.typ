=== 2. Además de los argumentos de navegación, ¿qué otro mecanismo podrías usar para compartir datos entre pantallas en una app Compose (por ejemplo, un ViewModel compartido)? Explica brevemente cómo funcionaría.

Aunque los argumentos de navegación (como usamos en `navArgument("usuario")`) son útiles para datos primarios o simples como Strings y Enteros, *no son el mecanismo recomendado* para objetos complejos o dominios pesados. Para ello, utilizaríamos un *ViewModel compartido*.

*¿Cómo funcionaría?*
Podríamos instanciar un `ViewModel` a nivel superior (por ejemplo, pasándolo como parámetro en el nivel del `NavHost`) o utilizando un _NavGraph_ anidado. De este modo:
1. El `ViewModel` alojaría las variables de estado (por ejemplo, `StateFlow` o `LiveData`) que representan la sesión del usuario.
2. La pantalla de `LoginScreen` inyectaría el `ViewModel` y, tras un inicio de sesión exitoso, actualizaría su estado.
3. Al navegar a `HomeScreen`, esta pantalla también inyectaría el mismo `ViewModel` (ya instanciado) y simplemente reaccionaría al estado previamente almacenado.

Esto nos liberaría de la responsabilidad de pasar los datos mediante la ruta del `NavController`, desacoplando completamente la lógica de estado de la lógica de navegación y permitiendo la supervivencia de los datos ante cambios de configuración (como girar la pantalla).
