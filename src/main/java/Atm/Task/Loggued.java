package Atm.Task;

import Atm.PO.LoginPage;
import Atm.Utils.Routes;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Evaluate;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.conditions.Check;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.questions.WebElementQuestion;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class Loggued implements Task {

    private final String username;
    private final String password;

    public Loggued(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                // OnlineCast keeps one browser per actor name, so the session from a previous scenario
                // can still be in localStorage here even though serenity.conf restarts the browser.
                // Clearing storage and reloading guarantees the login form is actually shown.
                Evaluate.javascript("try { localStorage.clear(); sessionStorage.clear(); } catch (e) {}"),
                Open.url(Routes.PETSTAY_URL),
                // Render puts the app to sleep when idle, so the first load can take almost a minute
                WaitUntil.the(LoginPage.USERNAME, isVisible()).forNoMoreThan(60).seconds(),
                WaitUntil.the(LoginPage.PASSWORD, isVisible()).forNoMoreThan(60).seconds(),
                Enter.theValue(username).into(LoginPage.USERNAME),
                Enter.theValue(password).into(LoginPage.PASSWORD),
                Check.whether(WebElementQuestion.stateOf(LoginPage.LOGIN_BUTTON),
                        WebElementStateMatchers.isVisible()).andIfSo(Click.on(LoginPage.LOGIN_BUTTON))
        );
        System.out.println(" login performed with user '" + username + "' ");
    }

    public static Loggued withCredentials(String username, String password) {
        return instrumented(Loggued.class, username, password);
    }
}
