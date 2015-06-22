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

    private String evaluateExpression(String cell) throws EvaluateException {
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
                        operandStack.add(calculate(op1, op2, fromStackOperation));
                    } else {
                        break;
                    }
                }
                operationStack.add(operation);
            } else {
                try {
                    operandStack.add(Integer.parseInt(token));
                } catch (NumberFormatException e) {
                    operandStack.add(Integer.parseInt(evaluateValue(evaluateReference(token))));
                }
            }
        }
        while (!operationStack.empty()) {
            Operation fromStackOperation = operationStack.pop();
            int op1 = operandStack.pop();
            int op2 = operandStack.pop();
            operandStack.add(calculate(op1, op2, fromStackOperation));
        }
        return operandStack.pop().toString();
    }

    private String evaluateReference(String reference) {
        int column = LETTERS.indexOf(reference.charAt(0));
        int row = Integer.parseInt(reference.substring(1)) - 1;
        return cells[row][column].getValue();
    }

    private int calculate(int op1, int op2, Operation operation) throws EvaluateException {
        switch (operation) {
            case PLUS:
                return op2 + op1;
            case MINUS:
                return op2 - op1;
            case MUL:
                return op2 * op1;
            case DIV:
                return op2 / op1;
        }
        throw new EvaluateException("UNSUPP_OPER");
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
