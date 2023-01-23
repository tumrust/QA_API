import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThirdLessonsTests {

    @Test
    public void shortPhraseTest() {
    String phrase = new String();

    assertTrue(phrase.length() >= 15,"The phrase is shorter than 15 symbols");
    }
}
