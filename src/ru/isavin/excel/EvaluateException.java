package ru.isavin.excel;

/**
 * @author ilasavin
 * @since 17.06.15
 */
public class EvaluateException extends Exception {

    public EvaluateException() {
        super();
    }

    public EvaluateException(String message) {
        super(message);
    }

    public EvaluateException(Throwable th) {
        super(th);
    }
}
