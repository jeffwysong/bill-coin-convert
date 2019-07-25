package com.wysong.billcoinconvert;

import com.wysong.billcoinconvert.controller.NumberBody;
import com.wysong.billcoinconvert.model.BillSupply;
import com.wysong.billcoinconvert.model.Bills;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
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

        final Response response = initToDefault();
        response.then().assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("numAvailable", not(lessThan(100)))
                .body("numAvailable", not(greaterThan(100)));



        final int NUMBER_TO_CHECK = 30;
        initToNumber(NUMBER_TO_CHECK)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("numAvailable", not(lessThan(NUMBER_TO_CHECK)))
                .body("numAvailable", not(greaterThan(NUMBER_TO_CHECK)));

    }

    @Test
    public void testConversion() {
        //Create bill options
        BillSupply billSupply50 = new BillSupply().setNumProvided(3).setBillValue(Bills.FIFTY.getValue());
        BillSupply billSupply10 = new BillSupply().setNumProvided(3).setBillValue(Bills.TEN.getValue());
        BillSupply billSupply5 = new BillSupply().setNumProvided(3).setBillValue(Bills.FIVE.getValue());
        BillSupply billSupply1 = new BillSupply().setNumProvided(3).setBillValue(Bills.ONE.getValue());

        //reset machine to have $41
        initToDefault();

        //Test change for $18
        Set<BillSupply> billSuppliesCanHandle = new HashSet<>(2);
        billSuppliesCanHandle.add(billSupply1);
        billSuppliesCanHandle.add(billSupply5);


        Response convertResponse = postToCovertEndpoint(billSuppliesCanHandle);
        System.out.println(convertResponse.asString());

        convertResponse.then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("coinValue[0]", equalTo(25))
                .body("numReturned[0]", equalTo(72));

        //check inventory for $23
        assertInventory(28, 100, 100, 100);

        //Test change for $180
        Set<BillSupply> billSuppliesCannotHandle = new HashSet<>(2);
        billSuppliesCannotHandle.add(billSupply50);
        billSuppliesCannotHandle.add(billSupply10);

        postToCovertEndpoint(billSuppliesCannotHandle)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", startsWith("There is not enough money to fulfill your request."));

        //check machine still has $23
        assertInventory(28, 100, 100, 100);

        //Test change for $3
        billSuppliesCanHandle.remove(billSupply5);
        postToCovertEndpoint(billSuppliesCanHandle)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("coinValue[0]", equalTo(25))
                .body("numReturned[0]", equalTo(12));

        //check machine still has $20
        assertInventory(16, 100, 100, 100);

        //Get out $18 more
        Set<BillSupply> billSuppliesEqualTo18DollarsDifferentWay = new HashSet<>();
        BillSupply billSupplyTwoFives = new BillSupply(Bills.FIVE.getValue(), 2);
        BillSupply billSupplyEightOnes = new BillSupply(Bills.ONE.getValue(), 8);
        billSuppliesEqualTo18DollarsDifferentWay.add(billSupplyTwoFives);
        billSuppliesEqualTo18DollarsDifferentWay.add(billSupplyEightOnes);

        postToCovertEndpoint(billSuppliesEqualTo18DollarsDifferentWay)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("coinValue[0]", equalTo(25))
                .body("numReturned[0]", equalTo(16))
                .body("coinValue[1]", equalTo(10))
                .body("numReturned[1]", equalTo(100))
                .body("coinValue[2]", equalTo(5))
                .body("numReturned[2]", equalTo(80));

        //check machine now has $2
        assertInventory(0, 0, 20, 100);

        //Test change for $0
        postToCovertEndpoint(Collections.emptySet())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("", hasSize(0));

        //check machine still has $2
        assertInventory(0, 0, 20, 100);

        given().contentType(ContentType.JSON).body(Collections.emptySet()).when().post("/convert/1")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("coinValue[0]", equalTo(5))
                .body("numReturned[0]", equalTo(20));

        //check machine now has $1
        assertInventory(0, 0, 0, 100);
    }

    private Response initToNumber(Integer numberToInitTo) {
        return given().contentType(ContentType.JSON).body(new NumberBody(numberToInitTo)).when().put("/initialize");
    }

    private Response initToDefault() {
        return initToNumber(null);
    }

    private Response postToCovertEndpoint(Set<BillSupply> billSupplies) {
        return given()
                .contentType(ContentType.JSON)
                .body(billSupplies)
                .log().body()
                .when()
                .post("convert");

    }

    private void assertInventory(int quarters, int dimes, int nickels, int pennies) {
        List<Integer> coinValues = new ArrayList<>(4);
        coinValues.add(1);
        coinValues.add(5);
        coinValues.add(10);
        coinValues.add(25);
        List<Integer> numAvailable = new ArrayList<>(4);
        numAvailable.add(pennies);
        numAvailable.add(nickels);
        numAvailable.add(dimes);
        numAvailable.add(quarters);


        given()
                .when()
                .get("/inventory")
                .then()
                .assertThat()
                .body("coinValue", equalTo(coinValues))
                .body("numAvailable", equalTo(numAvailable));

    }
}
