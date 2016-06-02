package test;

import language.builder.ParsingException;
import language.builder.TestBuilder;

public class BiologyTest extends TestBuilder {

    @Override
    protected void define() {
        test("Vzorový test, na ktorého úspešné prejdenie potrebuješ aspoň 51 percent bodov", 51,

                otazka_s_volnou_odpovedou("Vzorová otázka s voľnou odpoveďou za 10 bodov?", 10,
                        spravna_odpoved("odpoveď")
                ),

                otazka_s_jednou_spravnou_odpovedou("Vzorová otázka s jednou odpoveďou za 8 bodov?", 8,
                        nespravna_odpoved("odpoveď 1"),
                        nespravna_odpoved("odpoveď 2"),
                        spravna_odpoved("odpoveď 3")
                ),

                otazka_na_hladanie_parov("Vzorová otázka na hľadanie dvojíc za 20 bodov", 20,
                        dvojica("odpoveď 1", "odpoveď 1"),
                        dvojica("odpoveď 2", "odpoveď 2"),
                        dvojica("odpoveď 3", "odpoveď 3")
                )
        );
    }
    
    public static void main(String[] args) throws ParsingException {
        new BiologyTest().compose();
    }
}
