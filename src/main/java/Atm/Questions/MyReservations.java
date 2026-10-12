package Atm.Questions;

import Atm.PO.ReservationsPage;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.questions.Text;

import java.time.Duration;

public class MyReservations {

    private MyReservations() {
    }

    public static Question<String> petOf(String code) {
        return Text.of(ReservationsPage.PET.of(code)).describedAs("the pet of reservation " + code);
    }

    public static Question<String> totalOf(String code) {
        return Text.of(ReservationsPage.TOTAL.of(code)).describedAs("the total of reservation " + code);
    }

    public static Question<String> statusOf(String code) {
        return Text.of(ReservationsPage.STATUS.of(code)).describedAs("the status of reservation " + code);
    }

    public static Question<String> refundOf(String code) {
        return Text.of(ReservationsPage.REFUND.of(code)).describedAs("the refund of reservation " + code);
    }

    public static Question<Long> withStatus(String status) {
        return Question.about("the number of reservations with status " + status).answeredBy(actor ->
                Text.ofEach(ReservationsPage.STATUSES).answeredBy(actor).stream()
                        .filter(status::equals).count());
    }

    /** Looks for the button without the implicit wait: a cancelled reservation has none. */
    public static Question<Boolean> canBeCancelled(String code) {
        return Question.about("whether reservation " + code + " can be cancelled").answeredBy(actor ->
                !BrowseTheWeb.as(actor).withTimeoutOf(Duration.ZERO)
                        .findAll(ReservationsPage.CANCEL_BUTTON.of(code).getCssOrXPathSelector()).isEmpty());
    }
}
