package Atm.Task;

import Atm.PO.ReservationsPage;
import Atm.PO.StartPage;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isNotVisible;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class CancelReservation {

    private CancelReservation() {
    }

    public static Performable withCode(String code) {
        return Task.where("{0} cancels the reservation " + code,
                Click.on(StartPage.MY_RESERVATIONS_MENU),
                Click.on(ReservationsPage.CANCEL_BUTTON.of(code)),
                Click.on(ReservationsPage.CONFIRM_CANCELLATION),
                WaitUntil.the(ReservationsPage.MESSAGE, isVisible()).forNoMoreThan(10).seconds()
        );
    }

    /** Opens the confirmation dialog and closes it with "No, volver". */
    public static Performable startingAndGoingBack(String code) {
        return Task.where("{0} starts cancelling the reservation " + code + " but goes back",
                Click.on(StartPage.MY_RESERVATIONS_MENU),
                Click.on(ReservationsPage.CANCEL_BUTTON.of(code)),
                WaitUntil.the(ReservationsPage.DIALOG, isVisible()).forNoMoreThan(5).seconds(),
                Click.on(ReservationsPage.GO_BACK),
                WaitUntil.the(ReservationsPage.DIALOG, isNotVisible()).forNoMoreThan(5).seconds()
        );
    }
}
