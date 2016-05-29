package language.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Question type with multiple correct answers.
 * @author Milan
 */
public class MultipleOptionsQuestion implements Question {
    
    public static final String NAME = "multiple-options-question";
    
    private String text;
    
    private int points;
    
    private List<Answer> answers = new LinkedList<>();

    public MultipleOptionsQuestion(String text, int points) {
        this.text = text;
        this.points = points;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
    
    @Override
    public void addAnswer(Answer answer) {
        answers.add(answer);
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
}
