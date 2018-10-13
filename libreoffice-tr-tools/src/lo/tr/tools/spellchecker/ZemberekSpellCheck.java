package lo.tr.tools.spellchecker;

import java.io.IOException;
import java.util.List;
import zemberek.morphology.TurkishMorphology;
import zemberek.normalization.TurkishSpellChecker;

public class ZemberekSpellCheck implements TurkishLinguist{

    private static ZemberekSpellCheck zemberekSpellCheckInstance;

    private ZemberekSpellCheck(){}

    public static ZemberekSpellCheck getInstance(){
        if (zemberekSpellCheckInstance == null){
            zemberekSpellCheckInstance = new ZemberekSpellCheck();
        }
        return zemberekSpellCheckInstance;
    }

    public boolean isCorrect(String w){
        try {
            TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
            TurkishSpellChecker spellChecker = new TurkishSpellChecker(morphology);
            return spellChecker.check(w);
        }catch(IOException ex) { return true;}
    }
    public List<String> getSuggestions(String s) {
        try {
            TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
            TurkishSpellChecker spellChecker = new TurkishSpellChecker(morphology);
            return spellChecker.suggestForWord(s);
        }catch(IOException ex) { return null;}
    }
}
