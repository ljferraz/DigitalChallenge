package com.db.awmd.challenge.exception;

public class NegativeBalanceException extends Exception {

    public NegativeBalanceException(String accountId) {
        super("Transfer cannot be performed as " + accountId + " will have negative balance and overdraft is not allowed.");
    }
}
