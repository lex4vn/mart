package com.freelancer.xwatch.utils.files;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.security.*;

import org.apache.commons.io.*;
import org.apache.commons.io.FileUtils;

import com.freelancer.xwatch.utils.*;

/**
 * Utility class for file-handling.
 */
public class FileUtility {

    private FileUtility() {

    }

    public static File createTempDirectory() {

        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        File resultDir;
        try {
            resultDir = File.createTempFile("Automic", "AutoPkgMgr", baseDir);
            if (!resultDir.delete()) {
                throw new RuntimeException("Could not delete temp file: " + resultDir.getAbsolutePath());
            }

            if (!resultDir.mkdirs()) {
                throw new RuntimeException("Failed to create tmp directory.");
            }
            return resultDir;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp dir");
        }
    }

    public static File getJarDir(Class<?> aclass) {
        URL url;
        String extURL;

        // get an URL
        try {
            url = aclass.getProtectionDomain().getCodeSource().getLocation();
        } catch (Exception ex) {
            url = aclass.getResource(aclass.getSimpleName() + ".class");
        }

        extURL = url.toExternalForm();

        // prune for various cases
        if (extURL.endsWith(".jar")) {
            extURL = extURL.substring(0, extURL.lastIndexOf("/"));
        } else { // from getResource
            String suffix = "/" + aclass.getName().replace(".", "/") + ".class";
            extURL = extURL.replace(suffix, "");
            if (extURL.startsWith("jar:") && extURL.endsWith(".jar!")) {
                extURL = extURL.substring(4, extURL.lastIndexOf("/"));
            }
        }

        // convert back to url
        try {
            url = new URL(extURL);
        } catch (MalformedURLException mux) {
            // leave url unchanged; probably does not happen
        }

        try {
            return new File(url.toURI());
        } catch (URISyntaxException ex) {
            return new File(url.getPath());
        }
    }

    public static String getInstallationPath(Class<?> aclass) {

        // try using apm.home property first
        if (isValidPath(System.getProperty("apm.home"))) {
            return System.getProperty("apm.home");
        }

        File jarPath = FileUtility.getJarDir(aclass);

        if (jarPath == null) {
            FDLogger.error("Could not get installation path");
            return null;
        }

        if (jarPath.getParent() != null) {
            return jarPath.getParent();
        }

        return jarPath.getPath();
    }

    private static boolean isValidPath(String path) {

        if (path == null) {
            return false;
        }

        if (!Files.exists(Paths.get(path))) {
            return false;
        }

        return true;
    }

    public static String getUserHomePath() {
        String home = System.getProperty("user.home");
        if (home != null && !"".equals(home)) {
            return home;
        }
        return null;
    }

    public static String getWorkingPath() {
        if (System.getProperty("user.dir") != null) {
            return System.getProperty("user.dir");
        }

        return new File("").getAbsolutePath();
    }

    public static String getChecksum(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] dataBytes = new byte[1024];

            int nread = 0;

            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            };

            byte[] mdbytes = md.digest();

            // convert the byte to hex format
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyDirectory(File srcDir, File destDir) {
        try {
            FileUtils.copyDirectory(srcDir, destDir);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to copy dir from %s to %s: %s", srcDir.getAbsolutePath(), destDir.getAbsolutePath(), e.getMessage()), e);
        }
    }

    public static void createTextFile(String path, String text) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(path, "UTF-8");
            writer.println(text);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFileName(URL url) {
        String path = url.getPath();
        String baseName = FilenameUtils.getBaseName(path);
        String extension = FilenameUtils.getExtension(path);
        return String.format("%s.%s", baseName, extension);
    }

    private static final String PROTOCOL_FILE = "file";

    public static void deleteDirectory(File path) throws IOException, SecurityException {
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        path.delete();
    }

    public static File createTempDirectory(String directoryName) {

        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        File directory = new File(baseDir, directoryName);
        if (directory.isDirectory()) {
            return directory;
        }

        try {
            directory.mkdir();
            return directory;
        } catch (SecurityException se) {
            throw new SecurityException("Failed to create the temp directory: " + directory.getPath(), se);
        }
    }

    public static String readFileAsString(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                sb.append(line);
                sb.append(System.getProperty("line.separator"));
            }
            return sb.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

    }

    public static File fileFromUri(URI uri) {
        try {
            if (uri.getScheme() == null) {
                uri = new File(uri.getPath()).toURI();
            } else {
                if (!uri.getScheme().equals(PROTOCOL_FILE)) {
                    return null;
                }
            }
            String fileName = uri.toURL().getFile();
            return new File(URLDecoder.decode(fileName, "UTF8"));
        } catch (Exception e) {
        }
        return null;

    }

    /**
     * Copies the content of a file to another file.
     *
     * @param filenameFrom the filename from
     * @param filenameTo the filename to
     * @throws FileNotFoundException the file not found exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void copyWholeFile(String filenameFrom, String filenameTo) throws FileNotFoundException, IOException {

        RandomAccessFile fis = new RandomAccessFile(filenameFrom, "r");
        // If the parent directory of the destination file doesn't exist, try to
        // create it
        File f = new File(filenameTo);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        boolean ok = f.delete();
        // ignore it if the delete was not successful (there may not even be an
        // old file there
        FileOutputStream fos = new FileOutputStream(filenameTo + ".filepart");
        byte[] content = new byte[8192];

        try {
            int i = -1;
            while ((i = fis.read(content)) > 0) {
                fos.write(content, 0, i);
            }
        } finally {
            fis.close();
            fos.close();
        }
        File file = new File(filenameTo + ".filepart");
        ok = file.renameTo(new File(filenameTo));
        if (!ok) {
            throw new RuntimeException("FileCopy from " + filenameFrom + " to " + filenameTo + " failed.");
        }
    }
}
