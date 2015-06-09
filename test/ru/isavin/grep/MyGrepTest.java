package ru.isavin.grep;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author ilasavin
 * @since 08.06.15
 */
public class MyGrepTest {

    @Test
    public void simpleTest() {
        String s = "hello\nworld";
        MyGrep myGrep = new MyGrep();
        myGrep.setInputStream(new ByteArrayInputStream(s.getBytes()));
        String expected = "world";
        List<String> grepResult = myGrep.grepToList("world");
        assertFalse(grepResult.isEmpty());
        assertEquals(expected, grepResult.get(0));
    }

    @Test
    public void caseSensitiveTest() throws IOException {
        String s = "hello\nworld";
        MyGrep myGrep = new MyGrep();
        myGrep.setInputStream(new ByteArrayInputStream(s.getBytes()));
        myGrep.setCaseSensitive(true);
        String expected = "hello";
        List<String> grepResult = myGrep.grepToList("Hello");
        assertTrue(grepResult.isEmpty());
        myGrep.getInputStream().reset();
        grepResult = myGrep.grepToList("hello");
        assertFalse(grepResult.isEmpty());
        assertEquals(expected, grepResult.get(0));
    }

    @Test
    public void allPatternsTest() throws IOException {
        String s = "hello world\nhello\nworld\n";
        MyGrep myGrep = new MyGrep();
        myGrep.setInputStream(new ByteArrayInputStream(s.getBytes()));
        String expected = "hello world";
        myGrep.setAllPatterns(true);
        List<String> grepResult = myGrep.grepToList("hello", "world");
        assertFalse(grepResult.isEmpty());
        assertEquals(expected, grepResult.get(0));

        myGrep.getInputStream().reset();
        myGrep.setAllPatterns(false);
        grepResult = myGrep.grepToList("hello", "world");
        assertFalse(grepResult.isEmpty());
        System.out.println(grepResult);
        assertEquals(3, grepResult.size());
    }

    @Test
    public void regexpTest() throws IOException {
        String s = "hello\nworld!";
        MyGrep myGrep = new MyGrep();
        myGrep.setInputStream(new ByteArrayInputStream(s.getBytes()));
        myGrep.setRegexp(true);
        List<String> grepResult = myGrep.grepToList("[a-z]+");
        assertFalse(grepResult.isEmpty());

        myGrep.setRegexp(false);
        myGrep.getInputStream().reset();
        grepResult = myGrep.grepToList("[a-z]+");
        assertTrue(grepResult.isEmpty());
    }
}
