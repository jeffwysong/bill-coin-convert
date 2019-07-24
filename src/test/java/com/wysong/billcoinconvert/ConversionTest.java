package com.wysong.billcoinconvert;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
@SpringBootTest
public class ConversionTest {

    @Test
    public void testInitialState() {
        given().params(Collections.emptyMap()).when().post("/initialize").then().assertThat().body("*", equalTo(100));


        final int NUMBER_TO_CHECK = 30;
        given().params("number", NUMBER_TO_CHECK).when().post("/initialize").then().assertThat().body("*", equalTo(NUMBER_TO_CHECK));

    }
}
