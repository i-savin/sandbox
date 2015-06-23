package ru.isavin.excel;

import java.util.Stack;
import java.util.StringTokenizer;

/**
 * @author ilasavin
 * @since 19.06.15
 */
public class Table {
    /*
     * Строка, содержащая все операции в таблице
     */
    private final static String DELIMITER = Operation.getOperationsString();    //"+-*/";
    /*
     * Строка, содеражщая буквы английского алфавита для ссылок на ячейки таблицы
     */
    private final static String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /*
     * Массив ячеек
     */
    private Cell[][] cells;
    /*
     * Глубина рекурсии при вычислении ссылок на ячейки - общая длина "пути" ссылки не может
     * превышать величины произведения сторон таблицы
     */
    private int referenceDeep;

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
        /*
         * Ссылка
         */
        if (expression.matches("=[A-Za-z][0-9]+")) {
            return evaluateValue(evaluateReference(expression.substring(1)));
        }

        /*
         * Если не ссылка, нужно обнулить глубину рекурсии, чтобы она не
         * учитывалась в следующих вычисления ссылок
         */
        if (referenceDeep != 0) {
            referenceDeep = 0;
        }

        /*
         * Если в таблицу уже была записана ошибка
         */
        if (expression.startsWith("#")) {
            return expression;
        }

        /*
         * Пустая ячейка
         */
        if (" ".equals(expression)) {
            return " ";
        }

        /*
         * Текстовая ячейка
         */
        if (expression.startsWith("'")) {
            return expression.substring(1);
        }

        /*
         * Ячейка-выражение
         */
        if (expression.startsWith("=")) {
            try {
                return evaluateExpression(expression);
            } catch (Exception e) {
                throw new EvaluateException("EXPR_ERR!");
            }
        }

        /*
         * Если ничего из вышеперечисленного не подходит, пробуем
         * распарсить число, если не получается - ParseException
         */
        try {
            Integer.parseInt(expression);
            return expression;
        } catch (NumberFormatException e) {
            throw new EvaluateException("NAN!");
        }
    }

    private String evaluateExpression(String cell) throws EvaluateException {
        StringTokenizer st = new StringTokenizer(cell.substring(1), DELIMITER, true);
        /*
         * Стек операций
         */
        Stack<Operation> operationStack = new Stack<>();
        /*
         * Стек операндов
         */
        Stack<Integer> operandStack = new Stack<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            //если токен - арифметическая операция
            if (DELIMITER.contains(token)) {
                Operation operation = Operation.fromString(token);
                /*
                 * из стека операций выталкиваем все операции с меньшим приоритетом и
                 * вычисляем результат этих операций последовательно для
                 * двух верхних операндов в стеке операндов, а результат
                 * кладем обратно в стек операндов
                 */
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
                //операцию кладем в стек операций
                operationStack.add(operation);
            } else {
                try {
                    //токен-число парсим и кладем с стек операндов
                    operandStack.add(Integer.parseInt(token));
                } catch (NumberFormatException e) {
                    try {
                        //если распарсить не удалось, вычисляем ссылку и кладем результат в стек-операций
                        operandStack.add(Integer.parseInt(evaluateValue(evaluateReference(token))));
                    } catch (NumberFormatException nfe) {
                        throw new EvaluateException("NAN!");
                    }
                }
            }
        }
        /*
         * Теперь последовательно берем верхний элемент из стека операций
         * и два верхних элемента из стека операндов,
         * вычисляем выражение, резльтат кладем в стек операндов и так далее,
         * пока стек не опустеет
         */
        while (!operationStack.empty()) {
            Operation fromStackOperation = operationStack.pop();
            int op1 = operandStack.pop();
            int op2 = operandStack.pop();
            operandStack.add(calculate(op1, op2, fromStackOperation));
        }
        return operandStack.pop().toString();
    }

    private String evaluateReference(String reference) throws EvaluateException {
        //если глубина ссылки превысила произведение сторон таблицы,
        // то это циклическая ссылка
        if (referenceDeep > cells.length * cells[0].length) {
            referenceDeep = 0;
            throw new EvaluateException("CYCLIC_REF!");
        }
        int column = LETTERS.indexOf(reference.charAt(0));
        int row = Integer.parseInt(reference.substring(1)) - 1;
        referenceDeep++;
        return cells[row][column].getValue();
    }

    /*
     * Вычисление выражения из двух операндов и операции
     */
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
        throw new EvaluateException("WRONG_OPER!");
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
