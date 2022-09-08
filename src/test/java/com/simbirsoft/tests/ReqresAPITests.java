package com.simbirsoft.tests;

import com.simbirsoft.filters.CustomLogFilter;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Feature("Reqres")
@Owner("Alexander Shashkin")
public class ReqresAPITests {

    @Test
    @Tag("reqres_tests")
    @Story("GET-запросы")
    @DisplayName("Проверка возврата общего числа пользователей")
    @AllureId("12147")
    void getListUsersTest() {
        Integer response =
            given().
                    filter(CustomLogFilter.customLogFilter().withCustomTemplates()).
                    log().all().
                    get("https://reqres.in/api/users?page=2").
            then().
                    log().all().
                    extract().path("total");

        assertEquals(12, response);
    }

    @Test
    @Tag("reqres_tests")
    @Story("GET-запросы")
    @DisplayName("Проверка возврата данных пользователя Janet Weaver")
    @AllureId("12148")
    void getSingleUserTest() {
        Response response =
                given().
                        filter(CustomLogFilter.customLogFilter().withCustomTemplates()).
                        log().all().
                        get("https://reqres.in/api/users/2").
                then().
                        log().all().
                        extract().response();

        String name = response.path("data.first_name") + " " + response.path("data.last_name");
        Integer id = response.path("data.id");
        String email = response.path("data.email");

        assertEquals("Janet Weaver", name);
        assertEquals(2, id);
        assertEquals("janet.weaver@reqres.in", email);
    }

    @Test
    @Tag("reqres_tests")
    @Story("GET-запросы")
    @DisplayName("Проверка возврата данных списка ресурсов")
    @AllureId("12149")
    void getResourceList() {
        Response response =
                given().
                        filter(CustomLogFilter.customLogFilter().withCustomTemplates()).
                        log().all().
                        get("https://reqres.in/api/unknown").
                then().
                        log().all().
                        extract().response();

        Integer page = response.path("page");
        Integer per_page = response.path("per_page");
        Integer total = response.path("total");
        Integer total_pages = response.path("total_pages");

        assertEquals(1, page);
        assertEquals(6, per_page);
        assertEquals(12, total);
        assertEquals(2, total_pages);
    }

    @Test
    @Tag("reqres_tests")
    @Story("POST-запросы")
    @DisplayName("Проверка выполнения запроса регистрации пользователя")
    @AllureId("12150")
    void registerUserTest() {
        Map<String, String> jsonBody = new HashMap<>();
        jsonBody.put("email", "eve.holt@reqres.in");
        jsonBody.put("password", "pistol");

        Response response =
        given().
                filter(CustomLogFilter.customLogFilter().withCustomTemplates()).
                log().all().
                header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36").
                contentType(ContentType.JSON).
                body(jsonBody).
        when().
                post("https://reqres.in/api/register").
        then().
                log().all().
                statusCode(200).
                extract().response();

        Integer id = response.path("id");
        String token = response.path("token");

        assertEquals(4, id);
        assertEquals("QpwL5tke4Pnpja7X4", token);
    }

    @Test
    @Tag("reqres_tests")
    @Story("POST-запросы")
    @DisplayName("Проверка выполнения запроса авторизации пользователя")
    @AllureId("12151")
    void loginUserTest() {
        Map<String, String> jsonBody = new HashMap<>();
        jsonBody.put("email", "eve.holt@reqres.in");
        jsonBody.put("password", "cityslicka");

        Response response =
        given().
                filter(CustomLogFilter.customLogFilter().withCustomTemplates()).
                log().all().
                header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                           "(KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36").
                contentType("application/json").
                body(jsonBody).
        when().
                post("https://reqres.in/api/login").
        then().
                log().all().
                statusCode(200).
                extract().response();

        String token = response.path("token");

        assertEquals("QpwL5tke4Pnpja7X4", token);
    }
}