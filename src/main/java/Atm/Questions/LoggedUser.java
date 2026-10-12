package Atm.Questions;

import Atm.PO.StartPage;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

public class LoggedUser {

    private LoggedUser() {
    }

    public static Question<String> name() {
        return Text.of(StartPage.GREETING).describedAs("the logged user name");
    }
}
