package com.freelancer.xwatch.utils.files;

public enum FileType {
	FOLDER, FILE, ALL;

	public boolean isFolder() {
		return this == FOLDER || this == ALL;
	}

	public boolean isFile() {
		return this == FILE || this == ALL;
	}

	public boolean isAll() {
		return this == ALL;
	}
}