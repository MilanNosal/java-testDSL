package language.model;

import language.builder.ErrorHandlingUtils;
import language.builder.ParsingException;

/**
 * Question type with several options and a single correct answer.
 * @author Milan
 */
public class SingleOptionQuestion extends Question {
    
    public static final String NAME = "single-option-question";

    public SingleOptionQuestion(String text, int points) {
        this.text = text;
        this.points = points;
    }

    @Override
    public String toHTML(String id) {
        String options = answers.stream().map((answer) -> {
            return "                    <div class='radio'>\n"
+ "                        <label>\n"
+ "                            <input type='radio' name='" + id + "optionsRadios' correct='" + answer.isCorrect() + "'>\n"
+ "                            " + answer.getText() + "\n"
+ "                        </label>\n"
+ "                    </div>\n";
        }).reduce("", (a, b) -> a + b);
        
        String retVal = "    <div style='margin:25px 0 25px 0;'>\n"
+ "        <form class='form-horizontal questionHeader' role='form' id='" + id + "' points='" + points + "'>\n"
+ "            <div class='form-group'>\n"
+ "                <label class='control-label col-sm-3'>" + text + "</label>\n"
+ "\n"
+ "                <div class='col-sm-8'>\n"
+ options
+ "                </div>\n"
+ "\n"
+ "                <label class='control-label col-sm-1 points'>?/" + points + "</label>\n"
+ "            </div>\n"
+ "        </form>\n"
+ "    </div>\n";
        return retVal;
    }

    @Override
    public String toJS(String id) {
        return "testSingleChoice('#" + id + "')";
    }

    @Override
    public String toString() {
        return "SingleOptionQuestion{" + "text=" + text + ", points=" + points + ", answers=" + answers + '}';
    }

    @Override
    public boolean validate(ErrorHandlingUtils errorHandling) throws ParsingException {
        boolean correct = true;
        for (MatchingPair pair : pairs) {
            errorHandling.reportError(pair, new ParsingException("Otázka '" + text + "' s jednou správnou odpoveďou nemôže mať v sebe definovaný pár '" + 
                    pair.getLeft() + "' <-> '" + pair.getRight() + "'! Odstráň ho z definície, alebo zmeň typ otázky."));
            correct = false;
        }
        
        int numberOfCorrect = 0;
        int numberOfIncorrect = 0;
        for (Answer a : answers) {
            if (a.isCorrect()) {
                numberOfCorrect++;
            } else {
                numberOfIncorrect++;
            }
            if (a.isCorrect() && numberOfCorrect > 1) {
                errorHandling.reportError(a, new ParsingException("Otázka '" + text + "' s jednou odpoveďou nemôže mať ďalšiu správnu odpoveď, odstráň odpoveď '" +
                        a.getText() + "'."));
                correct = false;
            }
        }
        
        if (numberOfCorrect == 0 || numberOfIncorrect == 0) {
            errorHandling.reportError(this, new ParsingException("Otázka '" + text + "' musí mať aspoň jednu " 
                    + (numberOfCorrect == 0 ? "správnu" : "nesprávnu") + " odpoveď! Dodaj ju prosím."));
            correct = false;
        }
        
        boolean superCorrect = super.validate(errorHandling);
        return correct && superCorrect;
    }
}
