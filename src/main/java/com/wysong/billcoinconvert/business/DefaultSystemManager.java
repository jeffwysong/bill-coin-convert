package com.wysong.billcoinconvert.business;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @since July 24, 2019.
 */
@Component
public class DefaultSystemManager implements SystemManager {
    @Async
    @Override
    public void shutdownSystemDueToOutOfMoney() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //Do nothing except shutdown sooner.
        }
        System.exit(0);
    }
}
