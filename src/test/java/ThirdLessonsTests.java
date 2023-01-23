import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThirdLessonsTests {

    @Test
    public void shortPhraseTest() {
    String phrase = new String();

    assertTrue(phrase.length() >= 15,"The phrase is shorter than 15 symbols");
    }

    @Test
    public void getCookieTest() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        assertEquals("hw_value",response.getCookie("HomeWork"),"Unexpected cookie value");
    }
}
