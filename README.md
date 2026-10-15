# PetStay · Automatización front-end con Serenity BDD

Taller de **Pruebas IA (ICESI)**: pruebas automatizadas de [PetStay](https://petstay-aphn.onrender.com/) con
Serenity BDD + Cucumber + patrón Screenplay, ejecutadas en GitHub Actions y trazadas contra la historia de usuario
**HU-02 · Hospedaje de mascotas**.

## Stack
Java 17 · Gradle (wrapper) · Serenity BDD 4.2.32 · Cucumber · JUnit 4 runner (`runners.Runner`) · Chrome headless.

## Estructura
```text
src/main/java/Atm/
├── PO/         Targets (localizadores) separados por página: Login, Start (encabezado), Pets, Booking, Reservations
├── Task/       Metas de negocio: Loggued, OpenSession, RegisterPet, StartBooking, RequestTotal, BookStay, CancelReservation
├── Questions/  Consultas del estado: LoggedUser, PetsList, BookingSummary, MyReservations
└── Utils/      Rutas, datos del usuario de práctica y fechas relativas a hoy (StayDates)
src/test/java/
├── runners/          Runner (CucumberWithSerenity)
└── stepdefinitions/  Hooks (OnlineCast) + steps por funcionalidad
src/test/resources/
├── features/   login · pets · booking · cancellation
└── serenity.conf
```

## Trazabilidad
Cada escenario tiene:
- `@HU-02` (a nivel de feature) y `@REQ-<área>-<n>`: criterio de aceptación de la HU (ACC acceso, PET mascotas,
  RES reservas, CAN cancelación).
- `@Issue-N`: issue N del repositorio (caso de prueba), incluido en el
  [Project de casos de prueba](https://github.com/users/Carlos-SD/projects/2).
- `@positive` / `@negative`, `@smoketest` (corre en cada push) y `@defect` (expone un defecto real de la app).

| Issue | Escenario | Requisito |
|---|---|---|
| #1 | Login exitoso | REQ-ACC-01 |
| #2 | Login rechazado | REQ-ACC-02 |
| #3 | Registro de mascota válida | REQ-PET-01, REQ-PET-05 |
| #4 | Tamaño según el peso | REQ-PET-03 |
| #5 | Sin vacunas al día | REQ-PET-02 |
| #6 | Nombre duplicado | REQ-PET-04 |
| #7 | Datos fuera de rango | REQ-PET-01 |
| #8 | Reserva confirmada PS-XXXX | REQ-RES-08, REQ-RES-10 |
| #9 | Descuento > 7 noches | REQ-RES-05, REQ-RES-07 |
| #10 | Paseo no disponible para gatos | REQ-RES-06 |
| #11 | Fechas inválidas | REQ-RES-03, REQ-RES-04 |
| #12 | Reservas que se cruzan | REQ-RES-09 |
| #13 | Salida = entrada de otra reserva | REQ-RES-09 |
| #14 | Reembolso 100 / 50 / 0 % | REQ-CAN-03, REQ-CAN-04 |
| #15 | Confirmación antes de cancelar | REQ-CAN-02 |
| #16 | No se cancela dos veces | REQ-CAN-04 |
| #17 | **Defecto**: 7 noches reciben descuento | REQ-RES-07 |
| #18 | **Defecto**: paseo cobrado una sola vez | REQ-RES-06 |

Los escenarios #17 y #18 fallan a propósito: la app aplica el descuento con `noches >= 7` (la HU dice *más de 7*)
y cobra el paseo una sola vez (la HU dice *por noche*).

## Uso de IA
El proyecto se trabajó con Claude Code configurado como tutor. Las reglas están en `.claude/`:
`CLAUDE.md` (contexto y estado del proyecto), `docs/instrucciones-ia-tutor-serenity.md` (rol, límites y flujo
de trabajo) y `docs/guia-serenity-bdd.md` (referencia técnica).

## Ejecución
```bash
./gradlew clean test                                     # toda la suite (headless)
./gradlew clean test -Dcucumber.filter.tags="@Issue-3"   # un caso de prueba
./gradlew clean test -Dheadless.mode=false               # ver el navegador
```
Reporte: `target/site/serenity/index.html`.

**GitHub Actions** → *CI / CD Pipeline* → *Run workflow* → `testTag` (por ejemplo `@Issue-3`). Al terminar, el
issue correspondiente recibe el label `status:passed` / `status:failed` y un comentario con el enlace a la ejecución.
En cada push se ejecutan los escenarios `@smoketest`.

> PetStay está en Render: la primera carga después de un tiempo inactiva puede tardar cerca de un minuto.
