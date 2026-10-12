package Atm.PO;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

import java.util.Map;

/** mascotas.html: pet registration form and registered pets list. */
public class PetsPage {

    public static final Target NAME = Target.the("pet name field").located(By.id("nombreMascota"));
    // "css:" prefix is needed: Serenity's CSS/XPath auto-detection treats "tag[child='value']" as valid XPath
    // (a predicate on a child element), so without the prefix it silently resolves to the wrong locator type.
    public static final Target SPECIES_OPTION = Target.the("species option {0}").locatedBy("css:input[name='especie'][value='{0}']");
    public static final Target WEIGHT = Target.the("pet weight field").located(By.name("peso"));
    public static final Target AGE = Target.the("pet age field").located(By.cssSelector("[data-testid='edad']"));
    public static final Target VACCINES = Target.the("vaccines up to date checkbox").located(By.name("vacunas"));
    public static final Target REGISTER_BUTTON = Target.the("register pet button").located(By.cssSelector("#form-mascota button[type='submit']"));

    public static final Target MESSAGE = Target.the("pets message").located(By.cssSelector("[data-testid='mensaje-mascotas']"));
    public static final Target FIELD_ERROR = Target.the("error of the pet {0} field").locatedBy(".error-campo[data-campo='{0}']");

    public static final Target PET_ROWS = Target.the("registered pets").locatedBy("#tabla-mascotas tbody tr");
    public static final Target PET_CELL = Target.the("column {1} of pet {0}")
            .locatedBy("//table[@id='tabla-mascotas']//tr[td[@class='nombre' and normalize-space()='{0}']]/td[{1}]");
    public static final Target BOOK_LINK = Target.the("book link of pet {0}")
            .locatedBy("//table[@id='tabla-mascotas']//tr[td[@class='nombre' and normalize-space()='{0}']]//a[normalize-space()='Reservar']");

    /** The form identifies each field error by its Spanish name (data-campo). */
    public static Target errorOf(String field) {
        return FIELD_ERROR.of(Map.of("name", "nombre", "weight", "peso", "age", "edad").get(field));
    }

    /** Column positions of the pets table. */
    public static final String SPECIES_COLUMN = "2";
    public static final String WEIGHT_COLUMN = "3";
    public static final String AGE_COLUMN = "4";
    public static final String SIZE_COLUMN = "5";
}
