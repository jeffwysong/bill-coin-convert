package com.wysong.billcoinconvert.business;

import com.wysong.billcoinconvert.eis.CoinBankRepository;
import com.wysong.billcoinconvert.model.CoinBank;
import com.wysong.billcoinconvert.model.Coins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @since July 24, 2019.
 */
@Component
public class DefaultCoinManager implements CoinManager {

    private final CoinBankRepository coinBankRepository;

    @Autowired
    public DefaultCoinManager(CoinBankRepository coinBankRepository) {
        this.coinBankRepository = coinBankRepository;
    }


    @Override
    public Set<CoinBank> initialize(Integer number) {
        if (number == null) {
            number = CoinBank.DEFAULT_INITIALIZED_NUMBER;
        }
        Set<CoinBank> coinBanks = new HashSet<>();

        //Have to create an "effectively final" integer
        Integer finalNumber = number;
        Arrays.stream(Coins.values()).forEach(coin -> {
            CoinBank coinBank = new CoinBank().setCoinValue(coin.getValue()).setNumAvailable(finalNumber);
            coinBankRepository.save(coinBank);
            coinBanks.add(coinBank);
        });
        return coinBanks;
    }

}
