package ru.isavin.excel;

/**
 * @author ilasavin
 * @since 17.06.15
 */
public class Excel {

    public static void main(String[] args) throws ParseException {
        System.out.println("Enter your data");
        Parser parser = new Parser(System.in);
        Cell[][] cells = parser.parse();
        System.out.println("Your sheet:");
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                System.out.print(cells[i][j].getValue() + "\t");
            }
            System.out.println();
        }

        cells = parser.evaluate(cells);
        System.out.println("Evaluated sheet:");
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                System.out.print(cells[i][j].getValue() + "\t");
            }
            System.out.println();
        }
    }
}
