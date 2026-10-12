package Atm.PO;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

import java.util.Map;

/** reservar.html: stay form, additional services and summary. */
public class BookingPage {

    public static final Target ENTRY_DATE = Target.the("entry date field").located(By.id("fecha-entrada"));
    public static final Target EXIT_DATE = Target.the("exit date field").located(By.cssSelector("[data-testid='fecha-salida']"));
    public static final Target BATH = Target.the("bath and grooming service").located(By.id("srv-bano"));
    public static final Target DAILY_WALK = Target.the("daily walk service").located(By.name("paseo"));
    public static final Target WALK_NOTE = Target.the("daily walk note").located(By.cssSelector(".nota-paseo"));
    public static final Target CALCULATE_BUTTON = Target.the("calculate total button").located(By.id("btn-calcular"));

    public static final Target MESSAGE = Target.the("booking message").located(By.cssSelector("[data-testid='mensaje-reserva']"));
    public static final Target FIELD_ERROR = Target.the("error of the {0} date field").locatedBy(".error-campo[data-campo='{0}']");

    /** The form identifies each date error by its Spanish name (data-campo). */
    public static Target dateErrorOf(String field) {
        return FIELD_ERROR.of(Map.of("entry", "entrada", "exit", "salida").get(field));
    }

    public static final Target SUMMARY = Target.the("booking summary").located(By.cssSelector(".panel-resumen"));
    public static final Target NIGHTS = Target.the("summary nights").located(By.cssSelector("[data-testid='res-noches']"));
    public static final Target LODGING = Target.the("summary lodging").located(By.id("res-alojamiento"));
    public static final Target DISCOUNT_ROW = Target.the("summary discount row").located(By.cssSelector(".panel-resumen .fila-descuento"));
    public static final Target DISCOUNT = Target.the("summary discount").located(By.cssSelector("[data-testid='res-descuento']"));
    public static final Target BATH_PRICE = Target.the("summary bath price").located(By.cssSelector(".panel-resumen .res-bano"));
    public static final Target WALK_PRICE = Target.the("summary daily walk price").located(By.id("res-paseo"));
    public static final Target TOTAL = Target.the("summary total").located(By.cssSelector("[data-testid='res-total']"));
    public static final Target CONFIRM_BUTTON = Target.the("confirm booking button").located(By.cssSelector(".panel-resumen .btn-primario"));

    public static final Target RESERVATION_CODE = Target.the("reservation code").located(By.cssSelector("[data-testid='codigo-reserva']"));
}
