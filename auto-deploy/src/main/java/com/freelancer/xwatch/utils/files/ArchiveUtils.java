package com.freelancer.xwatch.utils.files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.freelancer.xwatch.utils.FreelancerLoger;

public class ArchiveUtils {

	private ArchiveUtils() {
	}

	public static void zipDirectory(File dirToZip, File zipTarget) {

		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		try {
			fos = new FileOutputStream(zipTarget);
			zos = new ZipOutputStream(fos);

			zipArchive(zos, dirToZip.getAbsolutePath().length() + 1, dirToZip);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(String.format("Cannot zip directory %s to %s", dirToZip, zipTarget), e);
		} finally {
			closeOutputStream(zos);
			closeOutputStream(fos);
		}
	}

	private static void closeOutputStream(OutputStream stream) {
		if (stream != null)
			try {
				stream.close();
			} catch (IOException e) {
				throw new RuntimeException("Cannot close outputstream", e);
			}
	}

	private static void zipArchive(ZipOutputStream zos, int baseDirLength, File dirToZip) {

		for (File f : dirToZip.listFiles()) {
			ZipEntry entry = null;
			if (f.isDirectory()) {

				if (f.list().length == 0) { // Create empty directory
					entry = new ZipEntry(f.getAbsolutePath().substring(baseDirLength) + "/");
					try {
						zos.putNextEntry(entry);
					} catch (IOException e) {
						throw new RuntimeException("failed to zip archive " + zos, e);
					}
				} else {
					zipArchive(zos, baseDirLength, f);
				}

			} else {
				byte buf[] = new byte[8192];
				int ret = 0;
				try (BufferedInputStream fin = new BufferedInputStream(new FileInputStream(f))) {
					entry = new ZipEntry(f.getAbsolutePath().substring(baseDirLength));
					zos.putNextEntry(entry);
					while ((ret = fin.read(buf)) != -1) {
						zos.write(buf, 0, ret);
					}
				} catch (IOException e) {
					throw new RuntimeException("Unable to zipArchive " + dirToZip.getAbsolutePath(), e);
				}
			}
		}
	}

	public static final void unzipArchive(File zip, File extractTo) {
		ZipFile archive = null;
		try {
			archive = new ZipFile(zip);
			Enumeration<? extends ZipEntry> e = archive.entries();
			while (e.hasMoreElements()) {
				ZipEntry entry = e.nextElement();
				File file = new File(extractTo, entry.getName());
				if (entry.isDirectory()) {
					if (!file.exists())
						file.mkdirs();
				} else {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					InputStream in = archive.getInputStream(entry);
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
					byte[] buffer = new byte[8192];
					int read;
					while (-1 != (read = in.read(buffer))) {
						out.write(buffer, 0, read);
					}
					in.close();
					out.close();
				}
				file.setLastModified(entry.getTime());
			}

		} catch (IOException e) {
			throw new RuntimeException(String.format("Failed to unzip archive %s to %s", zip.getAbsolutePath(),
					extractTo.getAbsolutePath()), e);
		} finally {
			closeArchive(archive);
		}

	}

	private static void closeArchive(ZipFile archive) {
		if (archive != null) {
			try {
				archive.close();
			} catch (IOException e) {
				throw new RuntimeException("Unable to close archive", e);
			}
		}
	}

