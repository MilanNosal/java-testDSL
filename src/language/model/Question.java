package language.model;

import java.util.LinkedList;
import java.util.List;
import language.builder.ErrorHandlingUtils;
import language.builder.ParsingException;

/**
 * Interface for questions.
 * @author Milan
 */
public abstract class Question {
    
    protected String text;
    
    protected int points;
    
    protected List<MatchingPair> pairs = new LinkedList<>();
    
    protected List<Answer> answers = new LinkedList<>();
    
    public abstract String toHTML(String id);
    
    public abstract String toJS(String id);

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<MatchingPair> getPairs() {
        return pairs;
    }

    public void setPairs(List<MatchingPair> pairs) {
        this.pairs = pairs;
    }
    
    public void addPair(MatchingPair pair) {
        this.pairs.add(pair);
    }
    
    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
    
    public void addAnswer(Answer answer) {
        answers.add(answer);
    }
    
    public boolean validate(ErrorHandlingUtils errorHandling) throws ParsingException {
        boolean correct = true;
        List<String> usedAnswers = new LinkedList<>();
        for (Answer a : answers) {
            if (usedAnswers.contains(a.getText())) {
                errorHandling.reportError(a, 
                        new ParsingException("In the same question '" + text + "' there are two identical answers: '" + a.getText() + "'! Remove one."));
                correct = false;
            } else {
                usedAnswers.add(a.getText());
            }
        }
        usedAnswers.clear();
        for (MatchingPair mp : pairs) {
            if (usedAnswers.contains(mp.getLeft() + mp.getRight())) {
                errorHandling.reportError(mp, 
                        new ParsingException("In the same question '" + text + "' there are two identical matching pairs: '" + mp.getLeft() + "' <-> '" + mp.getRight() + "'!"
                                + " Remove one."));
                correct = false;
            } else {
                usedAnswers.add(mp.getLeft() + mp.getRight());
            }
        }
        
        if (text.trim().isEmpty()) {
            errorHandling.reportError(this, new ParsingException("Every question has to have text! Add it please."));
            correct = false;
        }
        
        if (points < 1) {
            errorHandling.reportError(this, new ParsingException("Question '" + text + "' has to have a positive number of points (at least one, you have stated '" + points + "')! Fix it."));
            correct = false;
        }
        
        return correct;
    }
}
