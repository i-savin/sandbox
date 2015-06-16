package ru.isavin.misc;

import java.util.StringTokenizer;

/**
 * @author ilasavin
 * @since 16.06.15
 */
public class StringTokenizerTest {

    public static void main(String[] args) {
        StringTokenizer st = new StringTokenizer("A1+B1*C1/5", "+-*/", true);
        while (st.hasMoreTokens()) {
            System.out.println(st.nextToken());
        }
    }
}
