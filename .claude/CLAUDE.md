# PetStay — Serenity BDD workshop (ICESI, Pruebas IA)

Front-end test automation workshop with Serenity BDD + Cucumber + Screenplay. It is graded with an oral defense,
so the student must understand every piece: act as a tutor, not just as a code generator.

## How to work in this project

These two documents are the rules for every session. Follow them:

- Tutor role, limits and workflow: @docs/instrucciones-ia-tutor-serenity.md
- Technical reference (structure, tasks, questions, assertions, build): @docs/guia-serenity-bdd.md

## Student preferences (override the defaults of the documents above)

- Talk to the student in Spanish.
- **Everything in the project is written in English: Java code AND Gherkin** (`Feature`, `Scenario Outline`,
  `Given/When/Then`, `Examples`). Do not add `# language: es`.
- Feature style used in class: `Scenario Outline` + `Examples` table.
- Commits and pushes go without a `Co-Authored-By` line.

## Project context

- App under test: PetStay (pet hotel) — https://petstay-aphn.onrender.com/
  - Static HTML + JS with no backend: sessions, pets and reservations live in the browser's `localStorage`
    (`petstay_sesion`, `petstay_mascotas`, `petstay_reservas`); loading layers simulate server latency.
  - Practice user: `dueno` / `huellitas123`. After login the app redirects to `mascotas.html`.
  - Hosted on Render: the first load after idle can take ~1 minute.
- Requirements: the teacher's single user story **HU-02 · Hospedaje de mascotas** (access + pets + booking +
  cancellation). Scenarios are tagged `@HU-02` (feature) and `@REQ-<ACC|PET|RES|CAN>-<n>` (acceptance criterion).
- Build: Gradle (Groovy DSL) with wrapper · Java 17 · Serenity 4.2.32.
- Runner, `serenity.conf`, `build.gradle`, `settings.gradle` and `gradle.properties` come from the teacher's
  class files. The runner is `runners.Runner` (`@RunWith(CucumberWithSerenity.class)`, glue `stepdefinitions`):
  keep those names. `build.gradle` adds `junit-vintage-engine` so the JUnit 4 runner runs on the JUnit Platform.
- Run: `./gradlew clean test` (headless by default; `-Dheadless.mode=false` to watch the browser;
  `-Dcucumber.filter.tags="@Issue-N"` for one case). Report: `target/site/serenity/index.html`.

## Workshop requirements (teacher's PDF)

- At least 10 scenarios, positive and negative, each linked to its own GitHub issue.
- Code in a GitHub repo, tests running in GitHub Actions.
- Traceability: tags on each scenario for the requirement and the issue number.

## Current state (2026-10-15)

All 18 scenarios are implemented and running (the student asked Claude to write the Gherkin and the code):
`features/{login,pets,booking,cancellation}`. Local and CI runs: 16 pass, the 2 `@defect` ones fail on purpose.

- Repo: https://github.com/Carlos-SD/petstay-serenity-bdd (public; remote `git@personal:...`). For `gh` use
  `GH_TOKEN=$(gh auth token -u Carlos-SD)`: the active `gh` account on this machine is a work account.
- Issues #1–#18 = one per scenario, tagged `@Issue-N`; Project https://github.com/users/Carlos-SD/projects/2.
  Labels: `status:passed` / `status:failed` (set by CI), `HU-02`, `positive`, `negative`, `defect`, `smoketest`.
- `.github/workflows/ci.yml` is the Karate workflow adapted to Serenity (`-Dcucumber.filter.tags`, Serenity report
  artifacts, Java 17). It extracts the issue with the regex `Issue-([0-9]+)`, so tags must be `@Issue-N`.
  Push runs `@smoketest` (#1, #8); a manual run with `testTag=@Issue-N` updates issue N. Tag expressions must
  start with `@` (e.g. `@HU-02 and not @defect`), because the workflow prefixes `@` to anything else.
- `@Issue-17` / `@Issue-18` (`@defect`) document real app bugs: the 15% discount is applied at exactly 7 nights
  (`noches >= 7`; HU says *more than* 7) and the daily walk is charged once instead of per night.
- `Loggued` clears `localStorage` before logging in: `OnlineCast` reuses the actor's browser, so app data leaked
  between scenarios. Targets with `[attr='{0}']` need the `css:` prefix or Serenity treats them as XPath.
- Dates in scenarios are "days from today" (`Atm.Utils.StayDates`) so they never expire.
- Not covered by a dedicated scenario yet: F2.1 (only registered pets), F2.2 (invalid date format),
  F3.1 (reservation list columns).

## Known gaps against the guide (pending the student's decision)

The code follows what was shown in class, which differs from `guia-serenity-bdd.md` in several points.
Point them out as suggestions; do not change them without approval:

- Automation code lives in `src/main/java/Atm/...` (packages `Atm.PO`, `Atm.Task`, `Atm.Questions`, `Atm.Utils`),
  as in the teacher's project; the guide puts it in `src/test` with lowercase packages (`ui`, `tasks`, `questions`).
- `Loggued` is a Task class with `instrumented()` and a `WaitUntil` before each field; the newer tasks use
  `Task.where`. The 60s waits exist because of Render's cold start.
- Runner uses JUnit 4 (`@RunWith(CucumberWithSerenity.class)`); the guide recommends a JUnit 5 `@Suite`.
- Assertions use `should(seeThat(...))` + Hamcrest (no `serenity-ensure` dependency in the teacher's build).
- `take.screenshots = FOR_EACH_ACTION` (guide: `FOR_FAILURES`, use FOR_EACH_ACTION only to debug).
