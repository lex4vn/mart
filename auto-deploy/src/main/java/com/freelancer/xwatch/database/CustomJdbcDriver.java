package com.freelancer.xwatch.database;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

public final class CustomJdbcDriver implements Driver {

    private Driver theDriver;

    public Driver getTheDriver() {
        return this.theDriver;
    }

    public void setTheDriver(Driver theDriver) {
        this.theDriver = theDriver;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return this.theDriver.acceptsURL(url);
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return this.theDriver.connect(url, info);
    }

    @Override
    public int getMajorVersion() {
        return this.theDriver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return this.theDriver.getMinorVersion();
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return this.theDriver.getPropertyInfo(url, info);
    }

    @Override
    public boolean jdbcCompliant() {
        return this.theDriver.jdbcCompliant();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return java.util.logging.Logger.getGlobal();
    }
}
