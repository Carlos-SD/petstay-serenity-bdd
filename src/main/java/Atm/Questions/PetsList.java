package Atm.Questions;

import Atm.PO.PetsPage;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.questions.Text;

import java.time.Duration;

public class PetsList {

    private PetsList() {
    }

    public static Question<String> speciesOf(String pet) {
        return Text.of(PetsPage.PET_CELL.of(pet, PetsPage.SPECIES_COLUMN)).describedAs("the species of " + pet);
    }

    public static Question<String> weightOf(String pet) {
        return Text.of(PetsPage.PET_CELL.of(pet, PetsPage.WEIGHT_COLUMN)).describedAs("the weight of " + pet);
    }

    public static Question<String> ageOf(String pet) {
        return Text.of(PetsPage.PET_CELL.of(pet, PetsPage.AGE_COLUMN)).describedAs("the age of " + pet);
    }

    public static Question<String> sizeOf(String pet) {
        return Text.of(PetsPage.PET_CELL.of(pet, PetsPage.SIZE_COLUMN)).describedAs("the size of " + pet);
    }

    /** Counts the rows without the implicit wait, so an empty list answers 0 right away. */
    public static Question<Integer> size() {
        return Question.about("the number of registered pets").answeredBy(actor ->
                BrowseTheWeb.as(actor).withTimeoutOf(Duration.ZERO)
                        .findAll(PetsPage.PET_ROWS.getCssOrXPathSelector()).size());
    }
}
