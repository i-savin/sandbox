package ru.isavin.excel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            return new Table(cells);
        } catch (IOException | NumberFormatException e) {
            throw new ParseException(e);
        }
    }
}
