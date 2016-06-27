package language.model;

import language.builder.ErrorHandlingUtils;
import language.builder.ParsingException;

/**
 * Question with open answer.
 * @author Milan
 */
public class OpenQuestion extends Question {
    
    public static final String NAME = "open-answer-question";
    
    private boolean caseSensitive = false;

    public OpenQuestion(String text, int points) {
        this.text = text;
        this.points = points;
    }

    public OpenQuestion(String text, int points, Answer answer) {
        this.text = text;
        this.points = points;
        this.answers.add(answer);
    }
    
    public OpenQuestion(String text, int points, Answer answer, boolean caseSensitive) {
        this.text = text;
        this.points = points;
        this.answers.add(answer);
        this.caseSensitive = caseSensitive;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    @Override
    public String toHTML(String id) {
        String retVal = 
        "    <div style='margin:25px 0 25px 0;'>\n"
+ "        <form class='form-horizontal questionHeader' role='form' id='" + id + "' points='" + points + "'>\n"
+ "            <div class='form-group'>\n"
+ "                <label class='control-label col-sm-3'>" + text + "</label>\n"
+ "\n"
+ "                <div class='col-sm-8'>\n"
+ "                    <input type='text' class='form-control' correctAnswer='" + answers.get(0).getText() + "' caseSensitive='" + caseSensitive + "'>\n"
+ "                    " + (caseSensitive ? "<small class='text-muted'>Watch out, the question is case sensitive.</small>" : "<small class='text-muted'>Question is not case sensitive.</small>") + "\n"
+ "                </div>\n"
+ "                <label class='control-label col-sm-1 points'>?/" + points + "</label>\n"
+ "            </div>\n"
+ "        </form>\n"
+ "    </div>\n";
        return retVal;
    }

    @Override
    public String toJS(String id) {
        return "testFreeAnswer('#" + id + "')";
    }

    @Override
    public String toString() {
        return "OpenQuestion{" + "text=" + text + ", points=" + points + ", answer=" + answers.get(0).getText() + ", caseSensitive=" + caseSensitive + '}';
    }

    @Override
    public boolean validate(ErrorHandlingUtils errorHandling) throws ParsingException {
        boolean correct = true;
        for (MatchingPair pair : pairs) {
            errorHandling.reportError(pair, new ParsingException("Open answer Question '" + text + "' cannot define a matching pair '" + 
                    pair.getLeft() + "' <-> '" + pair.getRight() + "'! Remove it, or change the question type to one that supports it."));
            correct = false;
        }
        
        int numberOfAnswers = 0;
        for (Answer a : answers) {
            numberOfAnswers++;
            
            if (numberOfAnswers == 1) {
                if (!a.isCorrect()) {
                    errorHandling.reportError(a, new ParsingException("Open answer question '" + text + "' cannot define an incorrect answer (answer '" + a.getText() + "')!"));
                    correct = false;
                }
            } else {
                errorHandling.reportError(a, new ParsingException("Open answer question '" + text + "' cannot have multiple answers. Remove answer '" + a.getText() + "'."));
                correct = false;
            }
        }
        
        if (numberOfAnswers == 0) {
            errorHandling.reportError(this, new ParsingException("Open answer question '" + text + "' has to have a single correct answer! Add it please."));
            correct = false;
        }
        
        boolean superCorrect = super.validate(errorHandling);
        return correct && superCorrect;
    }
}
