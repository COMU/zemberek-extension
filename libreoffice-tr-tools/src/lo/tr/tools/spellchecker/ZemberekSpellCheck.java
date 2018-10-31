package lo.tr.tools.spellchecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import zemberek.morphology.TurkishMorphology;
import zemberek.normalization.TurkishSpellChecker;

public class ZemberekSpellCheck implements TurkishLinguist {

  public static ZemberekSpellCheck instance = new ZemberekSpellCheck();

  private TurkishMorphology morphology;
  private TurkishSpellChecker spellChecker;


  private ZemberekSpellCheck() {
    this.morphology = TurkishMorphology.createWithDefaults();
    try {
      this.spellChecker = new TurkishSpellChecker(morphology);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static ZemberekSpellCheck getInstance() {
    return instance;
  }

  public boolean isCorrect(String w) {
    return spellChecker.check(removePunctuation(w));
  }

  public List<String> getSuggestions(String s) {
    List<String> suggestions = spellChecker.suggestForWord(removePunctuation(s));
    if (suggestions.size() > 7) {
      return suggestions.subList(0, 7);
    }
    return suggestions;
  }

  private String removePunctuation(String s) {
    return s.replaceAll("\\p{Punct}+$", "");
  }

}