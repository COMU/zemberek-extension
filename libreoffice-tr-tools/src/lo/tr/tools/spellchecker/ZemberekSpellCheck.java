package lo.tr.tools.spellchecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import zemberek.core.ScoredItem;
import zemberek.core.turkish.RootAttribute;
import zemberek.lm.LmVocabulary;
import zemberek.lm.NgramLanguageModel;
import zemberek.morphology.TurkishMorphology;
import zemberek.normalization.TurkishSpellChecker;

public class ZemberekSpellCheck implements TurkishLinguist {

  public static ZemberekSpellCheck instance = new ZemberekSpellCheck();

  private TurkishMorphology morphology;
  private TurkishSpellChecker spellChecker;
  private NgramLanguageModel uniGramLanguageModel;

  private ZemberekSpellCheck() {
    this.morphology = TurkishMorphology.createWithDefaults();
    try {
      this.spellChecker = new TurkishSpellChecker(morphology);
      // add a predicate to the spell checker
      // so that informal or out of official Turkish dictionary words are not allowed.
      this.spellChecker.setAnalysisPredicate(
          a -> !a.getDictionaryItem().hasAnyAttribute(RootAttribute.Ext, RootAttribute.Informal));
      this.uniGramLanguageModel = spellChecker.getUnigramLanguageModel();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static ZemberekSpellCheck getInstance() {
    return instance;
  }

  public boolean isCorrect(String w) {
    String input = removePunctuation(w);
    int indexOfDash = input.indexOf("-");
    if (indexOfDash != -1) {
      String w1 = input.substring(0, indexOfDash);
      String w2 = input.substring(indexOfDash + 1);
      if (spellChecker.check(w1) && spellChecker.check(w2)) {
        return true;
      }
    }
    return spellChecker.check(input);
  }

  public List<String> getSuggestions(String s) {

    List<String> suggestions = spellChecker.suggestForWord(removePunctuation(s));
    if (suggestions.size() > 7) {
      return suggestions.subList(0, 7);
    }
    suggestions.addAll(splitWordSuggestions(s));
    return suggestions;
  }

  private String removePunctuation(String s) {
    return s.replaceAll("\\p{Punct}+$", "");
  }

  private List<String> splitWordSuggestions(String s) {

    // Prevent small or large inputs.
    if (s.length() < 3 || s.length() > 25) {
      return Collections.emptyList();
    }

    // Apply brute force splitting, and use uni-gram probabilities for ranking multiple scores.
    // Normally using a higher order language model would be the correct approach
    // for ranking but that is not available.
    List<ScoredItem<String>> suggestions = new ArrayList<>(3);
    LmVocabulary vocabulary = uniGramLanguageModel.getVocabulary();

    for (int i = 1; i < s.length() - 1; i++) {
      String s1 = s.substring(0, i);
      String s2 = s.substring(i);
      if (isCorrect(s1) && isCorrect(s2)) {
        float p1 = uniGramLanguageModel.getProbability(vocabulary.indexOf(s1));
        float p2 = uniGramLanguageModel.getProbability(vocabulary.indexOf(s2));
        suggestions.add(new ScoredItem<>(s1 + " " + s2, p1 + p2));
      }
    }

    if (suggestions.size() == 0) {
      return Collections.emptyList();
    }

    // Sort with scores. Higher scored item comes first.
    suggestions.sort((a, b) -> Float.compare(b.score, a.score));

    // Only top 3
    if (suggestions.size() > 3) {
      suggestions = suggestions.subList(0, 3);
    }

    return suggestions.stream().map(a -> a.item).collect(Collectors.toList());
  }


}
