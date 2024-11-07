package com.springboot.productclient.exception;

public class RetryExhaustedException extends RuntimeException {
    public RetryExhaustedException(String messgae) {
        super(messgae);
    }
}
