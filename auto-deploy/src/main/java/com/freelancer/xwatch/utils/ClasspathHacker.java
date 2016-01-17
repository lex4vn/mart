package com.freelancer.xwatch.utils;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;

/**
 * Useful class for dynamically changing the classpath, adding classes during runtime.
 * 
 * @author unknown
 */
public class ClasspathHacker {

    /**
     * Adds a file to the classpath
     * 
     * @param f the file to be added
     * @throws IOException
     */
    public static void addFile(File f) throws IOException {
        URL jarURL = new URL("jar", "", "file:" + f.getCanonicalPath() + "!/");
        addURL(jarURL);
    }

    public static void addDir(File f) throws IOException {
        addURL(f.toURI().toURL());
    }

    /**
     * Adds a files to the classpath
     * 
     * @param files list of files to be added
     * @throws IOException
     */
    public static void addFile(File[] files) throws IOException {
        for (File f : files) {
            addFile(f);
        }
    }

    /**
     * Adds the content pointed by the URL to the classpath.
     * 
     * @param u the URL pointing to the content to be added
     * @throws IOException
     */
    private static void addURL(URL u) throws IOException {
        URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] {java.net.URL.class});
            method.setAccessible(true);
            method.invoke(sysLoader, new Object[] {u});
        } catch (Throwable t) {
            throw new IOException("Could not add URL " + u.toString() + " to system classloader");
        }
    }

    public static void loadJarsIntoClasspath(String directoryPath) throws Exception {

        FilenameFilter jarFileFilter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {

                return name.endsWith(".jar") && name.toLowerCase().startsWith("siebel");

            }
        };
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            throw new RuntimeException("Path '" + directoryPath + "' isn't valid");
        }
        File[] jars = dir.listFiles(jarFileFilter);
        ClasspathHacker.addFile(jars);
    }

    public static void loadDirIntoClasspath(String directoryPath) throws Exception {
        ClasspathHacker.addDir(new File(directoryPath));
    }
}
