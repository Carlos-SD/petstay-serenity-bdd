package stepdefinitions;

import Atm.Questions.MyReservations;
import Atm.Task.BookStay;
import Atm.Task.CancelReservation;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CancellationStepDefinitions {

    private String rememberedCode() {
        return theActorInTheSpotlight().recall(BookStay.RESERVATION_CODE);
    }

    @When("I cancel that reservation")
    public void iCancelThatReservation() {
        theActorInTheSpotlight().attemptsTo(CancelReservation.withCode(rememberedCode()));
    }

    @When("I start cancelling that reservation but go back")
    public void iStartCancellingThatReservationButGoBack() {
        theActorInTheSpotlight().attemptsTo(CancelReservation.startingAndGoingBack(rememberedCode()));
    }

    @Then("that reservation should have status {string} and refund {string}")
    public void thatReservationShouldHaveStatusAndRefund(String status, String refund) {
        String code = rememberedCode();
        theActorInTheSpotlight().should(
                eventually(seeThat(MyReservations.statusOf(code), equalTo(status))),
                seeThat(MyReservations.refundOf(code), equalTo(refund))
        );
    }

    @Then("that reservation should still have status {string}")
    public void thatReservationShouldStillHaveStatus(String status) {
        String code = rememberedCode();
        theActorInTheSpotlight().should(
                seeThat(MyReservations.statusOf(code), equalTo(status)),
                seeThat(MyReservations.canBeCancelled(code), is(true))
        );
    }

    @Then("that reservation should not offer the option to cancel it again")
    public void thatReservationShouldNotOfferTheOptionToCancelItAgain() {
        String code = rememberedCode();
        theActorInTheSpotlight().should(
                eventually(seeThat(MyReservations.statusOf(code), equalTo("Cancelada"))),
                seeThat(MyReservations.canBeCancelled(code), is(false))
        );
    }
}
