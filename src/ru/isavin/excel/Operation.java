package ru.isavin.excel;

/**
* @author ilasavin
* @since 17.06.15
*/
enum Operation {
    PLUS("+", 0),
    MINUS("-", 0),
    MUL("*", 0),
    DIV("/", 0);

    private String symbol;
    private int priority;

    Operation(String symbol, int priority) {
        this.symbol = symbol;
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }

    public static Operation fromString(String value) {
        if ("+".equals(value)) {
            return Operation.PLUS;
        }
        if ("-".equals(value)) {
            return Operation.MINUS;
        }
        if ("/".equals(value)) {
            return Operation.DIV;
        }
        if ("*".equals(value)) {
            return Operation.MUL;
        }
        throw new IllegalArgumentException("No enum constant ru.isavin.excel.Parser.Operation." + value);
    }
}
