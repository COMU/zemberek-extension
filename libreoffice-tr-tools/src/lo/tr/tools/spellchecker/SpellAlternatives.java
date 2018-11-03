package lo.tr.tools.spellchecker;

import com.sun.star.lang.Locale;

public class SpellAlternatives implements
    com.sun.star.linguistic2.XSpellAlternatives {

  private final short failureType;          // type of failure
  private String word;
  private Locale language;
  private String[] alternatives;           // list of alternatives, may be empty.

  public SpellAlternatives(
      String word,
      Locale language,
      short failureType,
      String[] alternatives) {
    this.word = word;
    this.language = language;
    this.alternatives = alternatives;
    this.failureType = failureType;

    // defensive coding.

    if (this.word == null) {
      this.word = "";
    }
    if (this.language == null) {
      this.language = new Locale();
    }

    if (this.alternatives == null) {
      this.alternatives = TurkishSpellChecker.EMPTY_STRING_ARRAY;
    }
  }

  // XSpellAlternatives
  public String getWord() throws com.sun.star.uno.RuntimeException {
    return word;
  }

  public Locale getLocale() throws com.sun.star.uno.RuntimeException {
    return language;
  }

  public short getFailureType() throws com.sun.star.uno.RuntimeException {
    return failureType;
  }

  public short getAlternativesCount() throws com.sun.star.uno.RuntimeException {
    return (short) alternatives.length;
  }

  public String[] getAlternatives() throws com.sun.star.uno.RuntimeException {
    return alternatives;
  }
}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
