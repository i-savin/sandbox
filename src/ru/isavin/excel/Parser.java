package ru.isavin.excel;

/**
 * @author ilasavin
 * @since 15.06.15
 */
public class Parser {

    public String parse(String cell) throws Exception {
        if (cell == null || "".equals(cell)) {
            return "Empty!";
        }

        if (cell.startsWith("'")) {
            return "Text!";
        }

        if (cell.startsWith("=")) {
            return "Expression!";
        }

        try {
            Integer.parseInt(cell);
            return "Number!";
        } catch (NumberFormatException e) {
            //TODO  сделать собственный класс исключений
            throw new Exception();
        }
    }
}
