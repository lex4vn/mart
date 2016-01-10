package com.freelancer.xwatch.utils.files;

import java.io.File;
import java.io.FileFilter;

import com.freelancer.xwatch.utils.Constants;
import com.freelancer.xwatch.utils.strings.EndsWithFilter;
import com.freelancer.xwatch.utils.strings.StringFilter;

public final class CustomFileFilter implements FileFilter {
	public static final FileFilter XML_FILTER = new CustomFileFilter(FileType.FILE,
			new EndsWithFilter(Constants.SUFFIX_XML));
	public static final FileFilter TXT_FILTER = new CustomFileFilter(FileType.FILE,
			new EndsWithFilter(Constants.SUFFIX_TXT));

	private final FileType fileType;
	private final StringFilter filter;

	public CustomFileFilter(FileType fileType) {
		this(fileType, null);
	}

	public CustomFileFilter(FileType fileType, StringFilter filter) {
		this.fileType = fileType;
		this.filter = filter;
	}

	public boolean accept(File file) {
		if (!this.fileType.isAll()) {
			if (this.fileType.isFolder() ? !file.isDirectory() : !file.isFile()) {
				return false;
			}
		}
		if (this.filter == null) {
			return true;
		}
		return this.filter.accept(file.getName());
	}

}
