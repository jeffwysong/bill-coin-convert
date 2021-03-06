package com.wysong.billcoinconvert.controller;

import com.wysong.billcoinconvert.business.CoinManager;
import com.wysong.billcoinconvert.model.BillSupply;
import com.wysong.billcoinconvert.model.CoinBank;
import com.wysong.billcoinconvert.model.CoinReturn;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

/**
 * @since July 24, 2019.
 */
@RestController
public class ConversionController {
    private final CoinManager coinManager;

    @Autowired
    public ConversionController(CoinManager coinManager) {
        this.coinManager = coinManager;
    }

    /**
     * Convenience endpoint to re-initialize the machine's coins to the number provided in the NumberBody.
     *
     * @param initBody the {@link NumberBody} that will define how many of each coin set.
     * @return the entire contents of the machine
     */
    @ApiOperation(value = "Re-initializes all the machine's coins to the number provided.",
                code = 201)
    @PutMapping(value = "/initialize", headers = "Accept=application/json")
    public ResponseEntity<Set<CoinBank>> initializeCoins(@RequestBody @Valid NumberBody initBody) {
        Set<CoinBank> coinBanks = coinManager.initialize(initBody.getNumber());
        return new ResponseEntity<>(coinBanks, HttpStatus.CREATED);
    }

    /**
     * Endpoint to check the contents of the machine.
     *
     * @return the entire contents of the machine
     */
    @ApiOperation(value = "Returns the current inventory of the machine.")
    @GetMapping(value = "/inventory")
    public ResponseEntity<Collection<CoinBank>> getInventory() {
        Collection<CoinBank> coinBanks = coinManager.getInventory();
        return new ResponseEntity<>(coinBanks, HttpStatus.OK);
    }

    @ApiOperation(value = "Converts provided bill values into coins.")
    @PostMapping(value = "/convert", headers = "Accept=application/json")
    public ResponseEntity<Set<CoinReturn>> convertToCoins(@RequestBody Set<BillSupply> billSupplies) {
        Set<CoinReturn> coinsReturned = coinManager.convertBills(billSupplies);
        return new ResponseEntity<>(coinsReturned, HttpStatus.OK);
    }

    @ApiOperation(value = "Convenience method to convert provided number of dollars to coins.")
    @PostMapping(value = "/convert/{dollars}")
    public ResponseEntity<Set<CoinReturn>> simpleConvertToCoins(@PathVariable int dollars) {
        Set<CoinReturn> coinsReturned = coinManager.convertBills(dollars * 100);
        return new ResponseEntity<>(coinsReturned, HttpStatus.OK);

    }

}
