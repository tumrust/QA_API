import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

import static org.apache.http.client.methods.RequestBuilder.post;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecondLessonTests {

    @Test
    public void testGetJsonHomework() {
        Map<String, String> messages = new HashMap<>();

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

    @Test
    public void passwordSelection() {
        String getSecretPassword = "https://playground.learnqa.ru/ajax/api/get_secret_password_homework";
        String checkCookie = "https://playground.learnqa.ru/ajax/api/check_auth_cookie";

        Map<String, String> data = new HashMap<>();
        data.put("login", "super_admin");
        data.put("password", "secret_password");

        String[] passwords = new String[]{ "1234", "solo", "12345", "admin", "hello", "jesus", "login", "ninja", "000000", "123qwe", "111111", "121212", "123123", "123456", "555555", "654321", "666666", "696969", "888888", "abc123", "access", "ashley", "azerty", "batman", "donald", "dragon", "flower", "hottie", "lovely", "loveme", "master", "monkey", "qazwsx", "qwerty", "shadow", "1234567", "7777777", "charlie", "freedom", "letmein", "michael", "mustang", "welcome", "1q2w3e4r", "1qaz2wsx", "12345678", "!@#$%^&*", "aa123456", "adobe123", "baseball", "football", "iloveyou", "passw0rd", "password", "princess", "starwars", "sunshine", "superman", "trustno1", "whatever", "zaq1zaq1", "123456789", "password1", "photoshop", "qwerty123", "1234567890", "qwertyuiop", "12345", "111111", "123123", "654321", "abc123", "ashley", "bailey", "dragon", "master", "monkey", "qazwsx", "qwerty", "shadow", "1234567", "letmein", "michael", "Football", "baseball", "iloveyou", "passw0rd", "sunshine", "superman", "trustno1", "123456789"};


        for (int i = 0; i < passwords.length; i++) {
            data.put("login", "super_admin");
            data.put("password", passwords[i]);

            Response checkPassword = RestAssured
                    .given()
                    .body(data)
                    .post(getSecretPassword)
                    .andReturn();

            String auth_cookie = checkPassword.getCookie("auth_cookie");

            Response checkAuth = RestAssured
                    .given()
                    .cookie(auth_cookie)
                    .get(checkCookie)
                    .andReturn();
            System.out.println(data.get("password") + checkAuth.getBody().asString());
            if (checkAuth.body().asString().equals("You are authorized"))
                break;
        }
    }
}
