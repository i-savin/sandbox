package ru.isavin.misc;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * @author ilasavin
 * @since 07.06.15
 */
public class DirListOnly {
    public static void main(String[] args) {
        String dirname = "/java";
        File f1 = new File(dirname);
        FilenameFilter ff = (file, name) -> {return name.endsWith("");};
        f1.listFiles(ff);
    }
}
