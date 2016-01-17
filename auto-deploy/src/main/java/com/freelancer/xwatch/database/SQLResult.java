package com.freelancer.xwatch.database;

public class SQLResult {
    private Table table;
    private int updateCount;

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public SQLResult() {
        table = new Table();
        updateCount = 0;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public SQLResult add(SQLResult result) {
        return null;
    }

}
