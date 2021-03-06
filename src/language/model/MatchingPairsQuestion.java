package language.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import language.builder.ErrorHandlingUtils;
import language.builder.ParsingException;

/**
 * Question type providing lists 
 * @author Milan
 */
public class MatchingPairsQuestion extends Question {
    
    public static final String NAME = "matching-pairs-question";

    public MatchingPairsQuestion(String text, int points) {
        this.text = text;
        this.points = points;
    }
    
    public List<String> getAllMatchingOptions() {
        List<String> list = new LinkedList<>(pairs.stream().map((pair) -> {
            return pair.getRight();
        }).collect(Collectors.toSet()));
        Collections.shuffle(list);
        return list;
    }

    @Override
    public String toString() {
        return "MatchingPairsQuestion{" + "text=" + text + ", points=" + points + ", pairs=" + pairs + '}';
    }

    @Override
    public String toHTML(String id) {
        String allOptionsHtml = getAllMatchingOptions().stream()
                .map((option) -> {
                    return "<option value='" + option + "'>" + option + "</option>";
                }).reduce("", (a, b) -> a + "\n                        " + b);
        
        String allPairs = pairs.stream().map((pair) -> {
            return "<div class='form-group'>\n"
+ "                <label class='col-sm-offset-3 control-label col-sm-3 slimFont'>" + pair.getLeft() + "</label>\n"
+ "                <label class=' control-label col-sm-1' style='text-align: center;'>&#x21d0;&#x21d2;</label>\n"
+ "\n"
+ "                <div class='col-sm-4'>\n"
+ "                    <select class='form-control' correct='" + pair.getRight() + "'>" + allOptionsHtml + "\n"
+ "                    </select>\n"
+ "                </div>\n"
+ "            </div>\n";
        }).reduce("", (a, b) -> a + "\n            " + b);
        
        String retVal = "    <div style='margin:25px 0 25px 0;'>\n"
+ "        <form class='form-horizontal questionHeader' role='form' id='" + id + "' points='" + points + "'>\n"
+ "            <div class='form-group'>\n"
+ "                <label class='control-label col-sm-3'>" + text + "</label>\n"
+ "                <label class='col-sm-offset-8 control-label col-sm-1 points'>?/" + points + "</label>\n"
+ "            </div>\n"
+ "\n"
+ allPairs
+ "\n        </form>\n"
+ "    </div>\n";
        return retVal;
    }

    @Override
    public String toJS(String id) {
        return "testPairingAnswer('#" + id + "')";
    }

    @Override
    public boolean validate(ErrorHandlingUtils errorHandling) throws ParsingException {
        boolean correct = true;
        
        for (Answer a : answers) {
            errorHandling.reportError(a, new ParsingException("Pairing question '" + text + "' cannot contain answer '" + 
                    a.getText()+ "'! Remove it, or change the question type."));
            correct = false;
        }
        
        boolean superCorrect = super.validate(errorHandling);
        return correct && superCorrect;
    }
}
