package Atm.Task;

import Atm.PO.PetsPage;
import Atm.Utils.Routes;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Open;

/** Opens the booking form with the pet already selected, using the "Reservar" link of the pets list. */
public class StartBooking {

    private StartBooking() {
    }

    public static Performable forPet(String pet) {
        return Task.where("{0} starts a booking for " + pet,
                Open.url(Routes.PETSTAY_URL + Routes.PETS_PAGE),
                Click.on(PetsPage.BOOK_LINK.of(pet))
        );
    }
}
