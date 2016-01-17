package com.freelancer.xwatch.database;

import java.util.*;

public class Row {
    private final List<Column<String, String>> cols = new ArrayList<>();

    public List<Column<String, String>> getColumn() {
        return this.cols;
    }
}
