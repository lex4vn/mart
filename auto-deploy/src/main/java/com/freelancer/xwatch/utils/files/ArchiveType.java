package com.freelancer.xwatch.utils.files;

public enum ArchiveType {
	TAR("tar"), ZIP("zip"), GZ("gz"), SOLUTION("solution"), TAR_GZ("tar.gz"), BZ2("bz2");

	String extension;

	private ArchiveType(String ex) {
		this.extension = ex;
	}

	public String extension() {
		return extension;
	}
}