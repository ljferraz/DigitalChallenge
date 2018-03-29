package com.db.awmd.challenge.exception;

public class SameAccountException extends Exception {

    public SameAccountException() {
        super("Accounts informed are the same, please verify.");
    }
}
