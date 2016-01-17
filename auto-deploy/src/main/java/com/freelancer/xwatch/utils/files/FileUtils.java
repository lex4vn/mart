package com.freelancer.xwatch.utils.files;

import java.io.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.*;

import com.freelancer.xwatch.utils.*;
import com.freelancer.xwatch.utils.strings.*;

public final class FileUtils {
    private FileUtils() {}

    private static boolean validFile(File file, boolean isDirectory) {
        return FileUtils.validFile(file, isDirectory, false);
    }

    private static boolean validFile(File file, boolean isDirectory, boolean isCreateIfNotExist) {
        if (file == null) {
            throw new IllegalArgumentException("File is not null");
        }
        if (!file.exists()) {
            if (isCreateIfNotExist) {
                if (isDirectory) {
                    return file.mkdirs();
                } else {
                    try {
                        return file.createNewFile();
                    } catch (IOException e) {
                        FDLogger.error("Cannot create file", e);
                        return false;
                    }
                }
            }
            throw new IllegalArgumentException("File with path: " + file.getAbsolutePath() + " is not existed");
        }
        if (isDirectory ? !file.isDirectory() : !file.isFile()) {
            String msg = isDirectory ? "folder" : "file";
            throw new IllegalArgumentException("File must be a " + msg);
        }
        return true;
    }

    public static File getWorkingFolder(String workingFolderPath, boolean isNew) {
        File temp = new File(workingFolderPath);
        if (temp.exists()) {
            if (isNew) {
                FileUtils.delete(temp);
            } else {
                return temp;
            }
        }
        temp.mkdir();
        return temp;
    }

