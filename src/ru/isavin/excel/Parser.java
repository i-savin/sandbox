package ru.isavin.excel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * @author ilasavin
 * @since 15.06.15
 */
public class Parser {
    private InputStream is;

    private final static String DELIMITER = "+-*/";

    public Parser(InputStream is) {
        this.is = is;
    }

    public Cell[][] parse() throws ParseException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
            String firstLine = br.readLine();
            Cell[][] table = null;
            int rows = 0;
            int columns = 0;
            if (firstLine != null) {
                String[] dimension = firstLine.split("\t");
                if (dimension.length != 2) {
                    throw new ParseException("Incorrect table size");
                }
                rows = Integer.parseInt(dimension[0]);
                columns = Integer.parseInt(dimension[1]);
                table = new Cell[rows][columns];
            }

            String line = null;
            int rowCount = 0;
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "\t");
                if (st.countTokens() != columns) {
                    throw new ParseException("Incorrect line: " + line);
                }

                for (int i = 0; i < columns; i++) {
                    table[rowCount][i] = new Cell(st.nextToken());
                }
                if (++rowCount == rows) {
                    break;
                }
            }
            return table;
        } catch (IOException | NumberFormatException e) {
            throw new ParseException(e);
        }
    }

    public Cell[][] evaluate(Cell[][] table) {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                String value;
                try {
                    value = evaluateValue(table[i][j].getValue());
                } catch (EvaluateException e) {
                    value = "#" + e.getMessage();
                }
                table[i][j].setValue(value);
            }
        }
        return table;
    }

    private String evaluateValue(String expression) throws EvaluateException {
        if (expression == null || " ".equals(expression)) {
            return " ";
        }
        if (expression.startsWith("'")) {
            return expression.substring(1);
        }
        if (expression.startsWith("=")) {
            try {
                return parseExpression(expression);
            } catch (Exception e) {
                e.printStackTrace();
                throw new EvaluateException("EXPR_ERR!");
            }
        }
        try {
            Integer.parseInt(expression);
            return expression;
        } catch (NumberFormatException e) {
            throw new EvaluateException("NAN!");
        }
    }

    private String parseExpression(String cell) {
        StringTokenizer st = new StringTokenizer(cell.substring(1), DELIMITER, true);
        Stack<Operation> operationStack = new Stack<>();
        Stack<Integer> operandStack = new Stack<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (DELIMITER.contains(token)) {
                Operation operation = Operation.fromString(token);
                while (!operationStack.empty()) {
                    if (operationStack.peek().getPriority() <= operation.getPriority()) {
                        Operation fromStackOperation = operationStack.pop();
                        int op1 = operandStack.pop();
                        int op2 = operandStack.pop();
                        switch (fromStackOperation) {
                            case PLUS:
                                operandStack.add(op2 + op1);
                                break;
                            case MINUS:
                                operandStack.add(op2 - op1);
                                break;
                            case MUL:
                                operandStack.add(op2 * op1);
                                break;
                            case DIV:
                                operandStack.add(op2 / op1);
                                break;
                        }
                    } else {
                        break;
                    }
                }
                operationStack.add(operation);
            } else {
                operandStack.add(Integer.parseInt(token));
            }
        }
        while (!operationStack.empty()) {
            Operation fromStackOperation = operationStack.pop();
            int op1 = operandStack.pop();
            int op2 = operandStack.pop();
            switch (fromStackOperation) {
                case PLUS:
                    operandStack.add(op2 + op1);
                    break;
                case MINUS:
                    operandStack.add(op2 - op1);
                    break;
                case MUL:
                    operandStack.add(op2 * op1);
                    break;
                case DIV:
                    operandStack.add(op2 / op1);
                    break;
            }
        }
        return operandStack.pop().toString();
    }

    private List<String> parseText(String cell) {
        List<String> rpn = new ArrayList<>();
        rpn.add("'");
        rpn.add(cell.substring(1));
        return rpn;
    }

    private List<String> parseEmpty() {
        return new ArrayList<>();
    }

    private List<String> parseNumber(String cell) {
        List<String> rpn = new ArrayList<>();
        rpn.add(cell);
        return rpn;
    }
}
