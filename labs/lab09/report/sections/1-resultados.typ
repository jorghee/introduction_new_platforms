#import "../components/lab-section.typ": lab-section

#lab-section(title: "RESULTADOS DE LOS EJERCICIOS PROPUESTOS")[

  #highlight(fill: rgb("fff176"))[Link del repositorio GitHub]: #link("https://github.com/jorghee/introduction_new_platforms/tree/main/labs/lab09")[https://github.com/jorghee/introduction_new_platforms/tree/main/labs/lab09]

  == Desarrollo de la Solución

  === Organización Modular del Código y Separación de Responsabilidades

  En el desarrollo de aplicaciones móviles con Android y Jetpack Compose, concentrar todas las entidades, interfaces de acceso a datos, lógica de presentación y componentes de interfaz gráfica dentro de un único archivo (como `MainActivity.kt`) representa un antipatrón arquitectónico conocido como _God Object_ o _Activity monolítica_. Este enfoque dificulta el mantenimiento, la depuración, las pruebas unitarias y la escalabilidad del sistema a medida que los requisitos crecen.

  Por ello, de forma previa a la resolución de los ejercicios propuestos, se estructuró el proyecto aplicando el principio de responsabilidad única (_Single Responsibility Principle_ - SRP) y la arquitectura recomendada por Google (MVVM - _Model-View-ViewModel_ con patrón Repositorio), organizando los módulos en paquetes específicos dentro de `com.example.persistenciaroom_compose`:

  #table(
    columns: (1.3fr, 1.2fr, 2.5fr),
    align: (left, left, left),
    table.header(
      [*Capa / Paquete*],
      [*Componentes Clave*],
      [*Responsabilidad Específica en la Solución*]
    ),
    [`data.local.entity`],
    [`EdificioEntity`, `AmbienteEntity`, `NotaEntity`],
    [Definen el esquema relacional de la base de datos SQLite. Cada clase representa una tabla física con sus claves primarias, foráneas e índices.],

    [`data.local.relation`],
    [`EdificioConAmbientes`, `AmbienteConNotas`],
    [Estructuras de datos que agrupan entidades relacionadas mediante `@Embedded` y `@Relation` para modelar consultas relacionales 1:N.],

    [`data.local.dao`],
    [`EdificioDao`],
    [Interfaz de acceso a datos (DAO). Declara de forma tipada las operaciones CRUD (`@Insert`, `@Update`, `@Delete`) y las consultas transaccionales (`@Transaction`).],

    [`data.local`],
    [`AppDatabase`],
    [Clase abstracta que hereda de `RoomDatabase`. Centraliza la configuración de SQLite, versionamiento y patrón Singleton de acceso seguro.],

    [`data.repository`],
    [`EdificioRepository`],
    [Fuente única de verdad (_Single Source of Truth_). Abstrae la base de datos de las capas superiores y gestiona las operaciones asíncronas.],

    [`ui.viewmodel`],
    [`EdificiosViewModel`, `EdificiosViewModelFactory`],
    [Mantiene el estado de la UI desacoplado del ciclo de vida de la Activity. Expone flujos reactivos `StateFlow` y procesa eventos de usuario.],

    [`ui.components`],
    [`EdificioItem`, `AmbienteNotaItem`, `DialogosCrud`],
    [Componentes Composables modulares y reutilizables para tarjetas visuales, listas anidadas y ventanas modales de edición.],

    [`ui.screens`],
    [`EdificiosScreen`],
    [Pantalla principal con navegación por pestañas (`TabRow`) y botón flotante de acción (`FloatingActionButton`).],

    [`com.example...`],
    [`MainActivity`],
    [Punto de entrada de la aplicación. Se limita a inicializar el repositorio e inyectarlo en el ViewModel dentro de `setContent`.]
  )

  Esta separación desacopla la persistencia de la interfaz visual, permitiendo modificar las consultas de Room o los esquemas de base de datos sin alterar los componentes gráficos de Jetpack Compose.

  === 1. Diagrama del Modelo de Datos Relacional con Room

  Para satisfacer los requerimientos del campus universitario y modelar el flujo de información de las edificaciones, se diseñó un modelo de datos relacional jerárquico compuesto por tres entidades vinculadas mediante relaciones de uno a muchos (1:N):

  1. *`EdificioEntity`:* Representa el nivel superior de la infraestructura física del campus (por ejemplo, "Pabellón EPIS", "Biblioteca Central", "Auditorio Magno").
  2. *`AmbienteEntity`:* Representa los recintos, laboratorios o salas ubicados dentro de un edificio específico (por ejemplo, "Lab 101 - Redes y Conectividad", "Hemeroteca y Tesis"). Se vincula con `EdificioEntity` mediante la clave foránea `edificioId`.
  3. *`NotaEntity`:* Modela las observaciones, apuntes técnicos, reportes de mantenimiento o estados de uso asociados a un ambiente determinado (por ejemplo, "Racks y switches de laboratorio operativos", "Proyector láser y microfonía inalámbrica verificados"). Se vincula con `AmbienteEntity` mediante la clave foránea `ambienteId`.

  #figure(
    image("../img/report/diagrama_modelo_datos_room.png", width: 65%),
    caption: [Diagrama Entidad-Relación (ERD) del modelo de datos con las tres entidades relacionadas en Room @room_relations],
  )

  La cardinalidad del modelo establece que:
  - Un *Edificio* puede albergar cero, uno o múltiples *Ambientes* ($1 : N$). Si un edificio se elimina, sus ambientes asociados se eliminan en cascada (`ForeignKey.CASCADE`) para preservar la coherencia de la base de datos.
  - Un *Ambiente* pertenece a un único *Edificio* y puede registrar cero, una o múltiples *Notas* ($1 : N$). Al eliminarse un ambiente, todas sus notas vinculadas se eliminan automáticamente mediante la misma regla de propagación en cascada.

  === 2. Implementación de Entidades, Claves Foráneas y DAOs

  La implementación técnica de las entidades en Room se realizó mediante clases de datos Kotlin anotadas con `@Entity`. En cumplimiento de las buenas prácticas de ingeniería de bases de datos relacionales, se definieron restricciones de clave foránea (`ForeignKey`) con acción de borrado en cascada (`onDelete = ForeignKey.CASCADE`) y se indexaron explícitamente las columnas foráneas (`indices = [Index(value = ["..."])]`) para prevenir escaneos de tabla completa (_full table scans_) en SQLite durante las operaciones de búsqueda y eliminación:

  ```kotlin
  @Entity(
      foreignKeys = [
          ForeignKey(
              entity = AmbienteEntity::class,
              parentColumns = ["id"],
              childColumns = ["ambienteId"],
              onDelete = ForeignKey.CASCADE
          )
      ],
      indices = [Index(value = ["ambienteId"])]
  )
  data class NotaEntity(
      @PrimaryKey(autoGenerate = true) val id: Int = 0,
      val ambienteId: Int,
      val texto: String
  )
  ```

  Para la manipulación de los datos se construyó la interfaz `EdificioDao`, declarando operaciones atómicas y reactivas ejecutadas en hilos de fondo mediante funciones de suspensión (`suspend`) de coroutines de Kotlin:
  - *Inserción:* `@Insert suspend fun insertarEdificio(...)`, `insertarAmbiente(...)`, `insertarNota(...)`.
  - *Actualización:* `@Update suspend fun actualizarEdificio(...)`, `actualizarAmbiente(...)`, `actualizarNota(...)`.
  - *Eliminación:* `@Delete suspend fun eliminarEdificio(...)`, `eliminarAmbiente(...)`, `eliminarNota(...)`.
  - *Lectura:* Consultas directas `@Query` y consultas relacionales transaccionales `@Transaction`.

  La base de datos se encapsuló en `AppDatabase`, configurada como un singleton seguro frente a hilos (_thread-safe_) mediante `@Volatile` y doble comprobación de bloqueo (_double-checked locking_):

  ```kotlin
  @Database(
      entities = [EdificioEntity::class, AmbienteEntity::class, NotaEntity::class],
      version = 1,
      exportSchema = false
  )
  abstract class AppDatabase : RoomDatabase() {
      abstract fun edificioDao(): EdificioDao

      companion object {
          @Volatile
          private var INSTANCE: AppDatabase? = null

          fun getInstance(context: Context): AppDatabase {
              return INSTANCE ?: synchronized(this) {
                  val instance = Room.databaseBuilder(
                      context.applicationContext,
                      AppDatabase::class.java,
                      "app.db"
                  ).fallbackToDestructiveMigration(true).build()
                  INSTANCE = instance
                  instance
              }
          }
      }
  }
  ```

  Para comprobar fehacientemente la creación física del archivo SQLite y la persistencia de las tres tablas, se inspeccionó el entorno mediante la herramienta _App Inspection / Database Inspector_ de Android Studio conectada al emulador en ejecución:

  #figure(
    image("../img/report/captura01_db_inspector_panel.png", width: 95%),
    caption: [Visualización de la base de datos `app.db` en el Database Inspector de Android Studio: Tablas `EdificioEntity`, `AmbienteEntity`, `NotaEntity` y registros de ambientes @room_training],
  )

  Asimismo, se extrajo la base de datos directamente del almacenamiento de la aplicación para validar los registros almacenados en cada una de las tablas mediante el motor de comandos `sqlite3`:

  ```sql
  -- Consulta directa sobre la base de datos SQLite física (app.db):
  SELECT * FROM EdificioEntity;
  -- 1|Pabellón EPIS|Académico
  -- 2|Biblioteca Central|Servicios
  -- 3|Auditorio Magno|Institucional

  SELECT * FROM AmbienteEntity;
  -- 1|1|Lab 101 - Redes y Conectividad
  -- 2|1|Lab 102 - Desarrollo de Software
  -- 3|2|Sala de Lectura Principal
  -- 4|2|Hemeroteca y Tesis
  -- 5|3|Sala Principal de Grados

  SELECT * FROM NotaEntity;
  -- 1|1|Racks y switches de laboratorio operativos
  -- 2|1|Capacidad física: 32 estaciones de trabajo
  -- 3|2|Equipos configurados con Android Studio y Compose
  -- 4|3|Horario ininterrumpido de 07:00 a 20:00
  -- 5|4|Consulta de material bibliográfico de posgrado
  -- 6|5|Proyector láser y microfonía inalámbrica verificados
  ```

  === 3. Consultas Relacionales con `@Relation` y `@Transaction`

  En Room, cuando se requiere obtener entidades vinculadas en una estructura jerárquica de objetos sin recurrir a complejas combinaciones manuales (`JOIN`) ni desnormalizar los datos, se emplean clases intermedias decoradas con `@Embedded` y `@Relation` @room_relations. 

  Se implementaron dos consultas relacionales independientes para resolver las necesidades del sistema:

  1. *Relación 1: `EdificioConAmbientes` (Edificio con sus Ambientes asociados):*
     Permite que cada edificio agrupe automáticamente su lista de ambientes:
     ```kotlin
     data class EdificioConAmbientes(
         @Embedded val edificio: EdificioEntity,
         @Relation(
             parentColumn = "id",
             entityColumn = "edificioId"
         )
         val ambientes: List<AmbienteEntity>
     )
     ```
  2. *Relación 2: `AmbienteConNotas` (Ambiente con sus Notas y Observaciones asociadas):*
     Constituye la segunda consulta relacional solicitada en la guía, permitiendo inspeccionar cada recinto junto a todas sus observaciones técnicas:
     ```kotlin
     data class AmbienteConNotas(
         @Embedded val ambiente: AmbienteEntity,
         @Relation(
             parentColumn = "id",
             entityColumn = "ambienteId"
         )
         val notas: List<NotaEntity>
     )
     ```

  En la interfaz `EdificioDao`, ambas operaciones se anotan con `@Transaction`:

  ```kotlin
  @Transaction
  @Query("SELECT * FROM EdificioEntity")
  suspend fun obtenerEdificiosConAmbientes(): List<EdificioConAmbientes>

  @Transaction
  @Query("SELECT * FROM AmbienteEntity")
  suspend fun obtenerAmbientesConNotas(): List<AmbienteConNotas>
  ```

  La anotación `@Transaction` es mandatoria porque Room realiza internamente múltiples consultas SQL separadas (una para la entidad padre y otra para recuperar los registros hijos asociados mediante cláusulas `IN`). Ejecutar estas consultas dentro de una transacción garantiza la atomicidad y coherencia temporal de la lectura, evitando inconsistencias o condiciones de carrera si otra operación en segundo plano modifica las tablas entre ambas lecturas.

  Las dos consultas relacionales se visualizan en la aplicación mediante pestañas dedicadas:

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    figure(
      image("../img/report/captura02_relacion_edificios_ambientes.png", width: 62%),
      caption: [Pestaña 1: Consulta relacional `EdificioConAmbientes` mostrando cada edificio con sus ambientes asignados],
    ),
    figure(
      image("../img/report/captura03_relacion_ambientes_notas.png", width: 62%),
      caption: [Pestaña 2: Consulta relacional `AmbienteConNotas` mostrando cada ambiente con sus observaciones técnicas],
    )
  )

  === 4. Interfaz de Usuario y Operaciones CRUD en Tiempo Real

  Para ofrecer una experiencia interactiva completa, la interfaz gráfica implementada en `EdificiosScreen.kt` incluye controles visuales para ejecutar todas las operaciones de modificación y eliminación sobre los datos persistidos:

  - *Navegación contextual por pestañas:* Se diseñó un encabezado interactivo con `TabRow` que permite alternar instantáneamente entre la vista de *Edificios y Ambientes* y la vista de *Ambientes y Notas*.
  - *Botones de acción CRUD en cada elemento:*
    - Cada tarjeta de edificio dispone de botones para *Editar* su nombre/categoría y *Eliminar* el edificio completo (gatillando la eliminación en cascada de sus ambientes).
    - Cada ambiente incluye botones de *Editar* y *Borrar*, además de un botón rápido para agregar nuevos ambientes o notas.
    - Cada nota técnica dispone de botones dedicados para *Editar* su contenido textual y *Borrarla*.
  - *Formularios en ventanas modales (`AlertDialog`):* Se construyó el módulo `DialogosCrud.kt` con tres diálogos modales (`DialogoEdificio`, `DialogoAmbiente`, `DialogoNota`) que validan los campos de entrada antes de despachar las coroutines de inserción o actualización hacia el `EdificiosViewModel`.
  - *Botón de acción flotante (FAB):* Permite dar de alta nuevos edificios en cualquier momento.

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    figure(
      image("../img/report/captura04_dialogo_crud_actualizar.png", width: 62%),
      caption: [Diálogo modal interactivo para actualizar un edificio en tiempo de ejecución],
    ),
    figure(
      image("../img/report/captura05_persistencia_post_edicion.png", width: 62%),
      caption: [Resultado de la actualización persistida ("Pabellón EPIS - Renovado") reflejada reactivamente en la interfaz],
    )
  )

  *Comprobación de la persistencia real:*
  Tras editar el registro con identificador 1 a "Pabellón EPIS - Renovado" y guardar los cambios, la aplicación se cerró por completo (`adb shell am force-stop com.example.persistenciaroom_compose`) y se volvió a iniciar en frío. La consulta relacional cargó de inmediato los datos modificados directamente desde el archivo `app.db`, demostrando que los cambios son persistentes y no residen únicamente en la memoria volátil de la aplicación.
]
