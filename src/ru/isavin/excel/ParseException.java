package ru.isavin.excel;

/**
 * @author ilasavin
 * @since 15.06.15
 */
public class ParseException extends Exception {

    public ParseException() {
        super();
    }

    public ParseException(String message) {
        super(message);
    }

    public ParseException(Throwable th) {
        super(th);
    }
}
