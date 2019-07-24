package com.wysong.billcoinconvert.eis;

import com.wysong.billcoinconvert.model.CoinBank;
import org.springframework.data.repository.CrudRepository;

/**
 * @since July 24, 2019.
 */
public interface CoinBankRepository extends CrudRepository<CoinBank, Integer> {
}
