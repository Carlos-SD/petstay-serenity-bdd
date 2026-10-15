# Guía técnica de Serenity BDD con Screenplay

Guía de referencia para automatizar pruebas frontend con **Serenity BDD**, **Cucumber** y el **patrón Screenplay** en Java, con **Maven o Gradle**. Prioriza el **estilo fluido** recomendado por la documentación oficial: menos clases, menos código repetitivo y menos lugares donde equivocarse.

> Documentación oficial: https://serenity-bdd.github.io/docs/screenplay/screenplay_fundamentals

---

## 1. Conceptos clave

| Concepto | Qué es | Ejemplo |
|---|---|---|
| **Actor** | Representa al usuario que interactúa con el sistema | `Toby`, `Admin` |
| **Ability** | Capacidad técnica del actor | `BrowseTheWeb` (navegar con WebDriver) |
| **Interaction** | Acción atómica sobre la UI | `Click.on(...)`, `Enter.theValue(...)` |
| **Task** | Agrupa interacciones para cumplir una meta de negocio | `Login.withCredentials(...)` |
| **Question** | Consulta el estado del sistema | `Text.of(...)`, `Visibility.of(...)` |
| **Target** | Localizador de un elemento con nombre legible | `LoginPage.USERNAME` |

Regla general: **el actor usa sus habilidades para ejecutar tareas e interacciones y responde preguntas sobre el estado del sistema.**

Flujo en un proyecto con Cucumber:

```text
Feature (Gherkin) → Step Definition → Actor → Task / Question → Target
```

---

## 2. Estructura del proyecto

Todo el código de automatización es código de prueba: va en `src/test`.

```text
src/test/java/com/<organizacion>/<proyecto>/
├── runners/          → Suite de JUnit 5 que ejecuta Cucumber
├── stepdefinitions/  → Traducen cada paso Gherkin a llamadas del actor
├── hooks/            → Preparación de la escena (actores) y parámetros
├── tasks/            → Metas de negocio
├── interactions/     → Solo si Serenity no trae la acción que se necesita
├── questions/        → Consultas del estado de la aplicación
├── ui/               → Targets agrupados por página o componente
└── models/           → Datos de prueba (POJO o record)

src/test/resources/
├── features/         → Archivos .feature
└── serenity.conf     → Configuración de Serenity y WebDriver
```

Convenciones de nombres:

- Paquetes en minúscula (`tasks`, `ui`), nunca `Atm.PO` ni `Question`.
- Clases en `PascalCase` (`LoginPage`, `Login`).
- Métodos y variables en `camelCase` (`withCredentials`).
- Constantes y `Target` en `UPPER_SNAKE_CASE` (`LOGIN_BUTTON`).
- Nombres en inglés y expresivos del dominio: el código debe leerse como una frase. Por ejemplo: `actor.attemptsTo(Login.withCredentials(user, password))`.

---

## 3. Configuración del build

**JUnit 5 es el estándar.** JUnit 4 está marcado como obsoleto desde Serenity 5.0.0 y se eliminará en la 6.0.0. No se debe iniciar un proyecto nuevo con JUnit 4.

Todas las dependencias de Serenity deben usar **la misma versión**. Consulta la vigente en Maven Central (`net.serenity-bdd:serenity-core`) antes de fijarla.

### 3.1. Maven (`pom.xml`)

```xml
<properties>
    <java.version>17</java.version>
    <serenity.version>X.Y.Z</serenity.version>
</properties>

<dependencies>
    <dependency>
        <groupId>net.serenity-bdd</groupId>
        <artifactId>serenity-core</artifactId>
        <version>${serenity.version}</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>net.serenity-bdd</groupId>
        <artifactId>serenity-cucumber</artifactId>
        <version>${serenity.version}</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>net.serenity-bdd</groupId>
        <artifactId>serenity-screenplay</artifactId>
        <version>${serenity.version}</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>net.serenity-bdd</groupId>
        <artifactId>serenity-screenplay-webdriver</artifactId>
        <version>${serenity.version}</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>net.serenity-bdd</groupId>
        <artifactId>serenity-ensure</artifactId>
        <version>${serenity.version}</version>
        <scope>test</scope>
    </dependency>
    <!-- Motor JUnit Platform Suite (versión alineada con JUnit 5) -->
    <dependency>
        <groupId>org.junit.platform</groupId>
        <artifactId>junit-platform-suite</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Plugins necesarios:
- `maven-failsafe-plugin` (o `maven-surefire-plugin`), configurado para incluir la clase runner.
- `serenity-maven-plugin` con el goal `aggregate` en la fase `post-integration-test`, que genera el reporte.

### 3.2. Gradle (`build.gradle`)

```groovy
plugins {
    id 'java'
    id 'net.serenity-bdd.serenity-gradle-plugin' version 'X.Y.Z'
}

