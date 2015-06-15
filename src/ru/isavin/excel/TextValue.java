package ru.isavin.excel;

/**
 * @author ilasavin
 * @since 15.06.15
 */
public class TextValue extends Value {

    private String value;

    public TextValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
