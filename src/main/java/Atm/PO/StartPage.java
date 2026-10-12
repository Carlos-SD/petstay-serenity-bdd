package Atm.PO;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/** Header shared by every page after login: greeting and main menu. */
public class StartPage {

    public static final Target GREETING = Target.the("logged user name").located(By.cssSelector("header.barra .saludo strong"));
    public static final Target MY_RESERVATIONS_MENU = Target.the("My reservations menu option").located(By.id("enlace-reservas"));
}