def serenityVersion = 'X.Y.Z'

dependencies {
    testImplementation "net.serenity-bdd:serenity-core:${serenityVersion}"
    testImplementation "net.serenity-bdd:serenity-cucumber:${serenityVersion}"
    testImplementation "net.serenity-bdd:serenity-screenplay:${serenityVersion}"
    testImplementation "net.serenity-bdd:serenity-screenplay-webdriver:${serenityVersion}"
    testImplementation "net.serenity-bdd:serenity-ensure:${serenityVersion}"
    testImplementation 'org.junit.platform:junit-platform-suite'
}

test {
    useJUnitPlatform()
    systemProperties System.getProperties()   // propaga -Denvironment, -Dcucumber.filter.tags, etc.
    finalizedBy 'aggregate'                   // genera el reporte al terminar
}
```

En Kotlin DSL (`build.gradle.kts`) la estructura es la misma. Cambia la sintaxis: `testImplementation("...")` y `tasks.test { ... }`.

### 3.3. Comandos equivalentes

| Acción | Maven | Gradle |
|---|---|---|
| Ejecutar y generar reporte | `./mvnw clean verify` | `./gradlew clean test aggregate` |
| Elegir entorno | `-Denvironment=dev` | `-Denvironment=dev` |
| Filtrar por etiqueta | `-Dcucumber.filter.tags="@smoke"` | `-Dcucumber.filter.tags="@smoke"` |
| Reporte | `target/site/serenity/index.html` | `target/site/serenity/index.html` |

Usa siempre el **wrapper** (`mvnw` / `gradlew`). En Windows: `mvnw.cmd` / `gradlew.bat`.

---

## 4. Configuración de Serenity (`serenity.conf`)

```hocon
serenity {
  project.name = "Automatización OrangeHRM"
  take.screenshots = FOR_FAILURES      # FOR_EACH_ACTION solo para depurar: vuelve lenta la suite
}

screenplay.pronouns = "él,ella,ellos,ellas,su,sus"

webdriver {
  driver = chrome
  capabilities {
    "goog:chromeOptions" {
      args = ["--headless=new", "--window-size=1920,1080", "--no-sandbox", "--disable-dev-shm-usage"]
    }
  }
}

environments {
  default { webdriver.base.url = "https://opensource-demo.orangehrmlive.com" }
  dev     { webdriver.base.url = "https://dev.mi-app.local" }
}
```

Selenium Manager descarga el driver del navegador automáticamente, así que no hace falta configurar rutas de `chromedriver`.

---

## 5. Runner (JUnit 5)

```java
package com.acme.orangehrm.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.acme.orangehrm")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
        value = "net.serenitybdd.cucumber.core.plugin.SerenityReporterParallel,pretty")
class CucumberTestSuite {
}
```

No uses `@RunWith(Cucumber.class)`: ejecuta Cucumber sin Serenity y no genera la Documentación Viva.

---

## 6. Escena, actores y parámetros (hooks)

El actor recibe el navegador desde la escena, **no dentro de un Step**:

```java
package com.acme.orangehrm.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

public class ScreenplayHooks {

    @Before
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());   // cada actor recibe su propio navegador
    }

    @ParameterType("[A-ZÁÉÍÓÚÑ][a-záéíóúñ]+|él|ella")
    public Actor actor(String actorName) {
        return OnStage.theActorCalled(actorName);   // reconoce nombres y pronombres
    }
}
```

Con esto, el Gherkin puede nombrar al actor ("Dado que Laura…") y el Step recibe directamente un `Actor`.

Con `OnlineCast`, Serenity gestiona el ciclo de vida del navegador. No hace falta `@Managed WebDriver` ni cerrar el driver a mano.

---

## 7. Localizadores (`ui/`)

```java
package com.acme.orangehrm.ui;

