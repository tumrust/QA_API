import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void testLongtimeJob() throws InterruptedException {
        String url = "https://playground.learnqa.ru/ajax/api/longtime_job";
        Map<String, Object> params = new HashMap<>();

        JsonPath createTask = RestAssured
                .get(url)
                .jsonPath();

        params.putAll(createTask.get());

        JsonPath checkStatusNotReady = RestAssured
                .given()
                .queryParam("token", params.get("token"))
                .get(url)
                .jsonPath();

        params.putAll(checkStatusNotReady.get());
        assertEquals("Job is NOT ready", params.get("status"), "Unexpected status");

        int time = (int) params.get("seconds") * 1000;
        Thread.sleep(time);

        JsonPath checkStatusReady = RestAssured
                .given()
                .queryParam("token", params.get("token"))
                .get(url)
                .jsonPath();

        params.putAll(checkStatusReady.get());
        assertEquals("Job is ready", params.get("status"), "Unexpected status");

        JsonPath checkJobDone = RestAssured
                .given()
                .queryParam("token", params.get("token"))
                .get(url)
                .jsonPath();

        params.putAll(checkJobDone.get());
        assertEquals("Job is ready", params.get("status"), "Unexpected status");
        assertEquals("42", params.get("result"), "Unexpected result");
    }

}
