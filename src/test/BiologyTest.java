//EDSLAddon_start_fold desc="Start defining test here (run it by pressing Shift + F6):"
package test;

import language.builder.ParsingException;
import language.builder.TestBuilder;

public class BiologyTest extends TestBuilder {
    
    @Override
    protected void define() {
//EDSLAddon_end_fold
        create_test("Biology test", 20);

        multiple_choice_question("Which of the following are herbivores?", 10);
        incorrect_answer("Lion");
        correct_answer("Sheep");
        incorrect_answer("Bear");
        correct_answer("Cow");

        open_answer_question("What does a cat say?", 20, "Meow");

        pairing_question("Combine males and females:", 10);
        pair("Lion", "Lioness");
        pair("Bull", "Cow");
        pair("Tiger", "Tigress");
//EDSLAddon_start_fold desc="Here ends the definition."
    }
    
    public static void main(String[] args) throws ParsingException {
        new BiologyTest().compose();
    }
}
//EDSLAddon_end_fold
