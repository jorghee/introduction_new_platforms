#import "lib.typ": unsa-report
#import "components/code-block.typ": code-block

#show: unsa-report.with(
  course_name: "Introducción al Desarrollo de Nuevas Plataformas",
  lab_title: "Persistencia de datos con Room Database en Jetpack Compose",
  lab_number: "09",
  instructor_name: "Roxana Evelyn Limache Calatayud",
  members: (
    "Jorge Luis Mamani Huarsaya",
    "Yordano Hernan Boza Portilla",
  ),
  presentation_date: "31/10/2026",
  sem_code: "VIII",
)

#set image(width: 78%)
#set list(indent: 2pt)
#show raw.where(block: false): it => box(inset: (x: 0.5pt))[#it]

#include "sections/1-resultados.typ"
#v(0.5em)
#include "sections/2-cuestionario.typ"
#pagebreak()
#include "sections/3-conclusiones.typ"
#v(0.5em)
#include "sections/4-referencias.typ"
