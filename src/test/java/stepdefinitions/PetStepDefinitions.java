package stepdefinitions;

import Atm.PO.PetsPage;
import Atm.Questions.PetsList;
import Atm.Task.RegisterPet;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;
import static org.hamcrest.Matchers.equalTo;

public class PetStepDefinitions {

    @Given("I have registered the pet {string}, a {string} of {string} kg and {string} years old")
    public void iHaveRegisteredThePet(String name, String species, String weight, String age) {
        theActorInTheSpotlight().attemptsTo(
                RegisterPet.withData(name, species, weight, age, true),
                // Registration shows a loading layer of up to 0.9 s before the pet appears in the list
                WaitUntil.the(PetsPage.PET_CELL.of(name, PetsPage.SIZE_COLUMN), isVisible()).forNoMoreThan(10).seconds()
        );
    }

    @When("I register the pet {string}, a {string} of {string} kg and {string} years old, with vaccines up to date")
    public void iRegisterThePetWithVaccines(String name, String species, String weight, String age) {
        theActorInTheSpotlight().attemptsTo(RegisterPet.withData(name, species, weight, age, true));
    }

    @When("I register the pet {string}, a {string} of {string} kg and {string} years old, without vaccines up to date")
    public void iRegisterThePetWithoutVaccines(String name, String species, String weight, String age) {
        theActorInTheSpotlight().attemptsTo(RegisterPet.withData(name, species, weight, age, false));
    }

    @Then("I should see the pets message {string}")
    public void iShouldSeeThePetsMessage(String message) {
        theActorInTheSpotlight().should(
                eventually(seeThat(Text.of(PetsPage.MESSAGE).describedAs("the pets message"), equalTo(message)))
        );
    }

    @Then("the pet {string} should be listed with species {string}, weight {string}, age {string} and size {string}")
    public void thePetShouldBeListedWith(String name, String species, String weight, String age, String size) {
        theActorInTheSpotlight().should(
                eventually(seeThat(PetsList.speciesOf(name), equalTo(species))),
                seeThat(PetsList.weightOf(name), equalTo(weight)),
                seeThat(PetsList.ageOf(name), equalTo(age)),
                seeThat(PetsList.sizeOf(name), equalTo(size))
        );
    }

    @Then("the pet {string} should be listed with size {string}")
    public void thePetShouldBeListedWithSize(String name, String size) {
        theActorInTheSpotlight().should(eventually(seeThat(PetsList.sizeOf(name), equalTo(size))));
    }

    @Then("I should see the error {string} in the pet {string} field")
    public void iShouldSeeTheErrorInThePetField(String error, String field) {
        theActorInTheSpotlight().should(
                eventually(seeThat(Text.of(PetsPage.errorOf(field)).describedAs("the error of the " + field + " field"),
                        equalTo(error)))
        );
    }

    @Then("my pets list should have {int} pet(s)")
    public void myPetsListShouldHave(int pets) {
        theActorInTheSpotlight().should(seeThat(PetsList.size(), equalTo(pets)));
    }
}