import net.serenitybdd.screenplay.targets.Target;

public class LoginPage {
    public static final Target USERNAME = Target.the("campo usuario").locatedBy("[name='username']");
    public static final Target PASSWORD = Target.the("campo contraseña").locatedBy("[name='password']");
    public static final Target LOGIN_BUTTON = Target.the("botón ingresar").locatedBy("button[type='submit']");
}
```

Buenas prácticas:
- Orden de preferencia: `id` → `name` / atributos `data-*` → CSS → XPath relativo. **Nunca XPath absoluto.**
- `Target.the("...")` define cómo se ve el elemento en el reporte: usa nombres que entienda el negocio.
- Localizadores dinámicos con parámetros: `Target.the("opción {0}").locatedBy("//li[normalize-space()='{0}']")` y luego `MENU_OPTION.of("Admin")`.
- En XPath, `and` / `or` van en minúscula, y `text()` no aplica a `<input>`: usa `@value` o `@placeholder`.
- Los selectores se declaran aquí, no dentro de las tareas. La documentación oficial los escribe en línea solo para simplificar los ejemplos.

---

## 8. Tareas (`tasks/`)

### 8.1. Estilo por defecto: `Task.where`

```java
package com.acme.orangehrm.tasks;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

import static com.acme.orangehrm.ui.LoginPage.*;

public class Login {

    public static Performable withCredentials(String user, String password) {
        return Task.where("{0} inicia sesión como " + user,
                Enter.theValue(user).into(USERNAME),
                Enter.theValue(password).into(PASSWORD),
                Click.on(LOGIN_BUTTON)
        );
    }
}
```

Ventajas: no usa campos (ni `static` ni de instancia), no requiere constructores ni `instrumented()` y se reporta correctamente. `{0}` se reemplaza por el nombre del actor.

### 8.2. Tareas con lógica: lambda

```java
public static Performable withEmployees(String... names) {
    return Task.where("{0} registra los empleados " + Arrays.asList(names),
            actor -> {
                for (String name : names) {
                    actor.attemptsTo(RegisterEmployee.named(name));
                }
            });
}
```

### 8.3. Tarea como clase (solo cuando se justifica)

Úsala cuando la tarea tenga muchos parámetros, un *builder* o lógica extensa. Dos requisitos:

1. Un **constructor vacío**, sin el cual la tarea no aparece bien en el reporte.
2. `@Step("{0} ...")` en `performAs`, importado desde `net.serenitybdd.annotations.Step`.

```java
public class RegisterEmployee implements Performable {
    private String name;

    public RegisterEmployee() {}
    public RegisterEmployee(String name) { this.name = name; }

    public static RegisterEmployee named(String name) { return new RegisterEmployee(name); }

    @Override
    @Step("{0} registra al empleado #name")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(/* interacciones */);
    }
}
```

### 8.4. Interacciones incluidas en Serenity

Antes de crear una interacción propia, revisa si ya existe: `Open.url(...)`, `Open.browserOn(...)`, `Click.on(...)`, `Enter.theValue(...).into(...)`, `Enter...thenHit(Keys.ENTER)`, `Clear.field(...)`, `SelectFromOptions.byVisibleText(...)`, `Scroll.to(...)`, `Hit.the(Keys...)`, `DoubleClick.on(...)`, `Upload...`, `Switch.toFrame(...)`.

### 8.5. Esperas

- Las interacciones sobre un `Target` **ya esperan** a que el elemento esté listo. No antepongas `WaitUntil` a cada `Enter` o `Click`.
- Usa `WaitUntil.the(TARGET, isVisible()).forNoMoreThan(10).seconds()` solo en casos puntuales: *spinners*, contenido que aparece tarde, transiciones.
- **Prohibido `Thread.sleep`.**

---

## 9. Preguntas (`questions/`)

### 9.1. Preguntas de fábrica (primera opción)

```java
String title    = actor.asksFor(Text.of(HomePage.TITLE));
boolean visible = actor.asksFor(Visibility.of(HomePage.DASHBOARD));
int count       = actor.asksFor(Text.of(CartPage.COUNTER).asInteger());
Collection<String> items = actor.asksFor(Text.ofEach(EmployeeList.NAMES));
```

Conversiones disponibles: `asInteger()`, `asDouble()`, `asBigDecimal()`, `asDate("d MMM uuuu")`, `asListOf(Integer.class)`, `mapEach(...)`.

### 9.2. Preguntas propias con `Question.about`

```java
public class TheEmployeeList {
    public static Question<Integer> size() {
        return Question.about("la cantidad de empleados listados")
                .answeredBy(actor -> Text.ofEach(EmployeeList.ROWS).answeredBy(actor).size());
    }
}
```

`Question.about` hace que el reporte y los mensajes de error digan qué se estaba consultando.

---

## 10. Aserciones

| Situación | Herramienta |
|---|---|
| Estado de elementos web (visible, habilitado, texto) | `Ensure` dentro de `attemptsTo` |
| Valor de negocio obtenido con una Question | `Ensure.that(question)` o `should(seeThat(...))` |
| Comparar objetos de dominio complejos | Matcher de Hamcrest (`TypeSafeMatcher`) o AssertJ |

```java
actor.attemptsTo(
    Ensure.that(HomePage.DASHBOARD).isDisplayed(),
    Ensure.that(HomePage.USER_ROLE).text().contains("Admin"),
    Ensure.that(TheEmployeeList.size()).isGreaterThan(0)
);
```

La **consulta** vive en la Question; la **expectativa** vive en el Step (o en la tarea de verificación).

---

## 11. Step Definitions

Los Steps solo traducen. No contienen selectores, esperas ni lógica de interfaz.

```java
public class LoginStepDefinitions {

