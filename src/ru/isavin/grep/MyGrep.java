package ru.isavin.grep;

import java.io.*;

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
        } else {
            File file = new File(args[0]);
            try {
                file = new File(args[0]);
                myGrep = new MyGrep(file);
                pattern = args[1];
            } catch (FileNotFoundException e) {
                System.err.println("MyGrep: " + file + ": no such file or directory");
                System.exit(-2);
            }
        }

//        new String[]{"/Users/ilasavin/junk",
        myGrep.grep(pattern);
    }

    public MyGrep(File file) throws FileNotFoundException {
        inputStream = new FileInputStream(file);
    }

    public MyGrep() {
        inputStream = System.in;
    }

    public void grep(String pattern) {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (lineMatches(line, pattern, true)) {
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
