#import "../components/lab-section.typ": lab-section

#lab-section(title: "CUESTIONARIO")[

  === 1. ¿Lograste resolver completamente el Ejercicio/Problema Resuelto por el Docente (datos de edificios y ambientes almacenados con Room, que persisten al cerrar y volver a abrir la app)? Adjunta una captura de pantalla del emulador mostrando el resultado como evidencia.

  Sí, el Ejercicio Resuelto por el Docente se implementó y validó de forma íntegra. El sistema ejecuta con éxito el flujo completo de persistencia local mediante la arquitectura de tres componentes de Room (`Entity`, `DAO` y `Database`):

  1. *Inicialización y Sembrado Inicial Condicional:*
     Al iniciar la aplicación por primera vez, el `EdificiosViewModel` consulta a través del repositorio si la tabla `EdificioEntity` se encuentra vacía (`dao.obtenerTodos().isEmpty()`). Al cumplirse la condición en una base de datos recién creada, se insertan dos edificios iniciales ("Biblioteca Central" con categoría "Servicios", y "Facultad de Ingeniería" con categoría "Académico") junto con sus respectivos ambientes ("Sala de lectura", "Hemeroteca", "Laboratorio de cómputo") utilizando claves foráneas que aseguran la integridad referencial.
  2. *Consulta Relacional:*
     Se realiza la consulta `@Transaction @Query("SELECT * FROM EdificioEntity")` que recupera la estructura jerárquica `EdificioConAmbientes` mediante las anotaciones `@Embedded` y `@Relation`.
  3. *Verificación de Persistencia tras Cierre Forzado:*
     Para verificar que los datos no residen únicamente en la memoria RAM volátil, la aplicación fue cerrada y expulsada de los procesos activos del sistema operativo mediante el comando:
     ```bash
     adb shell am force-stop com.example.persistenciaroom_compose
     ```
     Al reabrir la aplicación, la base de datos física `app.db` conservó intactos los registros previamente sembrados sin duplicar la información (evitado por la comprobación `isEmpty()`), cargando inmediatamente los registros en la lista `LazyColumn` de Jetpack Compose.

  #figure(
    image("../img/report/captura02_relacion_edificios_ambientes.png", width: 38%),
    caption: [Evidencia de ejecución en el emulador: Persistencia de edificios y sus ambientes relacionados tras reinicio en frío de la aplicación],
  )

  === 2. ¿Qué otra alternativa de almacenamiento (además de Room) se podría emplear en una aplicación con necesidades similares? Justifica tu respuesta comparando ventajas y desventajas frente a Room.

  En el ecosistema de desarrollo de Android moderno existen múltiples alternativas a Room para persistir datos estructurados localmente. A continuación se analizan cuatro tecnologías comparables frente a las necesidades de modelado relacional de la aplicación:

  ==== A. SQLDelight
  SQLDelight es una biblioteca desarrollada por Cash App basada en el paradigma _SQL-first_. A diferencia de Room (donde se escriben clases en Kotlin con anotaciones y la librería genera el SQL), en SQLDelight el desarrollador escribe sentencias SQL estándar en archivos `.sq`, y el compilador genera código Kotlin fuertemente tipado para interactuar con SQLite.

  - *Ventajas frente a Room:*
    - *Soporte Kotlin Multiplatform (KMP):* Funciona de manera nativa en Android, iOS, Desktop (JVM) y Web (Wasm/JS), permitiendo compartir el 100% de la lógica de datos.
    - *SQL nativo sin magia de anotaciones:* Ofrece control absoluto sobre las consultas complejas, índices, disparadores y optimizaciones de SQLite.
  - *Desventajas frente a Room:*
    - Curva de aprendizaje más pronunciada al requerir redacción manual exhaustiva de esquemas DDL y consultas DML.
    - Menor integración nativa con herramientas de Android Studio (por ejemplo, el Database Inspector tiene soporte de primer nivel optimizado para Room).

  ==== B. Realm / Atlas Device SDK
  Realm es un motor de base de datos orientado a objetos y no relacional (NoSQL), implementado en C++ a nivel de núcleo, diseñado específicamente para dispositivos móviles.

  - *Ventajas frente a Room:*
    - *Desaparición del desfase objeto-relacional (ORM):* Los objetos del modelo de dominio se guardan y leen directamente como objetos vivos en memoria, sin necesidad de mapeo entre tablas, columnas y filas de SQL.
    - *Sincronización en la nube integrada:* Cuenta con capacidades integradas para replicar datos automáticamente con bases de datos en la nube (MongoDB Atlas).
  - *Desventajas frente a Room:*
    - Mayor sobrecarga en el tamaño final del archivo APK (alrededor de 3 a 5 MB adicionales debido a las librerías binarias nativas `.so`).
    - Bloqueo de proveedor (_vendor lock-in_) frente al formato universal y estándar de SQLite.

  ==== C. ObjectBox
  ObjectBox es una base de datos orientada a objetos de altísimo rendimiento con soporte para relaciones entre entidades mediante enlaces de punteros en memoria (_Zero-Copy architecture_).

  - *Ventajas frente a Room:*
    - Rendimiento en operaciones de lectura y escritura significativamente superior a SQLite/Room en escenarios de alto volumen de transacciones por segundo.
    - Definición directa de relaciones 1:N mediante clases `ToMany` y `ToOne` sin necesidad de claves foráneas relacionales.
  - *Desventajas frente a Room:*
    - Ecosistema y comunidad más reducidos en comparación con la adopción masiva de Room respaldada oficialmente por Google.

  ==== D. DataStore (Preferences y Proto)
  Jetpack DataStore es el reemplazo moderno de `SharedPreferences`. Utiliza coroutines de Kotlin y Flow para almacenar pares clave-valor (Preferences DataStore) o estructuras de datos serializadas con Protocol Buffers (Proto DataStore).

  - *Ventajas frente a Room:*
    - Muy liviano y simple para almacenar configuraciones del usuario, tokens o estados atómicos de la interfaz.
  - *Desventajas frente a Room:*
    - *Inadecuado para datos relacionales:* No soporta consultas indexadas, filtros condicionales complejos, ni relaciones jerárquicas (1:N) como Edificios $->$ Ambientes $->$ Notas. Toda modificación requiere reescribir el archivo completo en disco.

  #table(
    columns: (1.2fr, 1.2fr, 1.4fr, 1.3fr, 1.1fr),
    align: (left, center, center, center, center),
    table.header(
      [*Tecnología*],
      [*Paradigma*],
      [*Validación en Compilación*],
      [*Soporte KMP*],
      [*Idoneidad Relacional*]
    ),
    [*Room*],
    [Relacional (ORM / SQLite)],
    [Sí (mediante KSP)],
    [Parcial (Room 2.7+)],
    [Excelente],

    [*SQLDelight*],
    [Relacional (SQL-first)],
    [Sí (desde archivos `.sq`)],
    [Total (Android, iOS, JVM)],
    [Excelente],

    [*Realm*],
    [NoSQL (Objetos vivos)],
    [Sí (esquema tipado)],
    [Limitado a Kotlin SDK],
    [Media (NoSQL)],

    [*ObjectBox*],
    [NoSQL (Objetos / Enlaces)],
    [Sí (procesador de código)],
    [Limitado],
    [Media (NoSQL)],

    [*DataStore*],
    [Clave-Valor / Protobuf],
    [Solo con Protobuf],
    [Total],
    [No idóneo]
  )

  *Conclusión comparativa:* Para una aplicación nativa de Android con requerimientos de relaciones estructuradas (Edificios $->$ Ambientes $->$ Notas) e integración con Jetpack Compose, *Room* sigue siendo la opción estándar recomendada por su soporte oficial en la plataforma, verificación estricta de consultas SQL en tiempo de compilación mediante KSP y consumo eficiente de recursos del sistema.

  === 3. ¿En dónde almacena Room los datos de la aplicación (almacenamiento interno o externo del dispositivo)? Justifica tu respuesta y explica qué implica para la privacidad de los datos del usuario.

  *Ubicación física del almacenamiento:*
  Room almacena los datos de la base de datos de manera estricta y predeterminada en el *almacenamiento interno privado* (_Internal Storage_) del dispositivo Android. 

  Específicamente, el archivo `app.db` se aloja en el directorio del entorno de seguridad de la aplicación dentro de la partición `/data`:
  ```text
  /data/data/com.example.persistenciaroom_compose/databases/app.db
  ```

  Cuando la base de datos opera bajo el modo WAL (_Write-Ahead Logging_) de SQLite, se generan dos archivos complementarios en la misma ruta:
  - `app.db-wal`: Registro de transacciones previas a la sincronización en disco, optimizando la concurrencia entre lecturas y escrituras simultáneas.
  - `app.db-shm`: Archivo de memoria compartida (_Shared Memory_) que coordina los punteros del registro WAL entre distintos procesos e hilos.

  *Justificación técnica y aislamiento por Sandboxing:*
  Esta ubicación se rige por el modelo de seguridad fundamental de Android basado en el _Sandboxing_ del kernel de Linux:
  1. *Aislamiento de Identidad (UID):* Durante la instalación del paquete `com.example.persistenciaroom_compose`, el sistema operativo le asigna un identificador de usuario único (`UID`, por ejemplo `u0_a185`). 
  2. *Permisos del Sistema de Archivos POSIX:* El directorio privado de la aplicación y sus archivos de base de datos se crean con permisos restringidos (`rwx------` para el directorio y `rw-------` para los archivos). Esto significa que *únicamente los procesos ejecutados bajo el UID de la aplicación* tienen privilegios para abrir, leer o escribir en esos ficheros.
  3. *Inaccesibilidad Externa:* Ninguna otra aplicación instalada en el dispositivo (incluso si cuenta con permisos generales de almacenamiento como `READ_EXTERNAL_STORAGE` o `MANAGE_EXTERNAL_STORAGE`) puede acceder al directorio `/data/data/com.example.persistenciaroom_compose/`. Solo un usuario con privilegios de superusuario (_root_) o una sesión de depuración autorizada por ADB (`run-as`) puede inspeccionar dicho directorio.
  4. *Ciclo de Vida Acoplado:* Si el usuario desinstala la aplicación o utiliza la opción "Borrar datos de almacenamiento" desde los ajustes del sistema, el sistema operativo purga automáticamente todo el árbol de directorios en `/data/data/`, garantizando que no queden restos de información en el almacenamiento persistente.

  *Implicaciones para la privacidad de los datos del usuario:*
  - *Protección contra fugas de información:* A diferencia del almacenamiento externo compartido (como `/sdcard/` o `Download/`), donde cualquier aplicación con permisos puede leer los ficheros de otras aplicaciones, el almacenamiento interno de Room garantiza la confidencialidad de la información (notas de mantenimiento, registros de infraestructura e inventarios de laboratorios).
  - *Cumplimiento normativo:* Esta arquitectura garantiza el principio de privacidad por diseño (_Privacy by Design_) exigido por marcos regulatorios como el GDPR o la Ley de Protección de Datos Personales (LPDP), evitando que datos personales o institucionales queden expuestos a vectores de ataque locales.
  - *Capacidad de cifrado avanzado:* Si una aplicación maneja datos altamente sensibles (médicos, bancarios o confidenciales), la capa de abstracción de Room permite incorporar la librería *SQLCipher* mediante `Room.databaseBuilder(...).openHelperFactory(SupportFactory(passphrase))`, logrando un cifrado transparente de 256 bits (AES-256) sobre el archivo físico en el almacenamiento interno para proteger los datos incluso frente a dispositivos vulnerados o con acceso root.
]
