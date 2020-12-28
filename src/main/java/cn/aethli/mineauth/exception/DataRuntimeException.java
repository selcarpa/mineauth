package cn.aethli.mineauth.exception;

public class DataRuntimeException extends RuntimeException {
    public DataRuntimeException() {
    }

    public DataRuntimeException(String message) {
        super(message);
    }

    public DataRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataRuntimeException(Throwable cause) {
        super(cause);
    }

    public DataRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
