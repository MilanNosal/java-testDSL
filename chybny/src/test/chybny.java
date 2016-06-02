//EDSLAddon_start_fold desc="Definíciu testu začneš písať tu (test spustíš kombináciou tlačidiel Shift + F6):"
package test;

import language.builder.ParsingException;
import language.builder.TestBuilder;

public class chybny extends TestBuilder {
    
    @Override
    protected void define() {
//EDSLAddon_end_fold
        test("Superhrdinovia", 110,
                otazka_s_viacerymi_spravnymi_odpovedami("Ktorí superhrdinovia patria k Avengers?", 20,
                        spravna_odpoved("Hulk"),
                        spravna_odpoved("Ironman"),
                        nespravna_odpoved("Batman"),
                        spravna_odpoved("Hulk"),
                        nespravna_odpoved("Superman")
                ),
                otazka_na_hladanie_parov("Spojte superhrdinov s ich úhlavnými nepriateľmi", 25,
                        dvojica("Superman", "Lex Luthor"),
                        dvojica("Batman", "Joker"),
                        dvojica("Spiderman", "Goblin"),
                        dvojica("Kapitán Amerika", "Hydra"),
                        dvojica("Professor X", "Magneto")
                ),
                otazka_s_volnou_odpovedou("Ktorý superhrdina už nemá rodičov?", 30,
                        spravna_odpoved("Batman"),
                        spravna_odpoved("Spiderman")
                ),
                otazka_s_volnou_odpovedou("Na akú horninu je alergický Superman?", 10, 
                        spravna_odpoved("Kryptonit")
                ),
                otazka_s_viacerymi_spravnymi_odpovedami("Ktorí z nasledujúcich hrdinov sú x-meni (mutanti)?", 15, 
                        spravna_odpoved("Wolverine"),
                        nespravna_odpoved("Batman bin Superman"),
                        nespravna_odpoved("Pacho Hybský zbojník"),
                        spravna_odpoved("Storm"),
                        nespravna_odpoved("Thor")
                ),
                otazka_s_jednou_spravnou_odpovedou("Ktorý z týchto superhrdinov je členom Justice League?", 15, 
                        nespravna_odpoved("Ironman"),
                        nespravna_odpoved("Čierna vdova"),
                        nespravna_odpoved("Batman"),
                        nespravna_odpoved("Hawkeye"),
                        nespravna_odpoved("Thor")
                ),
                otazka_s_volnou_odpovedou("Kto je Batmanovým pomocníkom?", 10, 
                        spravna_odpoved("Robin")
                ),
                otazka_s_volnou_odpovedou("V akých novinách pracuje Clark Kent?", 0,
                        spravna_odpoved("Daily Planet")
                ),
                otazka_s_jednou_spravnou_odpovedou("Koľko filmov je s mutantmi (x-men)?", 20, 
                        nespravna_odpoved("6"),
                        nespravna_odpoved("7"),
                        spravna_odpoved("8"),
                        nespravna_odpoved("9"),
                        nespravna_odpoved("10")
                )
        );
//EDSLAddon_start_fold desc="Tu končí priestor na zápis."
    }
    
    public static void main(String[] args) throws ParsingException {
        new chybny().compose();
    }
}
//EDSLAddon_end_fold
