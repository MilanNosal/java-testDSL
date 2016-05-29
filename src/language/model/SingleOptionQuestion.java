package language.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Question type with several options and a single correct answer.
 * @author Milan
 */
public class SingleOptionQuestion implements Question {
    
    public static final String NAME = "single-option-question";
    
    private String text;
    
    private int points;
    
    private List<Answer> answers = new LinkedList<>();

    public SingleOptionQuestion(String text, int points) {
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
        if (!answer.isCorrect()) {
            this.answers.add(answer);
        } else {
            if (this.answers.stream().map((a) -> a.isCorrect()).reduce(false, (a, b) -> a || b)) {
                throw new RuntimeException(NAME + " with text \'" + text + "\' cannot have multiple correct answers!");
            } else {
                this.answers.add(answer);
            }
        }
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
}
