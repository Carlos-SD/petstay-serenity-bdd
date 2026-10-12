package stepdefinitions;

import Atm.PO.BookingPage;
import Atm.Questions.BookingSummary;
import Atm.Questions.MyReservations;
import Atm.Task.BookStay;
import Atm.Task.GoToMyReservations;
import Atm.Task.RequestTotal;
import Atm.Task.StartBooking;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.questions.Visibility;

import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

public class BookingStepDefinitions {

    @Given("I have booked a stay for {string} entering in {int} days and leaving in {int} days")
    @When("I book a stay for {string} entering in {int} days and leaving in {int} days")
    public void iBookAStay(String pet, int entry, int exit) {
        theActorInTheSpotlight().attemptsTo(BookStay.forPet(pet, entry, exit));
    }

    @When("I request the total of a stay for {string} entering in {int} days and leaving in {int} days")
    public void iRequestTheTotalOfAStay(String pet, int entry, int exit) {
        theActorInTheSpotlight().attemptsTo(RequestTotal.forStay(pet, entry, exit));
    }

    @When("I request the total of a stay for {string} entering in {int} days and leaving in {int} days, with bath and daily walk")
    public void iRequestTheTotalOfAStayWithServices(String pet, int entry, int exit) {
        theActorInTheSpotlight().attemptsTo(RequestTotal.forStay(pet, entry, exit, true, true));
    }

    @When("I start a booking for {string}")
    public void iStartABookingFor(String pet) {
        theActorInTheSpotlight().attemptsTo(StartBooking.forPet(pet));
    }

    @Then("I should receive a reservation code with the format PS-XXXX")
    public void iShouldReceiveAReservationCode() {
        String code = theActorInTheSpotlight().recall(BookStay.RESERVATION_CODE);
        theActorInTheSpotlight().should(
                seeThat("the reservation code " + code, actor -> code, matchesPattern("PS-\\d{4}"))
        );
    }

    @Then("my reservation for {string} should be listed with status {string} and total {string}")
    public void myReservationShouldBeListed(String pet, String status, String total) {
        String code = theActorInTheSpotlight().recall(BookStay.RESERVATION_CODE);
        theActorInTheSpotlight().attemptsTo(GoToMyReservations.page());
        theActorInTheSpotlight().should(
                eventually(seeThat(MyReservations.petOf(code), equalTo(pet))),
                seeThat(MyReservations.statusOf(code), equalTo(status)),
                seeThat(MyReservations.totalOf(code), equalTo(total))
        );
    }

    @Then("I should have {int} reservations with status {string}")
    public void iShouldHaveReservationsWithStatus(int reservations, String status) {
        theActorInTheSpotlight().attemptsTo(GoToMyReservations.page());
        theActorInTheSpotlight().should(
                eventually(seeThat(MyReservations.withStatus(status), equalTo((long) reservations)))
        );
    }

    @Then("the summary should show {int} nights, lodging {string}, discount {string} and total {string}")
    public void theSummaryShouldShowDiscount(int nights, String lodging, String discount, String total) {
        theActorInTheSpotlight().should(
                eventually(seeThat(BookingSummary.nights(), equalTo(String.valueOf(nights)))),
                seeThat(BookingSummary.lodging(), equalTo(lodging)),
                seeThat(BookingSummary.discount(), equalTo(discount)),
                seeThat(BookingSummary.total(), equalTo(total))
        );
    }

    @Then("the summary should show {int} nights, lodging {string}, no discount and total {string}")
    public void theSummaryShouldShowNoDiscount(int nights, String lodging, String total) {
        theActorInTheSpotlight().should(
                eventually(seeThat(BookingSummary.nights(), equalTo(String.valueOf(nights)))),
                seeThat(BookingSummary.lodging(), equalTo(lodging)),
                seeThat(BookingSummary.hasDiscount(), is(false)),
                seeThat(BookingSummary.total(), equalTo(total))
        );
    }

    @Then("the summary should show bath {string}, daily walk {string} and total {string}")
    public void theSummaryShouldShowServices(String bath, String dailyWalk, String total) {
        theActorInTheSpotlight().should(
                eventually(seeThat(BookingSummary.bath(), equalTo(bath))),
                seeThat(BookingSummary.dailyWalk(), equalTo(dailyWalk)),
                seeThat(BookingSummary.total(), equalTo(total))
        );
    }

    @Then("the daily walk should not be available")
    public void theDailyWalkShouldNotBeAvailable() {
        theActorInTheSpotlight().should(eventually(seeThat(BookingSummary.dailyWalkAvailable(), is(false))));
    }

    @Then("I should see the booking note {string}")
    public void iShouldSeeTheBookingNote(String note) {
        theActorInTheSpotlight().should(
                seeThat(Visibility.of(BookingPage.WALK_NOTE).describedAs("whether the walk note is shown"), is(true)),
                seeThat(Text.of(BookingPage.WALK_NOTE).describedAs("the walk note"), equalTo(note))
        );
    }

    @Then("I should see the error {string} in the {string} date field")
    public void iShouldSeeTheErrorInTheDateField(String error, String field) {
        theActorInTheSpotlight().should(
                eventually(seeThat(Text.of(BookingPage.dateErrorOf(field)).describedAs("the error of the " + field + " date"),
                        equalTo(error)))
        );
    }

    @Then("I should see the booking message {string}")
    public void iShouldSeeTheBookingMessage(String message) {
        theActorInTheSpotlight().should(
                eventually(seeThat(Text.of(BookingPage.MESSAGE).describedAs("the booking message"), equalTo(message)))
        );
    }

    @Then("no summary should be shown")
    public void noSummaryShouldBeShown() {
        theActorInTheSpotlight().should(seeThat(BookingSummary.isShown(), is(false)));
    }
}
