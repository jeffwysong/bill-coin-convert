package com.wysong.billcoinconvert.business;

import com.wysong.billcoinconvert.model.CoinBank;

import java.util.Set;

/**
 * @since July 24, 2019.
 */
public interface CoinManager {
    Set<CoinBank> initialize(Integer number);
}
