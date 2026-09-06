=== Ejercicio 5

*Plantea una idea sencilla de aplicación móvil que ayude a resolver una necesidad real, indicando: nombre de la app, problema que busca resolver, usuario al que está dirigida, funciones básicas y posible estructura de pantallas. Este ejercicio servirá como base para el trabajo de las siguientes unidades.*

#figure(image("../../img/lab/logo_agromarketlocal.png", width: 10%), caption: [Logo de AgroMarket Local])

*AgroMarket Local* es una aplicación móvil orientada a conectar de forma directa a pequeños agricultores con consumidores de su misma localidad. El problema principal que busca solucionar es la dificultad que tienen los productores para comercializar alimentos frescos a precios justos debido a la alta dependencia de intermediarios comerciales. De manera análoga, resuelve la necesidad de los consumidores locales de adquirir alimentos orgánicos y con garantía de origen directamente del productor.

El sistema identifica y opera bajo dos perfiles de usuario bien definidos:
- *Productores:* Agricultores de pequeña y mediana escala que requieren gestionar su oferta agrícola y maximizar sus utilidades.
- *Consumidores:* Individuos interesados en el consumo responsable y directo de productos locales de origen rastreable.

*Funciones básicas del sistema:*
Para satisfacer estas necesidades, el aplicativo divide sus funciones principales en módulos específicos detallados a continuación:

#table(
  columns: (25%, 75%),
  align: (center, left),
  [*Módulo*], [*Descripción de la función*],
  [Identidad], [Registro y autenticación de usuarios según su rol (productor o consumidor).],
  [Catálogo y Trazabilidad], [Exhibición del catálogo de productos con evidencia fotográfica, información de precios y geolocalización de la zona de cosecha.],
  [Gestión de Pedidos], [Motor transaccional que agrupa la selección de productos y procesa la reserva directa con el agricultor.],
  [Comunicación Logística], [Servicio de mensajería (chat) en tiempo real para pactar los detalles de entrega y resolver incidencias.]
)

*Matriz de Requerimientos y Trazabilidad:*
Para asegurar el correcto desarrollo y trazabilidad de la solución, los requerimientos funcionales se han estructurado vinculándolos con su respectivo módulo y actor principal.

#table(
  columns: (auto, auto, auto, 1fr),
  align: (center, center, center, left),
  [*ID Req.*], [*Módulo*], [*Actor*], [*Descripción del Requerimiento*],
  [RF-01], [Identidad], [Ambos], [El sistema debe permitir el registro y autenticación diferenciada por tipo de usuario.],
  [RF-02], [Catálogo], [Productor], [El productor debe poder publicar productos detallando cantidad, precio y origen geográfico.],
  [RF-03], [Catálogo], [Consumidor], [El consumidor debe poder explorar y visualizar los productos disponibles priorizando la cercanía.],
  [RF-04], [Gestión de Pedidos], [Consumidor], [El consumidor debe poder añadir productos a su carrito y generar una solicitud de reserva.],
  [RF-05], [Gestión de Pedidos], [Productor], [El productor debe ser capaz de gestionar, aceptar o rechazar las reservas entrantes.],
  [RF-06], [Comunicación Logística], [Ambos], [El sistema debe habilitar un chat directo entre ambas partes una vez confirmada la reserva.],
  [RF-07], [Gestión de Perfil], [Ambos], [Los usuarios deben poder visualizar el historial de sus transacciones o ventas recientes.]
)

*Arquitectura de Interfaces de Usuario:*
A continuación, se detalla la propuesta visual y el flujo de navegación de las pantallas diseñadas para la solución.

1. *Autenticación y Registro:* Punto de entrada al sistema. Proporciona formularios para la creación de cuenta o inicio de sesión validado.
#figure(image("../../img/lab/inicio_registro_usuarios.png", width: 35%), caption: [Pantalla de inicio y registro de usuarios])

2. *Feed Principal de Cosechas:* Actúa como pantalla "Home". Despliega el listado de productos disponibles dando prioridad a los agricultores más cercanos geográficamente.
#figure(image("../../img/lab/pantalla_principal_feed_cosechas.png", width: 30%), caption: [Feed de cosechas y agricultores locales cercanos])

3. *Detalle del Producto:* Interfaz que muestra el desglose del producto seleccionado (peso, cantidad, origen) e incluye los controles para añadir unidades al carrito.
#figure(image("../../img/lab/detalle_producto_trazabilidad.png", width: 35%), caption: [Vista detallada del producto y trazabilidad agrícola])

4. *Reservas y Transacciones:* Pantalla de validación de compra (Carrito). Genera el resumen económico del pedido para la confirmación de reserva.
#figure(image("../../img/lab/carrito_reservas_directas.png", width: 28%), caption: [Interfaz de carrito de compras y reservas directas])

5. *Módulo de Chat Integrado:* Bandeja de mensajería interna orientada exclusivamente a la coordinación logística entre ambas partes involucradas en el pedido.
#figure(image("../../img/lab/chat_coordinacion_productor.png", width: 35%), caption: [Bandeja de mensajes para la coordinación logística])

6. *Gestión de Perfil de Usuario:* Interfaz administrativa individual donde los productores gestionan los datos de su huerto y los consumidores revisan su historial.
#figure(image("../../img/lab/perfil_usuario_gestion_huerto.png", width: 35%), caption: [Pantalla de perfil de usuario y gestión del huerto])

#figure(image("../../img/lab/design.png", width: 85%), caption: [Consolidado del diseño y los componentes visuales de la aplicación])
