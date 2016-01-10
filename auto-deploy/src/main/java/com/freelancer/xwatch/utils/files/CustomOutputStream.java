package com.freelancer.xwatch.utils.files;

import java.io.FilterWriter;
import java.io.Writer;

public final class CustomOutputStream extends FilterWriter{

	protected CustomOutputStream(Writer out) {
		super(out);
	}


}
