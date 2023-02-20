import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
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

    @Test
    public void getHeadersTest() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
       assertEquals("Some secret value", response.getHeader("x-secret-homework-header"),"Unexpected header value");
    }

    private static final String USER_AGENT_CHECK = "https://playground.learnqa.ru/ajax/api/user_agent_check";

    @ParameterizedTest
    @CsvSource(value = {
            "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 | Mobile | No | Android",
            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1 | Mobile | Chrome | iOS",
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html) | Googlebot | Unknown | Unknown",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0 | Web | Chrome | No",
            "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 | Mobile | No | iPhone"},
            delimiter = '|'
    )
    public void testUserAgentCheckTest(String agentAgentValue, String platform, String browser, String device) {

        Map<String, String> userAgent = new HashMap<>();
        userAgent.put("user-agent", agentAgentValue);

        Response response = RestAssured
                .given()
                .headers(userAgent)
                .get(USER_AGENT_CHECK)
                .andReturn();

        assertAll(
                () -> assertEquals(platform, response.jsonPath().getString("platform"), "user-agent: " + agentAgentValue + " \n" + "Unexpected platform"),
                () -> assertEquals(browser, response.jsonPath().getString("browser"), "user-agent: " + agentAgentValue + " \n" + "Unexpected browser"),
                () -> assertEquals(device, response.jsonPath().getString("device"), "user-agent: " + agentAgentValue + " \n" + "Unexpected device")
        );
    }
}
