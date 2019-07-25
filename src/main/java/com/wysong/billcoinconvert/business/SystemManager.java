package com.wysong.billcoinconvert.business;

/**
 * This manager exists only because the requirements stated the "machine" should exit once it runs out of coins.
 * I would never implement anything like this in production.
 *
 * @since July 24, 2019.
 */
public interface SystemManager {
    /**
     * Sleeps for one second so the original response can be sent successfully before the system exits.
     *
     */
    void shutdownSystemDueToOutOfMoney();
}
