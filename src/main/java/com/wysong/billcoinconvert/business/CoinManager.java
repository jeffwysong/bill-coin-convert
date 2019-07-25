package com.wysong.billcoinconvert.business;

import com.wysong.billcoinconvert.model.BillSupply;
import com.wysong.billcoinconvert.model.CoinBank;
import com.wysong.billcoinconvert.model.CoinReturn;

import java.util.Collection;
import java.util.Set;

/**
 * @since July 24, 2019.
 */
public interface CoinManager {
    Set<CoinBank> initialize(Integer number);

    Set<CoinReturn> convertBills(Set<BillSupply> bills);

    Set<CoinReturn> convertBills(int cents);

    Collection<CoinBank> getInventory();
}
