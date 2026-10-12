package Atm.Task;

import Atm.PO.StartPage;
import Atm.Utils.Routes;
import Atm.Utils.TestData;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/** Precondition shared by every feature after login: the owner is inside PetStay. */
public class OpenSession {

    private OpenSession() {
    }

    public static Performable asTheOwner() {
        return Task.where("{0} logs in to PetStay as the owner",
                Open.url(Routes.PETSTAY_URL),
                Loggued.withCredentials(TestData.USERNAME, TestData.PASSWORD),
                WaitUntil.the(StartPage.GREETING, isVisible()).forNoMoreThan(30).seconds()
        );
    }
}
