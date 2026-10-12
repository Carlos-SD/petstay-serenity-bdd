package Atm.Questions;

import Atm.PO.BookingPage;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.questions.Visibility;
import net.serenitybdd.screenplay.questions.WebElementQuestion;

public class BookingSummary {

    private BookingSummary() {
    }

    public static Question<String> nights() {
        return Text.of(BookingPage.NIGHTS).describedAs("the nights of the stay");
    }

    public static Question<String> lodging() {
        return Text.of(BookingPage.LODGING).describedAs("the lodging value");
    }

    public static Question<String> discount() {
        return Text.of(BookingPage.DISCOUNT).describedAs("the long-stay discount");
    }

    public static Question<Boolean> hasDiscount() {
        return Visibility.of(BookingPage.DISCOUNT_ROW).describedAs("whether the summary shows a discount");
    }

    public static Question<String> bath() {
        return Text.of(BookingPage.BATH_PRICE).describedAs("the bath and grooming value");
    }

    public static Question<String> dailyWalk() {
        return Text.of(BookingPage.WALK_PRICE).describedAs("the daily walk value");
    }

    public static Question<String> total() {
        return Text.of(BookingPage.TOTAL).describedAs("the total of the stay");
    }

    public static Question<Boolean> isShown() {
        return Visibility.of(BookingPage.SUMMARY).describedAs("whether the summary is shown");
    }

    public static Question<Boolean> dailyWalkAvailable() {
        return Question.about("whether the daily walk can be selected").answeredBy(actor ->
                WebElementQuestion.the(BookingPage.DAILY_WALK).answeredBy(actor).isEnabled());
    }
}
