import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

public class SecondLessonTests {

    @Test
    public void testGetJsonHomework() {
        Map<String ,String> messages = new HashMap<>();

        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        messages = response.get("messages[1]");
        System.out.println(messages);
    }

    @Test
    public void testRedirect() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String location = response.getHeader("Location");
        System.out.println(location);
    }

    @Test
    public void testLongRedirect() {
        String url = "https://playground.learnqa.ru/api/long_redirect";
        for (; ; ) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(url)
                    .andReturn();
            Headers headers = response.getHeaders();
            int statusCode = response.statusCode();
            url = response.getHeader("Location");
            System.out.println(statusCode + " " + url + " " + headers + "\n");
            if (url == null)
                break;
        }
    }
}
