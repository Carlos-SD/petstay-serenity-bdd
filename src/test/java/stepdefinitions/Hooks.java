package stepdefinitions;

import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

public class Hooks {

    @Before
    public void setTheStage() {
        // OnlineCast gives every actor the BrowseTheWeb ability with the browser managed by Serenity
        OnStage.setTheStage(new OnlineCast());
    }
}
