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
            errorHandling.reportError(pair, new ParsingException("Single choice question '" + text + "' cannot define matching pair '" + 
                    pair.getLeft() + "' <-> '" + pair.getRight() + "'! Remove it, or change the question type to one that supports pairs."));
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
                errorHandling.reportError(a, new ParsingException("Single choice question '" + text + "' cannot have multiple correct answers, remove '" +
                        a.getText() + "'."));
                correct = false;
            }
        }
        
        if (numberOfCorrect == 0 || numberOfIncorrect == 0) {
            errorHandling.reportError(this, new ParsingException("Question '" + text + "' has to have at least one correct and one incorrect answer."));
            correct = false;
        }
        
        boolean superCorrect = super.validate(errorHandling);
        return correct && superCorrect;
    }
}
