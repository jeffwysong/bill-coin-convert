package com.wysong.billcoinconvert.business;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when the system has no more money to give.
 * This class is never used because requirements ask that the program exit when machine is out of money.
 *
 * A cleaner solution for this REST api would be to throw this error and provide instructions on how to re-initialize
 * the system.
 *
 * @since July 24, 2019.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class OutOfMoneyException extends RuntimeException {

    public OutOfMoneyException() {
        super();
    }

    public OutOfMoneyException(String message) {
        super(message);
    }

    public OutOfMoneyException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutOfMoneyException(Throwable cause) {
        super(cause);
    }
}