	/**
	 * Extracts archive to target directory. Supported archive type: .zip, .tar,
	 * .jar, .ar, 7z, ...
	 * 
	 * @param archive
	 *            archive to extract
	 * @param targetDir
	 *            output directory
	 * @throws RuntimeException
	 */
	public static void extract(File archive, File targetDir) {
		ArchiveInputStream input = null;

		try {
			input = new ArchiveStreamFactory()
					.createArchiveInputStream(new BufferedInputStream(new FileInputStream(archive)));
			FreelancerLoger.debug("Archive file class " + input.getClass());
			ArchiveEntry entry = null;
			while ((entry = input.getNextEntry()) != null) {
				File output = new File(targetDir.getAbsolutePath(), entry.getName());
				if (entry.isDirectory()) {
					if (!output.isDirectory())
						output.mkdirs();
				} else {
					if (!output.getParentFile().isDirectory())
						output.getParentFile().mkdirs();
					OutputStream os = new BufferedOutputStream(new FileOutputStream(output));
					IOUtils.copy(input, os);
					IOUtils.closeQuietly(os);
				}
			}
			FreelancerLoger.debug(
					"Extracted " + archive.getAbsolutePath() + " to " + targetDir.getAbsolutePath() + " successfully");
		} catch (IOException | ArchiveException e) {
			throw new RuntimeException("Unable to extract " + archive.getAbsolutePath(), e);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Decompresses file. Supported types are: gzip (.gz), bzip2 (.bz2).
	 * 
	 * @param compressedFile
	 *            compressed file
	 * @param output
	 *            decompressed file
	 * @throws RuntimeException
	 */
	public static void decompress(File compressedFile, File output) {
		CompressorInputStream is = null;
		try {
			is = new CompressorStreamFactory()
					.createCompressorInputStream(new BufferedInputStream(new FileInputStream(compressedFile)));
			FreelancerLoger.debug("Compressed file class " + is.getClass());
			FileUtils.copyInputStreamToFile(is, output);
			FreelancerLoger.debug("Decompressed " + compressedFile.getAbsolutePath() + " to " + output.getAbsolutePath()
					+ " successfully");
		} catch (CompressorException e) {
			throw new RuntimeException("Couldn't determine compressing type of " + compressedFile.getName(), e);
		} catch (IOException e) {
			throw new RuntimeException("Cannot decompress " + compressedFile.getAbsolutePath(), e);
		} finally {
			if (is != null)
				IOUtils.closeQuietly(is);
		}
	}

	/**
	 * Decompresses and then extract file. This method applicable for such
	 * filetype like tar.gz or tar.bz2.
	 * 
	 * @param compressedFile
	 * @param targetDir
	 * @throws RuntimeException
	 *             if anything goes wrong while decompressing file
	 */
	public static void decompressThenExtract(File compressedFile, File targetDir) {
		File tempDir = null;
		try {
			tempDir = FileUtility.createTempDirectory();
			File tarArchive = new File(tempDir, compressedFile.getName());
			decompress(compressedFile, tarArchive);
			extract(tarArchive, targetDir);
		} finally {
			FileUtils.deleteQuietly(tempDir);
		}
	}

	public static void archive(File srcDir, File dstArchive, ArchiveType type) {
		ArchiveOutputStream aOS = null;
		final String aType;
		switch (type) {
		case ZIP:
		case SOLUTION:
			aType = ArchiveStreamFactory.ZIP;
			break;
		case TAR:
			aType = ArchiveStreamFactory.TAR;
			break;
		default:
			throw new IllegalArgumentException("Unknown or unsupported archive type " + type.extension);
		}

		try {
			aOS = new ArchiveStreamFactory().createArchiveOutputStream(aType,
					new BufferedOutputStream(new FileOutputStream(dstArchive)));
			if (aOS instanceof TarArchiveOutputStream) {
				((TarArchiveOutputStream) aOS).setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
			}
			File[] files = srcDir.listFiles();
			for (File file : files) {
				addFileToArchive(aOS, file, "", aType);
			}
		} catch (ArchiveException | IOException e) {
			throw new RuntimeException(String.format("Archive %s to %s failed", srcDir, dstArchive), e);
		} finally {
			IOUtils.closeQuietly(aOS);
		}
	}

	/**
	 * @param aOS
	 * @param fileToAdd
	 * @param base
	 *            base prefix to for the name of the archive file entry
	 * @param archiveType
	 *            zip or tar
	 * @throws RuntimeException
	 */
	private static void addFileToArchive(ArchiveOutputStream aOS, File fileToAdd, String base,
			final String archiveType) {
		String entryName = base + fileToAdd.getName();
		ArchiveEntry entry = null;
		if (ArchiveStreamFactory.ZIP.equals(archiveType)) {
			entry = new ZipArchiveEntry(fileToAdd, entryName);
		} else if (ArchiveStreamFactory.TAR.equals(archiveType)) {
			entry = new TarArchiveEntry(fileToAdd, entryName);
		}
		if (entry == null)
			return;

		FileInputStream fInputStream = null;
		try {
			aOS.putArchiveEntry(entry);

			if (fileToAdd.isFile()) {
				fInputStream = new FileInputStream(fileToAdd);
				IOUtils.copy(fInputStream, aOS);
				aOS.closeArchiveEntry();

			} else if (fileToAdd.isDirectory()) {
				aOS.closeArchiveEntry();
				File[] children = fileToAdd.listFiles();

				if (children != null) {
					for (File child : children) {
						addFileToArchive(aOS, child, entryName + "/", archiveType);
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to add file to archive", e);
		} finally {
			IOUtils.closeQuietly(fInputStream);
		}
	}

	/**
	 * @param srcFile
	 * @param dstFile
	 * @param type
	 * @throws RuntimeException
	 */
	public static void compress(File srcFile, File dstFile, ArchiveType type) {
		String aType = null;
		switch (type) {
		case GZ:
			aType = CompressorStreamFactory.GZIP;
			break;
		default:
			throw new IllegalArgumentException("Unknown or unsupported compress type " + type.extension);
		}
		CompressorOutputStream cOS = null;
		InputStream is = null;
		try {
			cOS = new CompressorStreamFactory().createCompressorOutputStream(aType,
					new BufferedOutputStream(new FileOutputStream(dstFile)));
			is = new FileInputStream(srcFile);
			IOUtils.copy(is, cOS);
		} catch (IOException | CompressorException e) {
			throw new RuntimeException("compress " + srcFile, e);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(cOS);
		}
	}

	/**
	 * @param srcDir
	 * @param dstFile
	 * @throws RuntimeException
	 */
	public static void createTarGz(File srcDir, File dstFile) {
		File tempDir = null;
		try {
			tempDir = FileUtility.createTempDirectory();
			File tarFile = new File(tempDir, "temp.tar");
			archive(srcDir, tarFile, ArchiveType.TAR);
			compress(tarFile, dstFile, ArchiveType.GZ);
		} finally {
			FileUtils.deleteQuietly(tempDir);
		}

	}
}
