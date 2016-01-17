package com.freelancer.xwatch.database;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.freelancer.xwatch.utils.*;
import com.freelancer.xwatch.utils.files.*;

public class SQLRunner {

    private final Connection conn;
    private final Statement stmt;
    private boolean stopOnError = false;
    private boolean executeFullScript = false;
    private String scriptDelimiter = ";";

    public SQLRunner(Connection conn) throws SQLException {
        this.conn = conn;
        this.stmt = conn.createStatement();
    }

    public void setScriptDelimiter(String scriptDelimiter) {
        this.scriptDelimiter = scriptDelimiter;
    }

    public void setStopOnError(boolean stopOnError) {
        this.stopOnError = stopOnError;
    }

    public void setExecueFullScript(boolean executeFullScript) {
        this.executeFullScript = executeFullScript;
    }

    public void close() {
        FDLogger.LOG.entry();
        if (this.stmt != null) {
            try {
                this.stmt.close();
            } catch (SQLException e) {

                FDLogger.error("Statement closed", e);
            }
        }

        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {
                FDLogger.error("Connection closed", e);
            }
        }
        FDLogger.LOG.exit();

    }

    public void commit() throws SQLException {
        FDLogger.LOG.entry();
        this.conn.commit();
        FDLogger.LOG.exit();
    }

    public void rollback() throws SQLException {
        FDLogger.LOG.entry();
        this.conn.rollback();
        FDLogger.LOG.exit();
    }

    public void executeScript(File script) throws SQLException {
        FDLogger.LOG.entry(script);
        Object sqls = null;
        try {
            if (this.executeFullScript) {
                sqls = FileUtility.readFileAsString(script);
            } else {
                sqls = readStatementsFromFile(script, this.scriptDelimiter);
            }
        } catch (IOException io) {
            FDLogger.error("Couldn't read SQL statements from script file");
            throw new SQLException();
        }
        if (this.executeFullScript) {
            this.executeSQL((String) sqls);
        } else {

            for (String sql : (String[]) sqls) {
                try {
                    this.executeSQL(sql);
                } catch (SQLException e) {
                    FDLogger.error("Error occurred when executing command", e);
                    if (this.stopOnError) {
                        throw e;
                    }
                }
            }

        }
        FDLogger.LOG.exit();
    }

    public static String[] readStatementsFromFile(File script, String seperator) throws IOException {
        FDLogger.LOG.entry(script, seperator);
        String sqls = FileUtility.readFileAsString(script);
        List<String> l = new ArrayList<String>();
        String[] statements =
            sqls.replaceAll("(?s)((['\"]).*?(?<!\\\\)\\2)|/\\*.*?\\*/|//.*?(\r?\n|$)|--.*?(\r?\n|$)", "$1$3$4").split(seperator);
        for (String statement : statements) {
            String trimmedLine = statement.trim();
            if (trimmedLine.isEmpty()) {
                continue;
            }
            l.add(trimmedLine);
        }
        return FDLogger.LOG.exit(l.toArray(new String[l.size()]));

    }

    public SQLResult executeSQL(String sql) throws SQLException {

        FDLogger.LOG.entry(sql);

        SQLResult result = new SQLResult();

        boolean hasResults = this.stmt.execute(sql);
        if (hasResults) {
            ResultSet rs = this.stmt.getResultSet();
            if (rs != null) {
                final ResultSetMetaData md = rs.getMetaData();
                final int cols = md.getColumnCount();
                while (rs.next()) {
                    Row row = new Row();
                    for (int i = 0; i < cols; i++) {
                        String name = md.getColumnLabel(i + 1);
                        String value = rs.getString(i + 1);
                        row.getColumn().add(new Column<String, String>(name, value));

                    }
                    result.getTable().getRowList().add(row);
                }
                rs.close();
            }
        }
        int updateCount = this.stmt.getUpdateCount();
        if (updateCount != -1) {
            result.setUpdateCount(updateCount);
        }

        return FDLogger.LOG.exit(result);
    }

}
