package lo.tr.tools.spellchecker;

public class ZemberekSpellCheckTest {

  public static void main(String[] args) {
    ZemberekSpellCheck zemberekSpellCheckInstance = ZemberekSpellCheck.getInstance();
    System.out.println("is yanlıs correct ?");
    System.out.println(zemberekSpellCheckInstance.isCorrect("yanlıs"));
    System.out.println("succest for yanlıs ");
    System.out.println(zemberekSpellCheckInstance.getSuggestions("yanlıs"));
  }
}
