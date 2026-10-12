package Atm.Task;

import Atm.PO.StartPage;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;

public class GoToMyReservations {

    private GoToMyReservations() {
    }

    public static Performable page() {
        return Task.where("{0} opens My reservations", Click.on(StartPage.MY_RESERVATIONS_MENU));
    }
}
