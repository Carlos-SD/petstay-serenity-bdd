package Atm.PO;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class LoginPage {

    public static final Target PAGE_HEADING = Target.the("PetStay page heading").located(By.cssSelector("h1"));
    public static final Target USERNAME = Target.the("username field").located(By.name("usuario"));
    public static final Target PASSWORD = Target.the("password field").located(By.id("pass-01"));
    public static final Target LOGIN_BUTTON = Target.the("login button").located(By.cssSelector("[data-testid='btn-ingresar']"));
    public static final Target LOGIN_ERROR = Target.the("login error message").located(By.id("error-login"));
}
