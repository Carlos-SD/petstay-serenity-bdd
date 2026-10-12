package Atm.PO;

import net.serenitybdd.screenplay.targets.Target;

/** reservas.html: list of reservations and cancellation dialog. */
public class ReservationsPage {

    private static final String ROW = "//table[@data-testid='tabla-reservas']//tr[td[@class='codigo' and normalize-space()='{0}']]";

    public static final Target PET = Target.the("pet of reservation {0}").locatedBy(ROW + "/td[2]");
    public static final Target TOTAL = Target.the("total of reservation {0}").locatedBy(ROW + "/td[@class='total']");
    public static final Target STATUS = Target.the("status of reservation {0}").locatedBy(ROW + "//span[contains(@class,'estado')]");
    public static final Target REFUND = Target.the("refund of reservation {0}").locatedBy(ROW + "/td[@class='reembolso']");
    // "css:" prefix is needed: Serenity's CSS/XPath auto-detection treats "tag[attr='value']" as valid XPath
    // (a predicate on a child element), so without the prefix it silently resolves to the wrong locator type.
    public static final Target CANCEL_BUTTON = Target.the("cancel button of reservation {0}").locatedBy("css:button.btn-cancelar[data-codigo='{0}']");
    public static final Target STATUSES = Target.the("reservation statuses").locatedBy("[data-testid='tabla-reservas'] tbody .estado");

    public static final Target CONFIRM_CANCELLATION = Target.the("confirm cancellation button").locatedBy("#confirmar-cancelacion");
    public static final Target GO_BACK = Target.the("go back button").locatedBy(".modal [data-accion='volver']");
    public static final Target DIALOG = Target.the("cancellation dialog").locatedBy(".fondo-modal");
    public static final Target MESSAGE = Target.the("reservations message").locatedBy("#mensaje-reservas");
}
