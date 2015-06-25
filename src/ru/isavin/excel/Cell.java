package ru.isavin.excel;

import java.util.List;

/**
 * @author ilasavin
 * @since 17.06.15
 */
public class Cell {
    /*
     * Запись хранящегося выражения в ПОЛИЗе
     */
    private String value;

    public Cell(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
