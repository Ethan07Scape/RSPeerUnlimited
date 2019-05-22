package org.ethan.peer.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class Utilities {

    public static void addToSystemClassLoader(File jar) {
        try {
            File file = jar;
            if (!file.exists()) {
                System.out.println("No File!");
                System.exit(0);
            }
            System.out.println("Adding "+file.getName() + " to system classloader.");
            URL url = file.toURI().toURL();
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dumpJar(final File file, HashMap<String, byte[]> mapOne, HashMap<String, byte[]> mapTwo) {
        try {
            dumpJar(new FileOutputStream(file), mapOne, mapTwo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void dumpJar(final FileOutputStream stream, HashMap<String, byte[]> mapOne, HashMap<String, byte[]> mapTwo) {
        try {
            JarOutputStream out = new JarOutputStream(stream);
            for (Map.Entry<String, byte[]> entry : mapOne.entrySet()) {
                JarEntry je = new JarEntry(entry.getKey());
                out.putNextEntry(je);
                out.write(entry.getValue());
            }
            for (Map.Entry<String, byte[]> entry : mapTwo.entrySet()) {
                JarEntry je = new JarEntry(entry.getKey());
                out.putNextEntry(je);
                out.write(entry.getValue());
            }
            out.close();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
