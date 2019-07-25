package com.wysong.billcoinconvert;

import com.wysong.billcoinconvert.business.CoinManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @since July 24, 2019.
 */
@Component
public class DataLoader implements ApplicationRunner {

    private final CoinManager coinManager;

    @Autowired
    public DataLoader(CoinManager coinManager) {
        this.coinManager = coinManager;
    }


    @Override
    public void run(ApplicationArguments args) {
        coinManager.initialize(null);
    }
}
