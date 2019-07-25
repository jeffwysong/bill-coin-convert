package com.wysong.billcoinconvert.business;

import com.wysong.billcoinconvert.eis.CoinBankRepository;
import com.wysong.billcoinconvert.model.BillSupply;
import com.wysong.billcoinconvert.model.CoinBank;
import com.wysong.billcoinconvert.model.CoinReturn;
import com.wysong.billcoinconvert.model.Coins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @since July 24, 2019.
 */
@Component
public class DefaultCoinManager implements CoinManager {

    private final CoinBankRepository coinBankRepository;
    private final SystemManager systemManager;

    @Autowired
    public DefaultCoinManager(CoinBankRepository coinBankRepository,
                              SystemManager systemManager) {
        this.coinBankRepository = coinBankRepository;
        this.systemManager = systemManager;
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

    @Override
    public Set<CoinReturn> convertBills(Set<BillSupply> bills) {
        if (CollectionUtils.isEmpty(bills)) {
            return Collections.emptySet();
        }

        int totalAmount = getTotalNeeded(bills);
        return convertBills(totalAmount);
    }

    @Override
    public Set<CoinReturn> convertBills(int cents) {
        sanityCheck(cents);

        Set<CoinReturn> coinsReturned = new HashSet<>();
        Map<Integer, CoinBank> valueCoinBankMap = createMap();

        for (Coins coin : Coins.values()) {
            int numOfNeededCoins = cents / coin.getValue();
            if (numOfNeededCoins == 0) {
                continue;
            }

            CoinBank coinBank = valueCoinBankMap.get(coin.getValue());
            int numToReturn = Math.min(numOfNeededCoins, coinBank.getNumAvailable());
            updateCoinBank(coinBank, numToReturn);

            cents = cents - (coin.getValue() * numToReturn);

            CoinReturn returned = new CoinReturn().setCoinValue(coinBank.getCoinValue()).setNumReturned(numToReturn);
            coinsReturned.add(returned);

            if (cents == 0) {
                break;
            }

        }

        checkIfBrokeTheBank();

        return coinsReturned;
    }

    private void updateCoinBank(CoinBank coinBank, int numToReturn) {
            int numLeft = coinBank.getNumAvailable() - numToReturn;
            coinBank.setNumAvailable(numLeft);
            coinBankRepository.save(coinBank);
    }

    private Map<Integer, CoinBank> createMap() {
        Iterable<CoinBank> allCoins = coinBankRepository.findAll();
        Map<Integer, CoinBank> valueCoinBankMap = new HashMap<>();
        for (CoinBank coinBank : allCoins) {
            valueCoinBankMap.put(coinBank.getCoinValue(), coinBank);
        }
        return valueCoinBankMap;
    }

    private int getTotalNeeded(Set<BillSupply> billSupplies) {
        return billSupplies.stream()
                .map(billSupply -> billSupply.getBillValue() * billSupply.getNumProvided() * 100)
                .reduce(0, Integer::sum);
    }

    private void sanityCheck(int amountDesiredToWithdraw) {
        int totalInSystem = getAmountInSystem();
        if (totalInSystem < amountDesiredToWithdraw) {
            throw new NotEnoughMoneyException();
        }
    }

    private void checkIfBrokeTheBank() {
        int totalInSystem = getAmountInSystem();
        if (totalInSystem == 0) {
            systemManager.shutdownSystemDueToOutOfMoney();
        }
    }

    private int getAmountInSystem() {
        int totalInSystem = 0;
        final Iterable<CoinBank> all = coinBankRepository.findAll();
        for (CoinBank coinBank : all) {
            totalInSystem = totalInSystem + (coinBank.getCoinValue() * coinBank.getNumAvailable());
        }
        return totalInSystem;
    }

    @Override
    public Collection<CoinBank> getInventory() {
        return (Collection<CoinBank>) coinBankRepository.findAll();
    }

}