    @Dado("que {actor} está en la página de inicio de sesión")
    public void isOnLoginPage(Actor actor) {
        actor.attemptsTo(Open.url("/web/index.php/auth/login"));
    }

    @Cuando("{actor} inicia sesión con el usuario {string} y la contraseña {string}")
    public void logsIn(Actor actor, String user, String password) {
        actor.attemptsTo(Login.withCredentials(user, password));
    }

    @Entonces("{actor} debería ver el panel principal")
    public void shouldSeeDashboard(Actor actor) {
        actor.attemptsTo(Ensure.that(HomePage.DASHBOARD).isDisplayed());
    }
}
```

- Para Gherkin en español, agrega `# language: es` al inicio del `.feature` e importa las anotaciones de `io.cucumber.java.es`.
- Si un paso no nombra al actor, usa `OnStage.theActorInTheSpotlight()`.
- Para compartir datos entre pasos usa `actor.remember("clave", valor)` / `actor.recall("clave")`, nunca variables `static`.

---

## 12. Documentación viva (reportes)

Serenity genera `target/site/serenity/index.html` con:
- Requisitos organizados por carpeta de `features`.
- Narrativa de cada escenario: lo que hizo cada actor, a partir de las descripciones de `Task.where`, `@Step` y `Target.the(...)`.
- Capturas según `serenity.take.screenshots`.
- Detalle de aserciones fallidas y errores.

Un reporte legible es un criterio de calidad: si el reporte no se entiende, las descripciones de tareas y *targets* deben mejorar.

---

## 13. Antipatrones frecuentes

| Antipatrón | Problema | Alternativa |
|---|---|---|
| Campos `static` en tareas | Datos compartidos entre escenarios | Parámetros de `Task.where` o campos de instancia |
| `WaitUntil` antes de cada acción | Ruido y lentitud | Esperas implícitas del `Target` |
| `Thread.sleep` | Pruebas lentas e inestables | `WaitUntil` puntual |
| Asignar el driver en un Step | Mezcla infraestructura con comportamiento | `OnlineCast` en `@Before` |
| Selectores dentro de tareas o Steps | Cambios de UI dispersos | Clases en `ui/` |
| Automatización en `src/main` | Mezcla código de prueba con producción | `src/test` |
| Gherkin con "doy clic en…" | Describe la interfaz, no el comportamiento | Describir la intención: "inicia sesión…" |
| XPath absoluto | Se rompe con cualquier cambio | CSS o XPath relativo |
| Una clase Question por cada verificación simple | Código innecesario | `Text.of`, `Visibility.of`, `Ensure` |
| `@RunWith(Cucumber.class)` o JUnit 4 en proyectos nuevos | Sin reporte Serenity / tecnología obsoleta | Suite JUnit 5 con `SerenityReporterParallel` |
