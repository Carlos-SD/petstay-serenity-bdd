package Atm.Task;

import Atm.PO.BookingPage;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/** Requests the total, confirms it and remembers the generated code for the next steps. */
public class BookStay {

    public static final String RESERVATION_CODE = "reservationCode";

    private BookStay() {
    }

    public static Performable forPet(String pet, int entryInDays, int exitInDays) {
        return Task.where("{0} books a stay for " + pet,
                actor -> {
                    actor.attemptsTo(
                            RequestTotal.forStay(pet, entryInDays, exitInDays),
                            Click.on(BookingPage.CONFIRM_BUTTON),
                            // The app simulates the server with a loading layer of up to 1.5 s
                            WaitUntil.the(BookingPage.RESERVATION_CODE, isVisible()).forNoMoreThan(10).seconds()
                    );
                    actor.remember(RESERVATION_CODE, Text.of(BookingPage.RESERVATION_CODE).answeredBy(actor));
                });
    }
}
