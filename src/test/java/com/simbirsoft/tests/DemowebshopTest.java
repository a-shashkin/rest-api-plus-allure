package com.simbirsoft.tests;

import com.simbirsoft.filters.CustomLogFilter;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Feature("Demowebshop")
@Owner("Alexander Shashkin")
public class DemowebshopTest {

    @Test
    @Tag("demowebshop_tests")
    @Story("Корзина товаров")
    @DisplayName("Проверка добавления товара в корзину")
    @AllureId("12152")
    void addToEmptyCartTest() {
        Response response =
        given().
                filter(CustomLogFilter.customLogFilter().withCustomTemplates()).
                log().all().
                contentType("application/x-www-form-urlencoded; charset=UTF-8").
                header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                        " Chrome/105.0.0.0 Safari/537.36").
                body("product_attribute_74_5_26=81&product_attribute_74_6_27=83&product_attribute_74_3_28=86&addtocart_74.EnteredQuantity=1").
        when().
                post("https://demowebshop.tricentis.com/addproducttocart/details/74/1").
        then().
                log().all().
                statusCode(200).
                extract().response();

        String message = response.path("message");
        Boolean success = response.path("success");
        String updatetopcartsectionhtml = response.path("updatetopcartsectionhtml");

        assertEquals("The product has been added to your <a href=\"/cart\">shopping cart</a>", message);
        assertEquals(true, success);
        assertEquals("(1)", updatetopcartsectionhtml);
    }

    @Test
    @Tag("demowebshop_tests")
    @Story("Голосование")
    @DisplayName("Проверка невозможности голосования без регистрации")
    @AllureId("12156")
    void unregisteredUserVote() {
        given().
                filter(CustomLogFilter.customLogFilter().withCustomTemplates()).
                log().all().
                contentType("application/x-www-form-urlencoded; charset=UTF-8").
                header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                        " Chrome/105.0.0.0 Safari/537.36").
                body("pollAnswerId=1").
        when().
                post("https://demowebshop.tricentis.com/poll/vote").
        then().
                log().all().
                statusCode(200).
                body("error", is("Only registered users can vote."));
    }

    @Test
    @Tag("demowebshop_tests")
    @Story("Корзина товаров")
    @DisplayName("Проверка удаления товара из корзины")
    @AllureId("12155")
    void removeFromCartTest() {
        given().
                filter(CustomLogFilter.customLogFilter().withCustomTemplates()).
                log().all().
                contentType("multipart/form-data; boundary=----WebKitFormBoundaryufsLp4KPpbjV0c4E").
                header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                        " Chrome/105.0.0.0 Safari/537.36").
                config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT))).
                body("------WebKitFormBoundaryufsLp4KPpbjV0c4E\n" +
                        "Content-Disposition: form-data; name=\"removefromcart\"\n" +
                        "\n" +
                        "2642767\n" +
                        "------WebKitFormBoundaryufsLp4KPpbjV0c4E\n" +
                        "Content-Disposition: form-data; name=\"itemquantity2642767\"\n" +
                        "\n" +
                        "1\n" +
                        "------WebKitFormBoundaryufsLp4KPpbjV0c4E\n" +
                        "Content-Disposition: form-data; name=\"updatecart\"\n" +
                        "\n" +
                        "Update shopping cart\n" +
                        "------WebKitFormBoundaryufsLp4KPpbjV0c4E\n" +
                        "Content-Disposition: form-data; name=\"discountcouponcode\"\n" +
                        "\n" +
                        "\n" +
                        "------WebKitFormBoundaryufsLp4KPpbjV0c4E\n" +
                        "Content-Disposition: form-data; name=\"giftcardcouponcode\"\n" +
                        "\n" +
                        "\n" +
                        "------WebKitFormBoundaryufsLp4KPpbjV0c4E\n" +
                        "Content-Disposition: form-data; name=\"CountryId\"\n" +
                        "\n" +
                        "0\n" +
                        "------WebKitFormBoundaryufsLp4KPpbjV0c4E\n" +
                        "Content-Disposition: form-data; name=\"StateProvinceId\"\n" +
                        "\n" +
                        "0\n" +
                        "------WebKitFormBoundaryufsLp4KPpbjV0c4E\n" +
                        "Content-Disposition: form-data; name=\"ZipPostalCode\"\n" +
                        "\n" +
                        "\n" +
                        "------WebKitFormBoundaryufsLp4KPpbjV0c4E--").
                when().
                post("https://demowebshop.tricentis.com/cart").
                then().
                log().all().
                statusCode(200);
    }
}
