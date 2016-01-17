package com.freelancer.xwatch.utils.files;

import java.io.*;

public final class CustomOutputStream extends FilterWriter {

    protected CustomOutputStream(Writer out) {
        super(out);
    }

}
