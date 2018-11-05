package lo.tr.tools.spellchecker;

import java.io.IOException;
import java.util.List;
import zemberek.core.turkish.RootAttribute;
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
      // add a predicate to the spell checker
      // so that informal or out of official Turkish dictionary words are not allowed.
      this.spellChecker.setAnalysisPredicate(
          a -> !a.getDictionaryItem().hasAnyAttribute(RootAttribute.Ext, RootAttribute.Informal));
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
    for (int i = 1; i <= s.length() - 2; i++) {
      String s1 = s.substring(0, i);
      String s2 = s.substring(i);
      if (isCorrect(s1) && isCorrect(s2)) {
        suggestions.add(0, s1 + " " + s2);
      }
    }
    if (suggestions.size() > 7) {
      return suggestions.subList(0, 7);
    }
    return suggestions;
  }

  private String removePunctuation(String s) {
    return s.replaceAll("\\p{Punct}+$", "");
  }
}
