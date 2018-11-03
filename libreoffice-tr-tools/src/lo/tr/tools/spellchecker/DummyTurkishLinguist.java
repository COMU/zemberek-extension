package lo.tr.tools.spellchecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DummyTurkishLinguist implements TurkishLinguist {

  private Map<String, List<String>> spellMap = new HashMap<>();

  DummyTurkishLinguist() {
    spellMap.put("yanlız", Arrays.asList("yalnız", "yanlı"));
    spellMap.put("mrb", Arrays.asList("merhaba", "maraba"));
    spellMap.put("keske", Arrays.asList("keşke", "keski", "keşkek"));
  }

  public List<String> getSuggestions(String s) {
    return spellMap.containsKey(s) ? spellMap.get(s) : new ArrayList<>(0);
  }

  public boolean isCorrect(String w) {
    return !spellMap.containsKey(w);
  }
}
