package org.web3j.crypto.bech32.exception;

public class AddressFormatException extends Exception {


    public AddressFormatException(String message) {
        super(message);
    }

    public AddressFormatException(Throwable cause) {
        super(cause);
    }

    public AddressFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
