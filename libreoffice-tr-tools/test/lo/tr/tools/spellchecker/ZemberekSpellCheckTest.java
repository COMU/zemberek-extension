package lo.tr.tools.spellchecker;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import zemberek.morphology.lexicon.RootLexicon;

public class ZemberekSpellCheckTest {

  @Test
  public void testIncorrectWords1() {
    RootLexicon lexicon = getVerbLexicon();
    ZemberekSpellChecker instance = ZemberekSpellChecker.getInstance(lexicon);
    Assert.assertFalse(instance.isCorrect("okuycam"));
    Assert.assertFalse(instance.isCorrect("gidicem"));
  }

  private RootLexicon getVerbLexicon() {
    return RootLexicon.fromLines("okumak", "gitmek [A:Voicing]");
  }

  @Test
  public void testSuggestions1() {
    RootLexicon lexicon = getVerbLexicon();
    ZemberekSpellChecker instance = ZemberekSpellChecker.getInstance(lexicon);
    String[] inputs = {"ou", "ku", "okuu", "oyu"};
    for (String input : inputs) {
      List<String> suggestions = instance.getSuggestions(input);
      Assert.assertTrue(suggestions.contains("oku"));
    }
  }

  @Test
  public void testSplit1() {
    RootLexicon lexicon = getVerbLexicon();
    ZemberekSpellChecker instance = ZemberekSpellChecker.getInstance(lexicon);
    Assert.assertTrue(instance.getSuggestions("okudumgittim").contains("okudum gittim"));
    Assert.assertTrue(instance.getSuggestions("giderokuyacak").contains("gider okuyacak"));
  }

  @Test
  public void testInformalWords1() {
    RootLexicon lexicon = getVerbLexicon();
    ZemberekSpellChecker instance = ZemberekSpellChecker.getInstance(lexicon);
    Assert.assertTrue(instance.getSuggestions("okuycam").contains("okuyacağım"));
    Assert.assertTrue(instance.getSuggestions("gidicem").contains("gideceğim"));
  }

  @Test
  public void testRegularWordWithApostrophe() {
    RootLexicon lexicon = RootLexicon.fromLines("kitap");

    ZemberekSpellChecker instance = ZemberekSpellChecker.getInstance(lexicon);
    Assert.assertTrue(instance.isCorrect("kitapta"));
    Assert.assertTrue(instance.isCorrect("Kitapta"));
    Assert.assertTrue(instance.isCorrect("Kitap'ta"));

    // expect false.
    Assert.assertFalse(instance.isCorrect("Kitap'taa"));
  }

  @Test
  public void testRegularWordWithApostropheSuggestions() {
    RootLexicon lexicon = RootLexicon.fromLines("kitap");

    ZemberekSpellChecker instance = ZemberekSpellChecker.getInstance(lexicon);
    Assert.assertTrue(instance.getSuggestions("ktapta").contains("kitapta"));
    Assert.assertTrue(instance.getSuggestions("Ktapta").contains("Kitapta"));
    Assert.assertTrue(instance.getSuggestions("Ktap'ta").contains("Kitap'ta"));
  }

  @Test
  public void testRegularWordWithApostrophesIssue16() {
    RootLexicon lexicon = RootLexicon.fromLines("laboratuvar");

    ZemberekSpellChecker instance = ZemberekSpellChecker.getInstance(lexicon);
    Assert.assertTrue(instance.isCorrect("laboratuvar"));
    Assert.assertTrue(instance.isCorrect("laboratuvarda"));
    Assert.assertTrue(instance.isCorrect("Laboratuvarda"));
    Assert.assertTrue(instance.isCorrect("Laboratuvar'da"));
  }



}
