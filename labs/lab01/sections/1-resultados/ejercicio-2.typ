=== Ejercicio 2

*Elabora un cuadro comparativo entre aplicación móvil, aplicación web y aplicación de escritorio, considerando al menos: dispositivo donde se ejecuta, conexión a internet, instalación, acceso a recursos del equipo y ejemplos.*

#figure(
  table(
    columns: (1.5fr, 2fr, 2fr, 2fr),
    align: center + horizon,
    fill: (_, row) => if row == 0 { luma(230) } else { white },
    [*Característica*], [*Aplicación Móvil*], [*Aplicación Web*], [*Aplicación de Escritorio*],
    [*Dispositivo*], [Teléfonos inteligentes, tabletas, wearables.], [Cualquier dispositivo con navegador web (PC, móvil, tablet).], [Computadoras de escritorio o laptops (PC, Mac).],
    [*Conexión a internet*], [Puede funcionar sin conexión (dependiendo de la app).], [Requiere conexión a internet (salvo algunas PWA).], [Generalmente no requiere conexión para sus funciones básicas.],
    [*Instalación*], [Requiere instalación desde una tienda de aplicaciones (Play Store, App Store).], [No requiere instalación, se accede mediante una URL.], [Requiere instalación y configuración en el sistema operativo local.],
    [*Acceso a recursos*], [Acceso total o parcial (cámara, GPS, sensores, contactos).], [Acceso muy limitado por motivos de seguridad del navegador.], [Acceso total al hardware y recursos del sistema (CPU, memoria, disco).],
    [*Ejemplos*], [WhatsApp, Instagram, Spotify (app móvil).], [Google Docs, Netflix (versión navegador), Canva.], [Microsoft Word, Adobe Photoshop, AutoCAD.]
  ),
  caption: [Cuadro comparativo de tipos de aplicaciones.]
)
