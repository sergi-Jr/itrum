package ru.itrum.api.exception;

public class WalletException extends RuntimeException {

    public WalletException(Throwable cause) {
        super(cause);
    }

    public WalletException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        if (this.getCause() != null) {
            return this.getCause().getMessage();
        }
        return super.getMessage();
    }

}
