package testdsljava;

import language.builder.ParsingException;
import language.builder.TestBuilder;

public class BiologyTest extends TestBuilder {

    @Override
    protected void define() {
        test("Test z biológie", 51,
                open_answer_question("Ako sa volá zviera, ktoré kváka?", 10,
                        correct_answer("Žaba")),
                single_choice_question("Ako robí mačka?", 8, 
                        wrong_answer("hav"),
                        wrong_answer("múú"),
                        correct_answer("miau"),
                        wrong_answer("krá")
                        ),
                multiple_choice_question("Ktoré zvieratká papkajú trávu?", 15, 
                        correct_answer("kravka"),
                        wrong_answer("lev"),
                        wrong_answer("medveď"),
                        correct_answer("ovečka"),
                        wrong_answer("vĺčik")),
                matching_question("Spojte príslušné vzťahy", 20, 
                        matching_pair("lev", "levíča"),
                        matching_pair("pes", "psíča"),
                        matching_pair("ovečka", "jahniatko"),
                        matching_pair("baran", "baranček"),
                        matching_pair("kravka", "teliatko")
                        ),
                matching_question("Spojte Nosáľovcov s ich zvieracími analógiami", 20, 
                        matching_pair("Maroš", "Prasa"),
                        matching_pair("Maťo", "Prasa"),
                        matching_pair("Milan", "Prasa"),
                        matching_pair("Michal", "Prasa")
                        )
        );
    }
    
    public static void main(String[] args) throws ParsingException {
        new BiologyTest().compose();
    }
}