    public static void delete(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                FileUtils.delete(f);
            }
        }
        file.delete();
    }

    public static void copy(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            FileUtils.copyDirectory(source, destination, true);
        } else {
            FileUtils.copyFile(source, destination);
        }
    }

    public static void copyDirectory(File source, File destination, boolean isFirst) throws IOException {
        FileUtils.validFile(source, true);
        FileUtils.validFile(destination, true, true);
        Path newFolderPath = destination.toPath().resolve(source.getName());
        if (isFirst) {
            File newFolder = newFolderPath.toFile();
            newFolder.mkdir();
            destination = newFolder;
        }

        File[] files = source.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                FileUtils.copyDirectory(file, new File(destination, file.getName()), false);
            } else {
                FileUtils.copyFile(file, new File(destination, file.getName()));
            }
        }
    }

    public static void copyFile(File source, File destination) throws IOException {
        FileUtils.validFile(source, false);

        if (destination == null) {
            throw new IllegalArgumentException("The destination is null.");
        }

        if (destination.exists() && destination.isFile()) {
            // do not overwrite existing file
            return;
        }

        File dest;
        if (destination.isDirectory()) {
            dest = destination.toPath().resolve(source.getName()).toFile();
        } else {
            dest = destination;
        }

        FileChannel sourceChannel = null;
        FileChannel targetChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            targetChannel = new FileOutputStream(dest).getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
        } finally {
            if (sourceChannel != null) {
                sourceChannel.close();
            }
            if (targetChannel != null) {
                targetChannel.close();
            }
        }
    }

    public static void rename(File source, Collection<TextOption> textOptions) {
        if (source.isDirectory()) {
            File[] files = source.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    rename(file, textOptions);
                } else if (file.isFile()) {
                    renameFile(file, textOptions);
                }
            }
            renameFile(source, textOptions);
        } else if (source.isFile()) {
            renameFile(source, textOptions);
        }
    }

    private static void renameFile(File source, Collection<TextOption> textOptions) {
        String sourceName = source.getName();
        String parent = source.getParent();
        for (TextOption textOption : textOptions) {
            String targetName = textOption.process(sourceName);
            if (targetName.equals(sourceName)) {
                continue;
            }
            boolean isRename = source.renameTo(new File(parent + File.separator + targetName));
            if (isRename) {
                break;
            } else {
                FDLogger.debug("Cannot rename file with path: " + sourceName);
            }
        }
    }

    public static void move(File source, File destination) throws IOException {
        FileUtils.copy(source, destination);
        FileUtils.delete(source);
    }

    public static void moveFile(File source, Path destinationPath) throws IOException {
        Files.move(source.toPath(), destinationPath.resolve(source.getName()), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyFolderWithTextOptions(File folder, FileFilter fileFilter, TextOption... textOptions)
        throws IOException {
        copyFolderWithTextOptions(folder, fileFilter, Arrays.asList(textOptions));
    }

    public static void copyFolderWithTextOptions(File folder, FileFilter fileFilter, Collection<TextOption> textOptions)
        throws IOException {
        FileUtils.validFile(folder, true);
        File[] files = folder.listFiles();
        for (File file : files) {
            if (fileFilter == null ? false : !fileFilter.accept(file)) {
                continue;
            }
            if (file.isDirectory()) {
                FileUtils.copyFolderWithTextOptions(file, fileFilter, textOptions);
            } else if (file.isFile()) {
                FileUtils.copyFileWithTextOptions(file, null, textOptions);
            }
        }
    }

    public static void copyFileWithTextOptions(File source, File destination, Collection<TextOption> textOptions)
        throws IOException {

        FileUtils.validFile(source, false);
        boolean updateFile = destination == null;
        File dest = FileUtils.createTempFileFromSource(source, destination);
        if (textOptions == null) {
            FileUtils.copy(source, dest);
            if (updateFile) {
                FileUtils.delete(source);
                dest.renameTo(source);
            }
            return;
        }
        OutputStream os = new FileOutputStream(dest);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        Scanner scanner = new Scanner(source);
        int countBlankLine = 0;
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            for (TextOption textOption : textOptions) {
                currentLine = textOption.process(currentLine);
            }
            if (StringUtils.isEmpty(currentLine)) {
                countBlankLine++;
                if (countBlankLine >= 2) {
                    continue;
                }
            } else {
                countBlankLine = 0;
            }
            bw.write(currentLine);
            bw.write(Constants.NEW_LINE);
        }
        scanner.close();
        bw.flush();
        bw.close();
        if (updateFile) {
            FileUtils.delete(source);
            dest.renameTo(source);
        }
    }

    public static void copyFileWithRemoveParagraph(File source, File destination, Collection<Paragraph> paragraphs)
        throws IOException {
        FileUtils.validFile(source, false);
        boolean updateFile = destination == null;
        File dest = FileUtils.createTempFileFromSource(source, destination);

        if (paragraphs == null || paragraphs.isEmpty()) {
            FileUtils.copy(source, dest);
            if (updateFile) {
                FileUtils.delete(source);
                dest.renameTo(source);
            }
            return;
        }
        OutputStream os = new FileOutputStream(dest);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        Scanner scanner = new Scanner(source);
        boolean inRemove = false;
        Paragraph tmpParagraph = null;
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            for (Paragraph paragraph : paragraphs) {
                if (!inRemove && currentLine.contains(paragraph.getStart())) {
                    tmpParagraph = paragraph;
                    inRemove = true;
                    break;
                }
            }
            if (!inRemove) {
                bw.write(currentLine);
                bw.write(Constants.NEW_LINE);
            }
            if (tmpParagraph != null && (tmpParagraph.isOneLine() || inRemove
                && currentLine.contains(tmpParagraph.getEnd()))) {
                inRemove = false;
            }
        }
        scanner.close();
        bw.flush();
        bw.close();
        if (updateFile) {
            FileUtils.delete(source);
            dest.renameTo(source);
        }
    }

    private static File createTempFileFromSource(File source, File destination) throws IOException {
        File dest = destination;
        if (dest == null) {
            int count = 0;
            Path parentPath = source.getParentFile().toPath();
            while (true) {
                String destinationName = Constants.TEMP + (count == 0 ? Constants.BLANK : Constants.UNDERLINE + count)
                    + source.getName();
                Path destinationPath = parentPath.resolve(destinationName);
                dest = destinationPath.toFile();
                if (dest.exists() && dest.isFile()) {
                    count++;
                } else {
                    dest.createNewFile();
                    break;
                }
            }
        } else {
            FileUtils.validFile(dest, true, true);
            dest = dest.toPath().resolve(source.getName()).toFile();
            dest.createNewFile();
        }
        return dest;
    }

    public static String getFirstContentByKeyWord(File file, String keyword) {
        FileUtils.validFile(file, false);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(keyword)) {
                    return line;
                }
            }
        } catch (FileNotFoundException e) {
            FDLogger.error("Unexpected error", e);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return null;
    }

    public static boolean hasMatchInContent(File folder, String... keywords) {
        FileUtils.validFile(folder, true);
        File[] files = folder.listFiles();
        for (File currentFile : files) {
            if (currentFile.isDirectory()) {
                if (FileUtils.hasMatchInContent(currentFile, keywords)) {
                    return true;
                }
            } else {
                Scanner scanner = null;
                try {
                    scanner = new Scanner(currentFile);
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        for (String keyword : keywords) {
                            if (line.contains(keyword)) {
                                return true;
                            }
                        }
                    }
                } catch (IOException e) {
                    FDLogger.debug("Unexpected error", e);
                } finally {
                    if (scanner != null) {
                        scanner.close();
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasFile(FileFilter filter, File folder) {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("File: " + folder.getAbsolutePath() + " is not a directory");
        }
        File[] files = folder.listFiles(filter);
        if (files.length > 0) {
            return true;
        }
        FileFilter folderFilter = new CustomFileFilter(FileType.FOLDER);
        File[] subFolders = folder.listFiles(folderFilter);
        for (File subFolder : subFolders) {
            if (FileUtils.hasFile(filter, subFolder)) {
                return true;
            }
        }
        return false;
    }

    public static File findFirstFolder(FileFilter filter, File folder) {
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("File: " + folder.getAbsolutePath() + " is not a directory");
        }
        File[] files = folder.listFiles(filter);
        if (files.length > 0) {
            return folder;
        }
        FileFilter folderFilter = new CustomFileFilter(FileType.FOLDER);
        File[] subFolders = folder.listFiles(folderFilter);
        for (File subFolder : subFolders) {
            if (FileUtils.findFirstFolder(filter, subFolder) != null) {
                return subFolder;
            }
        }
        return null;
    }

    public static Path createFileOrFolder(File rootFolder, String pathInString, boolean isFolder) {
        try {
            Path path = rootFolder.toPath().resolve(pathInString);
            File file = path.toFile();
            if (file.exists()) {
                return path;
            }
            if (isFolder) {
                file.mkdirs();
            } else {
                file.createNewFile();
            }
            return path;
        } catch (IOException e) {
            FDLogger.debug(e);
            return null;
        }
    }

    public static Properties loadProperties(String file, String defaultFile) {
        InputStream is = null;
        try {
            Properties props = new Properties();
            is = StringUtils.isEmpty(file) ? FileUtils.class.getResourceAsStream("/" + defaultFile)
                : new FileInputStream(file);
            props.load(is);
            return props;
        } catch (IOException ex) {
            FDLogger.debug(ex);
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    FDLogger.debug("Cannot close input stream", e);
                }
            }
        }
    }

    public static void write(Path fileName, boolean isAppend, String content) throws IOException {
        if (fileName == null) {
            return;
        }
        File file = fileName.toFile();
        if (!file.exists() || file.isDirectory()) {
            return;
        }
        OutputStream os = new FileOutputStream(file, isAppend);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        bw.append(Constants.NEW_LINE);
        bw.append(content);
        bw.flush();
        bw.close();
    }

    public static void write(File destFile, boolean isAppend, File srcFile, String content) throws IOException {
        if (destFile == null || !destFile.exists() || destFile.isDirectory()) {
            return;
        }
        OutputStream os = new FileOutputStream(destFile, isAppend);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        bw.append(Constants.NEW_LINE);
        bw.append(content);
        if (srcFile != null && srcFile.exists() && srcFile.isFile()) {
            Scanner scanner = new Scanner(srcFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                bw.append(line);
                bw.append(Constants.NEW_LINE);
            }
            scanner.close();
        }
        bw.flush();
        bw.close();
    }

    // public static void main(String[] args)
    // {
    // File source = new File("D:/PCK.AUTOMIC_ORACLE_ABC");
    // Collection<TextOption> textOptions = TextOptionUtils.loadTextOptions("textOptions.json");
    // FileUtils.rename(source, textOptions);
    // // try
    // // {
    // // FileUtils.copyFileWithTextOption(source, null, textOptions);
    // // }
    // // catch (IOException e)
    // // {
    // // e.printStackTrace();
    // // }
    // }
}
