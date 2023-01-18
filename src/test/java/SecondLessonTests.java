import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.List;


public class SecondLessonTests {

    @Test
    public void testGetJsonHomework() {

        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        List messages = response.get("messages");
        System.out.println(messages.get(1));
    }
}
