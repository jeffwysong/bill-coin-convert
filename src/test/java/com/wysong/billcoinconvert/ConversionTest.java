package com.wysong.billcoinconvert;

import com.wysong.billcoinconvert.controller.InitializeBody;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;


import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * **Problem**
 *
 * Given an amount in bills that can be $(1, 2, 5, 10, 20, 50, 100) change it to coins that are cents(1, 5, 10, 25). The machine needs to assume there is a finite number of coins.
 *
 *
 *
 * **Requirements**
 *
 * - Start with 100 coins of each
 *
 * - Machine should return the least amount of coins possible
 *
 * - Machine should display a message it does not have enough coins
 *
 * - Machine should maintain state (Coins left) throughout all the transactions till it runs out of coin, then it should exit.
 *
 * - Code should follow good coding practices and include tests
 *
 *
 * @since July 24, 2019.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConversionTest {

    @LocalServerPort
    int testingPort;

    @Before
    public void setUp() {
        RestAssured.port = testingPort;
    }

    @Test
    public void testInitialState() {

        final Response response = given().contentType(ContentType.JSON).body(new InitializeBody()).when().post("/initialize");
        response.then().assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("numAvailable", not(lessThan(100)))
                .body("numAvailable", not(greaterThan(100)));



        final int NUMBER_TO_CHECK = 30;
        given()
                .contentType(ContentType.JSON)
                .body(new InitializeBody(NUMBER_TO_CHECK))
                .when()
                .post("/initialize")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("numAvailable", not(lessThan(NUMBER_TO_CHECK)))
                .body("numAvailable", not(greaterThan(NUMBER_TO_CHECK)));

    }
}
