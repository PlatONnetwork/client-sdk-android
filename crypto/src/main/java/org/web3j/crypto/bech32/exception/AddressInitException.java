package org.web3j.crypto.bech32.exception;

public class AddressInitException extends Exception {

    public AddressInitException(String message) {
        super(message);
    }

    public AddressInitException(Throwable cause) {
        super(cause);
    }

    public AddressInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
