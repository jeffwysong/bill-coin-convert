package com.wysong.billcoinconvert.controller;

import com.wysong.billcoinconvert.business.CoinManager;
import com.wysong.billcoinconvert.model.CoinBank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(value = "/initialize", headers = "Accept=application/json")
    public ResponseEntity<Set<CoinBank>> initializeCoins(@RequestBody InitializeBody initBody) {
        Set<CoinBank> coinBanks = coinManager.initialize(initBody.getNumber());
        return new ResponseEntity<>(coinBanks, HttpStatus.CREATED);
    }
}
