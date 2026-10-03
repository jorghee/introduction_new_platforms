#import "../components/lab-section.typ": lab-section

#lab-section(title: "CONCLUSIONES")[
  
  - *Eficiencia en el paradigma declarativo:* Se evidenció que la adopción de Jetpack Compose y `Navigation Compose` agiliza notablemente el desarrollo de flujos multi-pantalla y sus correspondientes `NavigationBar`, prescindiendo completamente de la complejidad y sobrecarga de ciclo de vida que tenían los antiguos `Fragments`.
  
  - *Manejo maduro del estado:* Migrar de funciones lambda hacia una arquitectura con `SharedViewModel` permitió experimentar un patrón de diseño fundamental. El ViewModel actúa como orquestador del estado, centralizando la lógica de negocios y limpiando radicalmente la capa visual.
  
  - *Integración sin fricciones:* Al utilizar utilidades de navegación como `launchSingleTop` y `restoreState`, se logra proveer al usuario de una navegación predecible sin apilar vistas duplicadas, resultando en un control de pila robusto y un comportamiento a la par de aplicaciones de clase mundial.
]
