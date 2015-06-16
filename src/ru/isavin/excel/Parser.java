package ru.isavin.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * @author ilasavin
 * @since 15.06.15
 */
public class Parser {
    enum Operation {
        PLUS("+", 0),
        MINUS("-", 0),
        MUL("*", 0),
        DIV("/", 0),
        EQ("=", 1),
        AP("'", 1);

        private String symbol;
        private int priority;

        Operation(String symbol, int priority) {
            this.symbol = symbol;
            this.priority = priority;
        }

        public int getPriority() {
            return this.priority;
        }

        public static Operation getValue(String value) {
            if ("=".equals(value)) {
                return Operation.EQ;
            }
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
            if ("'".equals(value)) {
                return Operation.AP;
            }
            throw new IllegalArgumentException("No enum constant ru.isavin.excel.Parser.Operation." + value);
        }
    }

    String delimiter = "+-*/";

    public List<String> parse(String cell) throws ParseException {
        Stack<String> stack = new Stack<>();
        StringTokenizer st = new StringTokenizer(cell, delimiter, true);
        List<String> rpn = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            if (delimiter.contains(token)) {
                Operation operation = Operation.getValue(token);
                while (!stack.empty()) {
                    if (Operation.getValue(stack.peek()).getPriority() <= operation.getPriority()) {
                        rpn.add(stack.pop());
                    } else {
                        break;
                    }
                }
                stack.add(token);
            } else {
                rpn.add(token);
            }
        }
        while (!stack.empty()) {
            rpn.add(stack.pop());
        }
        return rpn;
    }
}
