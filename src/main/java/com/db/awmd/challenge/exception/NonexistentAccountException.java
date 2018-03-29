package com.db.awmd.challenge.exception;

public class NonexistentAccountException extends Exception {

    public NonexistentAccountException() {
        super("You've informed nonexistent accounts, please verify.");
    }
}
