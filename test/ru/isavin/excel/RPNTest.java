package ru.isavin.excel;

import org.junit.Test;

/**
 * @author ilasavin
 * @since 16.06.15
 */
public class RPNTest {

    @Test
    public void simpleTest() throws ParseException {
        Parser parser = new Parser();
        System.out.println(parser.parse("=A1+B1*C1/5"));
        System.out.println(parser.parse("1+2*4+3"));
        System.out.println(parser.parse("'jojo"));
    }
}
