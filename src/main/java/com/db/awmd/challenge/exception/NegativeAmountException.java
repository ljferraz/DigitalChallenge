package com.db.awmd.challenge.exception;

public class NegativeAmountException extends Exception {

    public NegativeAmountException() {
        super("Amount to be transferred must be a positive value, please verify.");
    }
}
