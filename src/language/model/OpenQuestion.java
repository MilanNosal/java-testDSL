package language.model;

/**
 * Question with open answer.
 * @author Milan
 */
public class OpenQuestion implements Question {
    
    public static final String NAME = "open-answer-question";
    
    private String text;
    
    private int points;
    
    private Answer answer;
    
    private boolean caseSensitive = false;

    public OpenQuestion(String text, int points) {
        this.text = text;
        this.points = points;
    }

    public OpenQuestion(String text, int points, Answer answer) {
        this.text = text;
        this.points = points;
        this.answer = answer;
    }
    
    public OpenQuestion(String text, int points, Answer answer, boolean caseSensitive) {
        this.text = text;
        this.points = points;
        this.answer = answer;
        this.caseSensitive = caseSensitive;
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

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
    
    @Override
    public void addAnswer(Answer answer) {
        if (this.answer != null) {
            throw new RuntimeException(NAME + " with text \'" + text + "\' cannot have multiple answers, provide only a single correct answer!");
        } else {
            if (!answer.isCorrect()) {
                throw new RuntimeException(NAME + " with text \'" + text + "\' cannot have wrong answer, provide only a single correct answer!");
            } else {
                this.answer = answer;
            }
        }
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
+ "                    <input type='text' class='form-control' correctAnswer='" + answer.getText() + "' caseSensitive='" + caseSensitive + "'>\n"
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
        return "OpenQuestion{" + "text=" + text + ", points=" + points + ", answer=" + answer + ", caseSensitive=" + caseSensitive + '}';
    }
}
