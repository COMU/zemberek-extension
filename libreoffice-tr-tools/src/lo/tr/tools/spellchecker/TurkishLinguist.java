package lo.tr.tools.spellchecker;

import java.util.List;

public interface TurkishLinguist {

  boolean isCorrect(String w);

  List<String> getSuggestions(String s);
}
