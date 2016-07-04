package language.builder;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import language.model.Answer;
import language.model.MatchingPair;
import language.model.MatchingPairsQuestion;
import language.model.MultipleOptionsQuestion;
import language.model.OpenQuestion;
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
    
    /**
     * Represents a positive choice for case sensitivity.
     */
    protected final static boolean YES = true;
    
    /**
     * Represents a negative choice for case sensitivity - the test should be case-insensitive.
     */
    protected final static boolean NO = false; 
    
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
        } else {
            System.err.println("There were some errors, fix them and rerun the program.");
        }
    }
    
    /**
     * Creates a new test. You can define its title, minimal points to success, and the list of questions in the test.
     * If there are no errors in the test definition, the program generates an HTML + JS test and runs it. Otherwise,
     * an error is reported along with a link to the line of code that cause the error.
     * @param title Text in quotation marks that represent the title.
     * @param minimalPoints Points minimum for the success. Cannot be negative, nor it can be more than the maximum possible points for the test (calculated as the possible points for all the questions).
     */
    protected void create_test(String title, int minimalPoints) {
        test = new Test(title, minimalPoints);
        errorHandling.registerObject(test);
    }
    
    /**
     * Creates a single choice question. The question defines multiple answers, but only a single one is correct.
     * In the result it is represented by radio buttons.
     * @param text Question text in quotation marks.
     * @param points Positive number of points for the question.
     */
    protected void single_choice_question(String text, int points) {
        SingleOptionQuestion soq = new SingleOptionQuestion(text, points);
        errorHandling.registerObject(soq);
        test.addQuestion(soq);
    }
    
    /**
     * Creates a multiple choice question. This question type can have multiple correct question, for each selected correct answer the user receives
     * a portion of the possible points, e.g., if there are ten points for the question and it has 2 correct answers, each answer is worth 5 points.
     * Selecting incorrect answer nullifies all the points.
     * The question has to have at least one correct and one incorrect answer.
     * @param text Question text in quotation marks.
     * @param points Positive number of points for the question.
     */
    protected void multiple_choice_question(String text, int points) {
        MultipleOptionsQuestion moq = new MultipleOptionsQuestion(text, points);
        errorHandling.registerObject(moq);
        test.addQuestion(moq);
    }
    
    /**
     * Creates open answer question (no options are provided, the user has to fill in a text box). The question has only a single
     * correct answer, all other answers are considered incorrect. By default the answer is not case sensitive.
     * @param text Question text in quotation marks.
     * @param points Positive number of points for the question.
     * @param correctAnswer The correct answer to the question.
     */
    protected void open_answer_question(String text, int points, String correctAnswer) {
        OpenQuestion oaq = new OpenQuestion(text, points);
        oaq.setCaseSensitive(NO);
        errorHandling.registerObject(oaq);
        test.addQuestion(oaq);
        correct_answer(correctAnswer);
    }
    
    /**
     * Creates pairing question. As an example there can be pairs such as
     *  "school" - "teacher", "airplane" - "pilot", "barracks" - "soldier", etc., where you have to match each item on the left
     * with the corresponding one on the right. In the result, items on the right are randomly shuffled.
     * @param text Question text in quotation marks.
     * @param points Positive number of points for the question.
     */
    protected void pairing_question(String text, int points) {
        MatchingPairsQuestion mpq = new MatchingPairsQuestion(text, points);
        errorHandling.registerObject(mpq);
        test.addQuestion(mpq);
    }
    
    /**
     * Creates a correct answer option for a question.
     * @param text Answer's text in quotation marks.
     */
    protected void correct_answer(String text) {
        Answer answer = new Answer(text, true);
        errorHandling.registerObject(answer);
        test.getLastQuestion().addAnswer(answer);
    }
    
    /**
     * Creates an incorrect answer option for a question.
     * @param text Answer's text in quotation marks.
     */
    protected void incorrect_answer(String text) {
        Answer answer = new Answer(text, false);
        errorHandling.registerObject(answer);
        test.getLastQuestion().addAnswer(answer);
    }
    
    /**
     * Create a pair of corresponding items for the pairing question type.
     * @param firstItem Item to be shown to the user.
     * @param secondItem Item to be included in the selection list.
     */
    protected void pair(String firstItem, String secondItem) {
        MatchingPair pair = new MatchingPair(firstItem, secondItem);
        errorHandling.registerObject(pair);
        test.getLastQuestion().addPair(pair);
    }
    
    /**
     * Validate method does some validation upon the model. In this particular
     * case it checks for duplication in entities and in their properties.
     * For the error reporting ti uses the ErrorHandlingUtils object.
     * @throws ParsingException 
     */
    private boolean validate() throws ParsingException {
        return test.validate(errorHandling);
    }
    
    private void generate() {
        String output = test.toHTML();
        
        File file = new File("html/test.html");
        file.mkdir();
        if (file.exists()) {
            file.delete();
        }

        Path path = Paths.get("html/test.html");
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {
            writer.write(output);
        } catch (IOException ex) {
            Logger.getLogger(TestBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        file = new File("html/test.html");
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
