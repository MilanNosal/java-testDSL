package test;
import language.builder.NestedFunctionsTestBuilder;
import language.builder.ParsingException;

    public class BiologyNestedFunctions extends NestedFunctionsTestBuilder {
    @Override
    protected void define() {
        create_test("Biology test", 20,
                multiple_choice_question("Which of the following are herbivores?", 10, 
                        incorrect_answer("Lion"),
                        correct_answer("Sheep"),
                        incorrect_answer("Bear"),
                        correct_answer("Cow")
                        ),
                open_answer_question("What does a cat say?", 20, "Meow"),
                pairing_question("Combine males and females:", 10, 
                        pair("Lion", "Lioness"),
                        pair("Bull", "Cow"),
                        pair("Tiger", "Tigress")
                        )
                );
    }
    
    public static void main(String[] args) throws ParsingException {
        new BiologyNestedFunctions().compose();
    }
}
