package lo.tr.tools.spellchecker;

import org.junit.Assert;
import org.junit.Test;
import zemberek.morphology.lexicon.RootLexicon;

public class ZemberekSpellCheckTest {

  @Test
  public void testIncorrectWords1() {
    RootLexicon lexicon = RootLexicon.fromLines("okumak", "gitmek [A:Voicing]");
    ZemberekSpellChecker instance = ZemberekSpellChecker.getInstance(lexicon);
    Assert.assertFalse(instance.isCorrect("okuycam"));
    Assert.assertFalse(instance.isCorrect("gidicem"));
  }

  @Test
  public void testSuggestions1() {
    RootLexicon lexicon = RootLexicon.fromLines("okumak", "gitmek [A:Voicing]");
    ZemberekSpellChecker instance = ZemberekSpellChecker.getInstance(lexicon);
    String[] inputs = {"ou", "ku", "okuu", "oyu"};
    for (String input : inputs) {
      Assert.assertTrue(instance.getSuggestions(input).contains("oku"));
    }
  }

  @Test
  public void testSplit1() {
    RootLexicon lexicon = RootLexicon.fromLines("okumak", "gitmek [A:Voicing]");
    ZemberekSpellChecker instance = ZemberekSpellChecker.getInstance(lexicon);
    Assert.assertTrue(instance.getSuggestions("okudumgittim").contains("okudum gittim"));
    Assert.assertTrue(instance.getSuggestions("giderokuyacak").contains("gider okuyacak"));
  }

  @Test
  public void testInformalWords1() {
    RootLexicon lexicon = RootLexicon.fromLines("okumak", "gitmek [A:Voicing]");
    ZemberekSpellChecker instance = ZemberekSpellChecker.getInstance(lexicon);
    Assert.assertTrue(instance.getSuggestions("okuycam").contains("okuyacağım"));
    Assert.assertTrue(instance.getSuggestions("gidicem").contains("gideceğim"));
  }


}
