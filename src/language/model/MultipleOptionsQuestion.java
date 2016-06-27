package language.model;

import language.builder.ErrorHandlingUtils;
import language.builder.ParsingException;

/**
 * Question type with multiple correct answers.
 * @author Milan
 */
public class MultipleOptionsQuestion extends Question {
    
    public static final String NAME = "multiple-options-question";

    public MultipleOptionsQuestion(String text, int points) {
        this.text = text;
        this.points = points;
    }

    @Override
    public String toHTML(String id) {
        String options = answers.stream().map((answer) -> {
            return "                    <div class='checkbox'>\n"
+ "                        <label>\n"
+ "                            <input type='checkbox' correct='" + answer.isCorrect() + "'> " + answer.getText() + "\n"
+ "                        </label>\n" +
"                    </div>\n" +
"\n";
        }).reduce("", (a, b) -> a + b);
        
        String retVal = "    <div style='margin:25px 0 25px 0;'>\n"
+ "        <form class='form-horizontal questionHeader' role='form' id='" + id + "' points='" + points + "'>\n"
+ "            <div class='form-group'>\n"
+ "                <label class='control-label col-sm-3'>" + text + "</label>\n"
+ "\n"
+ "                <div class='col-sm-8'>\n"
+ options
+ "                    <small class='text-muted'>Watch out, if you select an incorrect option, the whole question will be considered incorrect.</small>\n"
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
        return "testMultipleChoice('#" + id + "')";
    }

    @Override
    public String toString() {
        return "MultipleOptionsQuestion{" + "text=" + text + ", points=" + points + ", answers=" + answers + '}';
    }

    @Override
    public boolean validate(ErrorHandlingUtils errorHandling) throws ParsingException {
        boolean correct = true;
        for (MatchingPair pair : pairs) {
            errorHandling.reportError(pair, new ParsingException("Multiple choice question '" + text + "' cannot define a matching pair '" + 
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
        }
        
        if (numberOfCorrect == 0 || numberOfIncorrect == 0) {
            errorHandling.reportError(this, new ParsingException("Question '" + text + "' has to define at least one correct and one incorrect answer!"
                    + " Add them please."));
            correct = false;
        }
        
        boolean superCorrect = super.validate(errorHandling);
        return correct && superCorrect;
    }
}
