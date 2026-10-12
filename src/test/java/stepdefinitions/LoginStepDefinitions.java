package stepdefinitions;

import Atm.PO.LoginPage;
import Atm.Questions.LoggedUser;
import Atm.Task.Loggued;
import Atm.Task.OpenSession;
import Atm.Utils.Routes;
import Atm.Utils.TestData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.questions.page.TheWebPage;

import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class LoginStepDefinitions {

    @Given("I am on the PetStay login page")
    public void iAmOnThePetStayLoginPage() {
        theActorCalled(TestData.OWNER).attemptsTo(Open.url(Routes.PETSTAY_URL));
    }

    @Given("I am logged in to PetStay")
    public void iAmLoggedInToPetStay() {
        theActorCalled(TestData.OWNER).attemptsTo(OpenSession.asTheOwner());
    }

    @When("I log in with the username {string} and the password {string}")
    public void iLogInWithTheUsernameAndThePassword(String username, String password) {
        theActorInTheSpotlight().attemptsTo(Loggued.withCredentials(username, password));
    }

    @Then("I should be logged in as {string}")
    public void iShouldBeLoggedInAs(String username) {
        theActorInTheSpotlight().should(
                eventually(seeThat(TheWebPage.currentUrl(), containsString(Routes.PETS_PAGE)))
                        .waitingForNoLongerThan(15).seconds(),
                eventually(seeThat(LoggedUser.name(), equalTo(username)))
        );
    }

    @Then("I should see the login error {string}")
    public void iShouldSeeTheLoginError(String error) {
        theActorInTheSpotlight().should(
                eventually(seeThat(Text.of(LoginPage.LOGIN_ERROR).describedAs("the login error"), equalTo(error))),
                seeThat(TheWebPage.currentUrl(), containsString(Routes.PETSTAY_URL))
        );
    }
}
