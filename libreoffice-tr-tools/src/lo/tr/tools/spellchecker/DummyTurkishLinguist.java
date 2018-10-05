package lo.tr.tools.spellchecker;

import java.util.*;

class DummyTurkishLinguist {

    private Map<String, List<String>> spellMap = new HashMap<>();

    DummyTurkishLinguist() {
        spellMap.put("yanlız", Arrays.asList("yalnız", "yanlı"));
        spellMap.put("mrb", Arrays.asList("merhaba", "maraba"));
        spellMap.put("keske", Arrays.asList("keşke", "keski", "keşkek"));
    }

    List<String> getSuggestions(String s) {
        return spellMap.containsKey(s) ? spellMap.get(s) : new ArrayList<>(0);
    }

    boolean isCorrect(String w) {
        return !spellMap.containsKey(w);
    }
}
