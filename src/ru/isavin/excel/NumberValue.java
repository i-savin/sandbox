package ru.isavin.excel;

/**
 * @author ilasavin
 * @since 15.06.15
 */
public class NumberValue extends Value {

    private int value;

    public NumberValue(String value) {
        this.value = Integer.parseInt(value);
    }

    public String getValue() {
        return String.valueOf(value);
    }
}
