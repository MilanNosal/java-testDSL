package test;

import language.builder.ParsingException;
import language.builder.TestBuilder;

public class BiologyTest extends TestBuilder {

    @Override
    protected void define() {
        test("Test z Biológie", 51,
                otazka_s_jednou_spravnou_odpovedou("Ako robí mačička?", 10, 
                        nespravna_odpoved("hav"),
                        nespravna_odpoved("múúú"),
                        spravna_odpoved("miau"),
                        nespravna_odpoved("bééé")
                ),
                otazka_s_volnou_odpovedou("Aké zvieratko robí kváák?", 20,
                        spravna_odpoved("žabka")
                ),
                otazka_s_viacerymi_spravnymi_odpovedami("Ktoré zvieratká papkajú trávičku?", 14,
                        nespravna_odpoved("levík"),
                        nespravna_odpoved("medvedík"),
                        spravna_odpoved("kravička"),
                        spravna_odpoved("ovečka"),
                        nespravna_odpoved("hyenka")
                ),
                otazka_na_hladanie_parov("Nájdi dievčatko od každého zvieratka:", 20,
                        dvojica("lev", "levica"),
                        dvojica("had", "hadica"),
                        dvojica("krab", "krabica"),
                        dvojica("medveď", "medvedica")
                ),
                otazka_s_volnou_odpovedou("Ako sa povie maličkej kravke?", 20, 
                        spravna_odpoved("teliatko")
                )
        );
    }
    
    public static void main(String[] args) throws ParsingException {
        new BiologyTest().compose();
    }
}
