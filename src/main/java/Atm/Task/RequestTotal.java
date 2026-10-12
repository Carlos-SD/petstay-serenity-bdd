package Atm.Task;

import Atm.PO.BookingPage;
import Atm.Utils.StayDates;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.conditions.Check;

/** Fills the stay form and asks for the summary, without confirming the booking. */
public class RequestTotal {

    private RequestTotal() {
    }

    public static Performable forStay(String pet, int entryInDays, int exitInDays) {
        return forStay(pet, entryInDays, exitInDays, false, false);
    }

    public static Performable forStay(String pet, int entryInDays, int exitInDays, boolean bath, boolean dailyWalk) {
        String entry = StayDates.daysFromToday(entryInDays);
        String exit = StayDates.daysFromToday(exitInDays);
        return Task.where("{0} requests the total of a stay for " + pet + " from " + entry + " to " + exit,
                StartBooking.forPet(pet),
                Enter.theValue(entry).into(BookingPage.ENTRY_DATE),
                Enter.theValue(exit).into(BookingPage.EXIT_DATE),
                Check.whether(bath).andIfSo(Click.on(BookingPage.BATH)),
                Check.whether(dailyWalk).andIfSo(Click.on(BookingPage.DAILY_WALK)),
                Click.on(BookingPage.CALCULATE_BUTTON)
        );
    }
}
