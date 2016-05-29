package language.builder;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import language.model.Answer;
import language.model.MatchingPair;
import language.model.MatchingPairsQuestion;
import language.model.MultipleOptionsQuestion;
import language.model.OpenQuestion;
import language.model.Question;
import language.model.SingleOptionQuestion;
import language.model.Test;

/**
 * Expression builder for tests.
 * @author Milan
 */
public abstract class TestBuilder {
    /**
     * ErrorHandlingUtils object for advanced error handling.
     */
    private final ErrorHandlingUtils errorHandling = new ErrorHandlingUtils(this.getClass());
    
    private Test test;
    
    /**
     * The abstract define method will get overriden in concrete definition
     * classes.
     */
    protected abstract void define();
    
    public Test getTest() {
        return this.test;
    }
    
    /**
     * This method is called to create the model. It uses the concrete define
     * method of the definition class to create model and then it validates it.
     * @throws ParsingException 
     */
    public void compose() throws ParsingException {
        define();
        if (validate()) {
            generate();
        }
    }
    
    
    protected void test(String title, int minimalPercentage, Question... questions) {
        test = new Test(title, minimalPercentage);
        Arrays.stream(questions).forEach((q) -> test.addQuestion(q));
        errorHandling.registerObject(test);
    }
    
    protected SingleOptionQuestion single_choice_question(String text, int points, Answer... answers) {
        SingleOptionQuestion soq = new SingleOptionQuestion(text, points);
        Arrays.stream(answers).forEach((a) -> soq.addAnswer(a));
        return errorHandling.registerObject(soq);
    }
    
    protected MultipleOptionsQuestion multiple_choice_question(String text, int points, Answer... answers) {
        MultipleOptionsQuestion moq = new MultipleOptionsQuestion(text, points);
        Arrays.stream(answers).forEach((a) -> moq.addAnswer(a));
        return errorHandling.registerObject(moq);
    }
    
    protected OpenQuestion open_answer_question(String text, int points, Answer answer) {
        OpenQuestion oaq = new OpenQuestion(text, points, answer, false);
        return errorHandling.registerObject(oaq);
    }
    
    protected OpenQuestion open_answer_question(String text, int points, boolean caseSensitive, Answer answer) {
        OpenQuestion oaq = new OpenQuestion(text, points, answer, caseSensitive);
        return errorHandling.registerObject(oaq);
    }
    
    protected MatchingPairsQuestion matching_question(String text, int points, MatchingPair... pairs) {
        MatchingPairsQuestion mpq = new MatchingPairsQuestion(text, points);
        Arrays.stream(pairs).forEach((p) -> mpq.addPair(p));
        return errorHandling.registerObject(mpq);
    }
    
    protected Answer correct_answer(String text) {
        Answer answer = new Answer(text, true);
        return errorHandling.registerObject(answer);
    }
    
    protected Answer wrong_answer(String text) {
        Answer answer = new Answer(text, false);
        return errorHandling.registerObject(answer);
    }
    
    protected MatchingPair matching_pair(String leftValue, String rightValue) {
        MatchingPair pair = new MatchingPair(leftValue, rightValue);
        return errorHandling.registerObject(pair);
    }
    
    /**
     * Validate method does some validation upon the model. In this particular
     * case it checks for duplication in entities and in their properties.
     * For the error reporting ti uses the ErrorHandlingUtils object.
     * @throws ParsingException 
     */
    private boolean validate() throws ParsingException {
//        List<String> usedEntNames = new LinkedList<String>();
//        for(Entity entity : model.getEntities()) {
//            if(usedEntNames.contains(entity.getName())) {
//                // the user will be navigated to the line where
//                // the duplicated entity was defined
//                errorHandling.reportError(entity, 
//                        new ParsingException("Defined duplicate entity with name " + entity.getName()));
//            } else {
//                usedEntNames.add(entity.getName());
//            }
//            List<String> usedPropNames = new LinkedList<String>();
//            for(Property prop : entity.getProperties()) {
//                if(usedPropNames.contains(prop.getName())) {
//                    errorHandling.reportError(prop, 
//                            new ParsingException("Duplicate property in entity " + entity.getName()
//                            + " with name " + prop.getName()));
//                } else {
//                    usedPropNames.add(prop.getName());
//                }
//            }
//        }
        return true;
    }
    
    private void generate() {
        String output = test.toHTML();
        
        File file = new File("html/" + test.getTitle() + ".html");
        file.mkdir();
        if (file.exists()) {
            file.delete();
        }

        Path path = Paths.get("html/" + test.getTitle() + ".html");
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {
            writer.write(output);
        } catch (IOException ex) {
            Logger.getLogger(TestBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        file = new File("html/" + test.getTitle() + ".html");
        if(Desktop.isDesktopSupported())
        {
            try {
                Desktop.getDesktop().browse(file.toURI());
            } catch (IOException ex) {
                Logger.getLogger(TestBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
