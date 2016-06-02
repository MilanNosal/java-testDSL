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
    
    /**
     * Reprezentuje pozitívne rozhodnutie pre podporu odlišnosti veľkých a malých písmen.
     */
    protected final static boolean ANO = true;
    
    /**
     * Reprezentuje negatívne rozhodnutie pre podporu odlišnosti veľkých a malých písmen (na rozdiel sa nemá brať ohľad).
     */
    protected final static boolean NIE = false; 
    
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
            System.err.println("Našli sa chyby, prosím oprav ich a spusti test znova.");
        }
    }
    
    /**
     * Vytvorí nový test. V rámci testu je možné definovať jeho názov, počet percent, ktoré je potrebné
     * dosiahnúť na úspech v teste, a zoznam otázok, ktoré budú v teste. Ak je definícia testu bez chyby,
     * test sa automaticky spustí, inak vypíše chyby, ktoré musíš opraviť (spolu s odkazom na riadok kde je chyba).
     * @param nazov Text v úvodzovkách definujúci názov testu.
     * @param potrebnePercentaBodov Číslo od 0 do 100 definujúce, koľko percent bodov je potrebné dosiahnúť na úspech.
     * @param otazky Zoznam otázok, z ktorých sa test skladá.
     */
    protected void test(String nazov, int potrebnePercentaBodov, Question... otazky) {
        test = new Test(nazov, potrebnePercentaBodov);
        Arrays.stream(otazky).forEach((q) -> test.addQuestion(q));
        errorHandling.registerObject(test);
    }
    
    /**
     * Vytvorí otázku s jedinou správnou odpoveďou. Otázka definuje viacero odpovedí, z ktorých je jedna správna.
     * Pre výber odpovede je použitý radio button umožňujúci vybrať len jednu odpoveď.
     * Otázka musí mať práve jednu správnu a aspoň jednu nesprávnu odpoveď.
     * @param text Text otázky v úvodzovkách.
     * @param body Počet bodov za danú otázku (musí byť aspoň jeden bod).
     * @param odpovede Zoznam odpovedí v poradí, v akom sa majú zobraziť.
     * @return 
     */
    protected SingleOptionQuestion otazka_s_jednou_spravnou_odpovedou(String text, int body, Answer... odpovede) {
        SingleOptionQuestion soq = new SingleOptionQuestion(text, body);
        Arrays.stream(odpovede).forEach((a) -> soq.addAnswer(a));
        return errorHandling.registerObject(soq);
    }
    
    /**
     * Vytvorí otázku s viacerými správnymi možnosťami. Správnych odpovedí môže byť viac. Za výber každej z nich je možné získať
     * podiel z možných bodov, napr. ak sú 2 správne odpovede dokopy za 5 bodov, vybratím jednej zíkame 2.5 boda (5/2). Ak však
     * vyberieme čo i len jednu nesprávnu odpoveď, nezískame žiadne body.
     * Otázka musí mať aspoň jednu správnu a aspoň jednu nesprávnu odpoveď.
     * @param text Text otázky v úvodzovkách.
     * @param body Počet bodov za danú otázku (musí byť aspoň jeden bod).
     * @param odpovede Zoznam odpovedí v poradí, v akom sa majú zobraziť.
     * @return 
     */
    protected MultipleOptionsQuestion otazka_s_viacerymi_spravnymi_odpovedami(String text, int body, Answer... odpovede) {
        MultipleOptionsQuestion moq = new MultipleOptionsQuestion(text, body);
        Arrays.stream(odpovede).forEach((a) -> moq.addAnswer(a));
        return errorHandling.registerObject(moq);
    }
    
    /**
     * Vytvorí otázku s voľnou odpoveďou (nie je poskytnutá množina odpovedí, z ktorej sa vyberá, ale je
     * potrebné odpoveď vpísať do políčka). Otázka má len jednu správnu odpoveď, ktorú je potrebné uhádnuť. Akákoľvek
     * iná vpísaná odpoveď je považovaná za nesprávnu. Predvolene neberie ohľad na veľkosť písmen, a teda napr. ak máme správnu odpoveď
     * "Mačička", test uzná za správnu aj hodnotu "MAČIČKA".
     * @param text Text otázky v úvodzovkách.
     * @param body Počet bodov za danú otázku (musí byť aspoň jeden bod).
     * @param spravnaOdpoved Jedna správna odpoveď, s ktorou sa má porovnávať hodnota vpísaná do políčka.
     * @return 
     */
    protected OpenQuestion otazka_s_volnou_odpovedou(String text, int body, Answer... spravnaOdpoved) {
        OpenQuestion oaq = new OpenQuestion(text, body);
        oaq.setCaseSensitive(NIE);
        Arrays.stream(spravnaOdpoved).forEach((a) -> oaq.addAnswer(a));
        return errorHandling.registerObject(oaq);
    }
    
    /**
     * Vytvorí otázku s voľnou odpoveďou (nie je poskytnutá množina odpovedí, z ktorej sa vyberá, ale je
     * potrebné odpoveď vpísať do políčka). Otázka má len jednu správnu odpoveď, ktorú je potrebné uhádnuť. Akákoľvek
     * iná vpísaná odpoveď je považovaná za nesprávnu. Umožňuje rozhodnúť, či sa má brať ohľad na veľkosť písmen alebo nie. Ak sa ohľad berie,
     * tak napr. ak máme správnu odpoveď "Mačička", test neuzná za správnu hodnotu "MAČIČKA".
     * @param text Text otázky v úvodzovkách.
     * @param body Počet bodov za danú otázku (musí byť aspoň jeden bod).
     * @param zohladnitVelkeAMalePismena Určuje, či sa pri porovnávaní správnej odpovede s vpísanou hodnotou má brať ohľad na rozdiel vo veľkosti písmen (Zadaj ANO, alebo NIE).
     * @param spravnaOdpoved Jedna správna odpoveď, s ktorou sa má porovnávať hodnota vpísaná do políčka.
     * @return 
     */
    protected OpenQuestion otazka_s_volnou_odpovedou(String text, int body, boolean zohladnitVelkeAMalePismena, Answer... spravnaOdpoved) {
        OpenQuestion oaq = new OpenQuestion(text, body);
        oaq.setCaseSensitive(zohladnitVelkeAMalePismena);
        Arrays.stream(spravnaOdpoved).forEach((a) -> oaq.addAnswer(a));
        return errorHandling.registerObject(oaq);
    }
    
    /**
     * Vytvorí otázku, v ktorej riešiteľ hľadá dvojice (páry), ktoré k sebe patria. Príkladom môžu byť páry
     * ako "škola" - "učiteľ", "lietadlo" - "pilot", "kasárne" - "vojak", a podobne, kde ku každému výrazu na ľavo
     * je potrebné nájsť správny prvok z množiny tých čo sú napravo. Prvky sprava sú náhodne premiešané, aby nebolo možné ľahko uhádnuť čo patrí k čomu.
     * @param text Text otázky v úvodzovkách.
     * @param body Počet bodov za danú otázku (musí byť aspoň jeden bod).
     * @param dvojice Zoznam dvojíc, ktoré k sebe patria. Prvý prvok dvojice sa zobrazí naľavo, druhý sa dá do množiny, z ktorej je potom potrebné vybrať ten správny prvok.
     * @return 
     */
    protected MatchingPairsQuestion otazka_na_hladanie_parov(String text, int body, MatchingPair... dvojice) {
        MatchingPairsQuestion mpq = new MatchingPairsQuestion(text, body);
        Arrays.stream(dvojice).forEach((p) -> mpq.addPair(p));
        return errorHandling.registerObject(mpq);
    }
    
    /**
     * Vytvorí správnu odpoveď na otázku.
     * @param text Text odpovede v úvodzovkách.
     * @return 
     */
    protected Answer spravna_odpoved(String text) {
        Answer answer = new Answer(text, true);
        return errorHandling.registerObject(answer);
    }
    
    /**
     * Vytvorí nesprávnu odpoveď na otázku.
     * @param text Text odpovede v úvodzovkách.
     * @return 
     */
    protected Answer nespravna_odpoved(String text) {
        Answer answer = new Answer(text, false);
        return errorHandling.registerObject(answer);
    }
    
    /**
     * Vytvorí dvojicu k sebe patriacich prvkov pre otázku na hľadanie párov. Prvý prvok je ten,
     * ktorý je zobrazený, druhý je ten, ktorý sa pridá do zoznamu, z ktorého sa vyberá správna dvojica.
     * @param prvyPrvok Prvok, ktorý je zobrazený používateľovi.
     * @param druhyPrvok Prvok, ktorý sa pridá do množiny možností, z ktorých sa vyberá správny pár.
     * @return 
     */
    protected MatchingPair dvojica(String prvyPrvok, String druhyPrvok) {
        MatchingPair pair = new MatchingPair(prvyPrvok, druhyPrvok);
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
