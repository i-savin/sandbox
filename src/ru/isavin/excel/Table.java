package ru.isavin.excel;

import java.util.Stack;
import java.util.StringTokenizer;

/**
 * @author ilasavin
 * @since 19.06.15
 */
public class Table {
    private final static String DELIMITER = "+-*/";
    private final static String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private Cell[][] cells;

    public Table(Cell[][] cells) {
        this.cells = cells;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public void evaluate() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                String value;
                try {
                    value = evaluateValue(cells[i][j].getValue());
                } catch (EvaluateException e) {
                    value = "#" + e.getMessage();
                }
                cells[i][j].setValue(value);
            }
        }
    }

    //TODO вычислять ссылки
    private String evaluateValue(String expression) throws EvaluateException {
        if (expression == null || " ".equals(expression)) {
            return " ";
        }
        if (expression.startsWith("'")) {
            return expression.substring(1);
        }
        if (expression.startsWith("=")) {
            try {
                return evaluateExpression(expression);
            } catch (Exception e) {
//                e.printStackTrace();
//                throw new EvaluateException("EXPR_ERR!");
            }
            try {
                return evaluateExpression(evaluateReference(expression.substring(1)));
            } catch (Exception e) {
//                e.printStackTrace();
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

    private String evaluateExpression(String cell) {
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

    private String evaluateReference(String reference) {
        int column = LETTERS.indexOf(reference.charAt(0));
//        System.out.println(column);
        int row = Integer.parseInt(reference.substring(1)) - 1;
//        System.out.println(row);
        return cells[row][column].getValue();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                sb.append(cells[i][j].getValue()).append("\t");
            }
            if (i < cells.length - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}