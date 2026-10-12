package Atm.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Turns "days from today" into the DD/MM/AAAA format PetStay expects, so scenarios never get outdated. */
public final class StayDates {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private StayDates() {
    }

    public static String daysFromToday(int days) {
        return LocalDate.now().plusDays(days).format(FORMAT);
    }
}
