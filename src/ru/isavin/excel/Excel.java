package ru.isavin.excel;

import java.util.Map;

/**
 * @author ilasavin
 * @since 17.06.15
 */
public class Excel {

    public static void main(String[] args) throws ParseException {
        System.out.println("Enter your data");
        Parser parser = new Parser(System.in);
        Map<String, Cell> stringCellMap = parser.parse2();
//        Table table = parser.parse();
        System.out.println("Your sheet:");
        System.out.println(stringCellMap);


//        table.evaluate();
//        System.out.println("Evaluated sheet:");
//        System.out.println(table);
    }
}
