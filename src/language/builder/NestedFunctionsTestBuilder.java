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

public abstract class NestedFunctionsTestBuilder {
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
     * Create a new test. You can define its title, minimal points to success, and the list of questions in the test.
     * If there are no errors in the test definition, the program generates an HTML + JS test and runs it. Otherwise,
     * an error is reported along with a link to the line of code that cause the error.
     * @param title Text in quotation marks that represent the title.
     * @param minimalPoints Points minimum for the success. Cannot be negative, nor it can be more than the maximum possible points for the test (calculated as the possible points for all the questions).
     * @param questions Questions for the test.
     */
    protected void create_test(String title, int minimalPoints, Question... questions) {
        test = new Test(title, minimalPoints);
        Arrays.stream(questions).forEach((q) -> test.addQuestion(q));
        errorHandling.registerObject(test);
    }
    
    /**
     * Create a single choice question. The question defines multiple answers, but only a single one is correct.
     * In the result it is represented by radio buttons.
     * @param text Question text in quotation marks.
     * @param points Positive number of points for the question.
     * @param answers Question answers.
     * @return A single choice question.
     */
    protected Question single_choice_question(String text, int points, Answer... answers) {
        SingleOptionQuestion soq = new SingleOptionQuestion(text, points);
        Arrays.stream(answers).forEach((a) -> soq.addAnswer(a));
        return errorHandling.registerObject(soq);
    }
    
    /**
     * Creates a multiple choice question. This question type can have multiple correct question, for each selected correct answer the user receives
     * a portion of the possible points, e.g., if there are ten points for the question and it has 2 correct answers, each answer is worth 5 points.
     * Selecting incorrect answer nullifies all the points.
     * The question has to have at least one correct and one incorrect answer.
     * @param text Question text in quotation marks.
     * @param points Positive number of points for the question.
     * @param answers Question answers.
     * @return A multiple choice question.
     */
    protected Question multiple_choice_question(String text, int points, Answer... answers) {
        MultipleOptionsQuestion moq = new MultipleOptionsQuestion(text, points);
        Arrays.stream(answers).forEach((a) -> moq.addAnswer(a));
        return errorHandling.registerObject(moq);
    }
    
    /**
     * Creates open answer question (no options are provided, the user has to fill in a text box). The question has only a single
     * correct answer, all other answers are considered incorrect. By default the answer is not case sensitive.
     * @param text Question text in quotation marks.
     * @param points Positive number of points for the question.
     * @param correctAnswer The correct answer to the question.
     * @return An open answer question.
     */
    protected Question open_answer_question(String text, int points, String correctAnswer) {
        OpenQuestion oaq = new OpenQuestion(text, points);
        oaq.setCaseSensitive(NO);
        oaq.addAnswer(correct_answer(correctAnswer));
        return errorHandling.registerObject(oaq);
    }
    
    /**
     * Creates pairing question. As an example there can be pairs such as
     *  "school" - "teacher", "airplane" - "pilot", "barracks" - "soldier", etc., where you have to match each item on the left
     * with the corresponding one on the right. In the result, items on the right are randomly shuffled.
     * @param text Question text in quotation marks.
     * @param points Positive number of points for the question.
     * @param pairs Matching pairs for the question.
     * @return A pairing question.
     */
    protected Question pairing_question(String text, int points, MatchingPair... pairs) {
        MatchingPairsQuestion mpq = new MatchingPairsQuestion(text, points);
        Arrays.stream(pairs).forEach((p) -> mpq.addPair(p));
        return errorHandling.registerObject(mpq);
    }
    
    /**
     * Creates a correct answer option for a question.
     * @param text Answer's text in quotation marks.
     * @return A correct answer with the given text.
     */
    protected Answer correct_answer(String text) {
        Answer answer = new Answer(text, true);
        return errorHandling.registerObject(answer);
    }
    
    /**
     * Creates an incorrect answer option for a question.
     * @param text Answer's text in quotation marks.
     * @return An incorrect answer.
     */
    protected Answer incorrect_answer(String text) {
        Answer answer = new Answer(text, false);
        return errorHandling.registerObject(answer);
    }
    
    /**
     * Create a pair of corresponding items for the pairing question type.
     * @param firstItem Item to be show to the user.
     * @param secondItem Item to be included in the selection list.
     * @return A pair that is to be matched.
     */
    protected MatchingPair pair(String firstItem, String secondItem) {
        MatchingPair pair = new MatchingPair(firstItem, secondItem);
        return errorHandling.registerObject(pair);
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
