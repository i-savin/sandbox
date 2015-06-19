package ru.isavin.excel;

/**
 * @author ilasavin
 * @since 17.06.15
 */
public class Excel {

    public static void main(String[] args) throws ParseException {
        System.out.println("Enter your data");
        Parser parser = new Parser(System.in);
        Table table = parser.parse();
        System.out.println("Your sheet:");
        System.out.println(table);


        table.evaluate();
        System.out.println("Evaluated sheet:");
        System.out.println(table);
    }
}
