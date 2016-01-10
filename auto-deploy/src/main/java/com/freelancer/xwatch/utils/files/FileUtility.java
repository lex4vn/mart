package com.freelancer.xwatch.utils.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;

import com.freelancer.xwatch.utils.FreelancerLoger;

/**
 * Utility class for file-handling.
 */
public class FileUtility {

	private FileUtility() {

	}

	public static void deleteDirectory(File path) {
		File[] files = path.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory())
					deleteDirectory(file);
				else
					file.delete();
			}
		}
		path.delete();
	}

	public static File createTempDirectory() {

		File baseDir = new File(System.getProperty("java.io.tmpdir"));
		File resultDir;
		try {
			resultDir = File.createTempFile("Automic", "AutoPkgMgr", baseDir);
			if (!(resultDir.delete())) {
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
		if (extURL.endsWith(".jar")) // from getCodeSource
			extURL = extURL.substring(0, extURL.lastIndexOf("/"));
		else { // from getResource
			String suffix = "/" + (aclass.getName()).replace(".", "/") + ".class";
			extURL = extURL.replace(suffix, "");
			if (extURL.startsWith("jar:") && extURL.endsWith(".jar!"))
				extURL = extURL.substring(4, extURL.lastIndexOf("/"));
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
			FreelancerLoger.error("Could not get installation path");
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
			}
			;

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
			throw new RuntimeException(String.format("Failed to copy dir from %s to %s: %s",
					srcDir.getAbsolutePath(), destDir.getAbsolutePath(), e.getMessage()), e);
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
}
