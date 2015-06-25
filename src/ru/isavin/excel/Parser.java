package ru.isavin.excel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author ilasavin
 * @since 15.06.15
 */
public class Parser {
    private InputStream is;

    public Parser(InputStream is) {
        this.is = is;
    }

    public Table parse() throws ParseException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
            String firstLine = br.readLine();
            Cell[][] cells = null;
            int rows = 0;
            int columns = 0;
            if (firstLine != null) {
                String[] dimension = firstLine.split("\t");
                if (dimension.length != 2) {
                    throw new ParseException("Incorrect table size");
                }
                rows = Integer.parseInt(dimension[0]);
                columns = Integer.parseInt(dimension[1]);
                cells = new Cell[rows][columns];
            }

            String line = null;
            int rowCount = 0;
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "\t");
                if (st.countTokens() != columns) {
                    throw new ParseException("Incorrect line: " + line);
                }

                for (int i = 0; i < columns; i++) {
                    cells[rowCount][i] = new Cell(st.nextToken());
                }
                if (++rowCount == rows) {
                    break;
                }
            }
//            return new Table(cells);
            return null;
        } catch (IOException | NumberFormatException e) {
            throw new ParseException(e);
        }
    }

    public Map<String, Cell> parse2() throws ParseException {
        Map<String, Cell> table = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
            String firstLine = br.readLine();
            Cell[][] cells = null;
            int rows = 0;
            int columns = 0;
            if (firstLine != null) {
                String[] dimension = firstLine.split("\t");
                if (dimension.length != 2) {
                    throw new ParseException("Incorrect table size");
                }
                rows = Integer.parseInt(dimension[0]);
                columns = Integer.parseInt(dimension[1]);
                cells = new Cell[rows][columns];
            }

            String line = null;
            int rowCount = 0;
            int columnCount = 0;
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "\t");
                if (st.countTokens() != columns) {
                    throw new ParseException("Incorrect line: " + line);
                }

                for (int i = 0; i < columns; i++) {
                    String index = getLetterIndex(i) + (rowCount + 1);
                    table.put(index, new Cell(st.nextToken()));
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

    String getLetterIndex(int i) {
        StringBuffer sb = new StringBuffer();
        while (i > Table.LETTERS.length()) {
            int x = i / Table.LETTERS.length();//целая часть
            sb.append(Table.LETTERS.charAt(i-x*Table.LETTERS.length()));
            i = x;
        }
        sb.append(Table.LETTERS.charAt(i));
        return sb.reverse().toString();
    }
}
