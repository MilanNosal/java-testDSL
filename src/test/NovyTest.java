//EDSLAddon_start_fold desc="Definíciu testu začneš písať tu (test spustíš kombináciou tlačidiel Shift + F6):"
package test;

import language.builder.ParsingException;
import language.builder.TestBuilder;

public class NovyTest extends TestBuilder {
    
    @Override
    protected void define() {
//EDSLAddon_end_fold
        test("Názov testu", 51,
                otazka_s_jednou_spravnou_odpovedou("Koľko mám rokov?", 10, 
                        spravna_odpoved("28 rokov"),
                        nespravna_odpoved("29 rokov"),
                        nespravna_odpoved("30 rokov"),
                        spravna_odpoved("31 rokov")
                )
        );
//EDSLAddon_start_fold desc="Tu končí priestor na zápis."
    }
    
    public static void main(String[] args) throws ParsingException {
        new NovyTest().compose();
    }
}
//EDSLAddon_end_fold
