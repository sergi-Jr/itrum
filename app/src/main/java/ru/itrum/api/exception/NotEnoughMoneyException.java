package ru.itrum.api.exception;

public class NotEnoughMoneyException extends WalletException {

    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
