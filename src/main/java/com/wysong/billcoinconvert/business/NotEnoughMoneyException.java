package com.wysong.billcoinconvert.business;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @since July 24, 2019.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class NotEnoughMoneyException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "There is not enough money to fulfill your request.  Please check /inventory endpoint for remaining amount";

    public NotEnoughMoneyException() {
        super(DEFAULT_MESSAGE);
    }

}
