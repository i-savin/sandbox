package ru.isavin.excel;

/**
 * @author ilasavin
 * @since 15.06.15
 */
public class ExpressionValue extends Value {

    private String value;

    public ExpressionValue(String value) {
        this.value = value;
    }

    public String getValue() {
        //TODO evaluate expression here
        return value;
    }
}
