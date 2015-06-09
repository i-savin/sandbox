package ru.isavin.grep;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Необходимо реализовать консольную программу, которая бы фильтровала поток текстовой информации подаваемой на вход
 * и на выходе показывала лишь те строчки, которые содержат слово передаваемое программе на вход в качестве аргумента.
 * Варианты усложнения:
 *  Программа не должна учитывать регистр
 *  В аргументах может быть передано не одно слово, а несколько
 *  В качестве аргумента может быть задано не конкретное слово, а регулярное выражение
 *
 * @author ilasavin
 * @since 05.06.15
 */
public class MyGrep {

    private InputStream inputStream;
    /**
     * true, если поиск происходит по регулярному выражению
     */
    private boolean regexp;
    /**
     * true, если поиск должен быть регистрозависимым
     */
    private boolean caseSensitive;
    /**
     * true, если в строке должны найтись непременно ВСЕ шаблоны поиска,
     *                         иначе найтись должен хотя бы один
     */
    private boolean allPatterns;

    public static void main(String[] args) {
        MyGrep myGrep = null;
        String pattern = null;

        if (args.length == 1) {
            myGrep = new MyGrep();
            pattern = args[0];
        } else if (args.length >= 2) {
            File file = new File(args[0]);
            try {
                file = new File(args[0]);
                myGrep = new MyGrep(file);
                pattern = args[1];
            } catch (FileNotFoundException e) {
                System.err.println("MyGrep: " + file + ": no such file or directory");
                System.exit(-2);
            }
        } else {
            System.err.println("usage: MyGrep [file] [pattern] [-c] [-a] [-r]");
            System.exit(-1);
        }
        boolean caseSensitive = false;
        boolean allPatterns = false;
        boolean isRegexp = false;

        for (String arg : args) {
            if ("-c".equals(arg)) {
                caseSensitive = true;
            }
            if ("-a".equals(arg)) {
                allPatterns = true;
            }
            if ("-r".equals(arg)) {
                isRegexp = true;
            }
        }
        myGrep.setCaseSensitive(caseSensitive);
        myGrep.setAllPatterns(allPatterns);
        myGrep.setRegexp(isRegexp);
//        new String[]{"/Users/ilasavin/junk",
        myGrep.grep(pattern.split(","));
    }

    public MyGrep(File file) throws FileNotFoundException {
        inputStream = new FileInputStream(file);
    }

    public MyGrep() {
        inputStream = System.in;
    }

    /**
     * @param patterns массив шалонов поиска
     */
    public List<String> grep(String... patterns) {
        List<String> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                boolean allPresent = true;
                for (String pattern : patterns) {
                    if (allPatterns && !allPresent) {
                        break;
                    }
                    if (lineMatches(line, pattern)) {
                        allPresent = true;
                        if (!allPatterns) {
                            break;
                        }
                    } else {
                        allPresent = false;
                    }
                }
                if (allPresent) {
                    result.add(line);
//                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean lineMatches(String line, String pattern) {
        if (regexp) {
            return lineMatchesRegexp(line, pattern);
        } else {
            if (caseSensitive) {
                return line.contains(pattern);
            } else {
                return line.toLowerCase().contains(pattern.toLowerCase());
            }
        }
    }

    private boolean lineMatchesRegexp(String line, String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    public boolean isRegexp() {
        return regexp;
    }

    public void setRegexp(boolean regexp) {
        this.regexp = regexp;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isAllPatterns() {
        return allPatterns;
    }

    public void setAllPatterns(boolean allPatterns) {
        this.allPatterns = allPatterns;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
