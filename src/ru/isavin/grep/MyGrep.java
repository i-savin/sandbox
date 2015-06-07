package ru.isavin.grep;

import java.io.*;
import java.util.List;

/**
 * @author ilasavin
 * @since 05.06.15
 */
public class MyGrep {

    private InputStream inputStream;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: MyGrep input_string string_to_search");
            System.exit(-1);
        }

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
            System.err.println("usage: MyGrep input_string string_to_search");
            System.exit(-1);
        }
        boolean caseSensitive = false;
        boolean allPatterns = false;

        for (String arg : args) {
            if ("-c".equals(arg)) {
                caseSensitive = true;
            }
            if ("-a".equals(arg)) {
                allPatterns = true;
            }
        }
//        new String[]{"/Users/ilasavin/junk",
        myGrep.grep(caseSensitive, allPatterns, pattern.split(","));
    }

    public MyGrep(File file) throws FileNotFoundException {
        inputStream = new FileInputStream(file);
    }

    public MyGrep() {
        inputStream = System.in;
    }

    public void grep(boolean caseSensitive, boolean allPatternsCheck, String... patterns) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                boolean allPresent = true;
                for (String pattern : patterns) {
                    if (allPatternsCheck && !allPresent) {
                        break;
                    }
                    if (lineMatches(line, pattern, caseSensitive)) {
                        if (!allPatternsCheck) {
                            break;
                        }
                    } else {
                        allPresent = false;
                    }
                }
                if (allPresent) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean lineMatches(String line, String pattern, boolean caseSensitive) {
        if (caseSensitive) {
            return line.contains(pattern);
        } else {
            return line.toLowerCase().indexOf(pattern.toLowerCase()) >= 0;
        }

    }
}
