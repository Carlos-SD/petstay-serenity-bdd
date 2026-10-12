package Atm.Task;

import Atm.PO.PetsPage;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.conditions.Check;

public class RegisterPet {

    private RegisterPet() {
    }

    public static Performable withData(String name, String species, String weight, String age, boolean vaccinated) {
        return Task.where("{0} registers the pet " + name,
                Enter.theValue(name).into(PetsPage.NAME),
                Click.on(PetsPage.SPECIES_OPTION.of(species)),
                Enter.theValue(weight).into(PetsPage.WEIGHT),
                Enter.theValue(age).into(PetsPage.AGE),
                Check.whether(vaccinated).andIfSo(Click.on(PetsPage.VACCINES)),
                Click.on(PetsPage.REGISTER_BUTTON)
        );
    }
}
