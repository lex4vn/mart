package com.freelancer.xwatch.database;

import java.io.*;
import java.net.*;
import java.sql.*;

import org.apache.commons.io.FileUtils;

import com.freelancer.xwatch.cli.*;
import com.freelancer.xwatch.crypto.*;
import com.freelancer.xwatch.i18n.*;
import com.freelancer.xwatch.runner.*;
import com.freelancer.xwatch.utils.*;
import com.freelancer.xwatch.utils.files.*;

/**
 *
 */
public final class SQLTask extends AbstractTask {

    private Option<String> userArg;
    private Option<String> passArg;
    private Option<String> jdbcConnStrArg;
    private Option<String> jdbcDriverFileArg;
    private Option<String> jdbcDriverClassArg;
    private Option<Integer> loginTimeoutArg;
    private Option<Integer> connRetriesArg;
    private Option<String> scriptFileArg;
    private Option<String> queryArg;
    private Option<String> scriptSeperatorArg;
    private Option<String> outputSeperatorArg;
    private Option<String> propArg;
    private Option<String> tnsNamesORAFileNameArg;
    private Option<String> sqlPasswordArg;

    private Connection connection;
    private int connectionInterval = 10;
    private int nConnRetries = 5;
    // Number of results returned by the execution

    @Override
    public void initialize() {
        super.initialize();

        this.userArg =
            this.parser.addHelp(this.parser.addStringOption("usr", "user", true), FDInfoMessage.DATABASE_USER);

        this.passArg =
            this.parser.addHelp(this.parser.addStringOption("pwd", "password", true), FDInfoMessage.DATABASE_PASSWORD);

        this.jdbcConnStrArg =
            this.parser.addHelp(this.parser.addStringOption("jdbc_conn", "jdbcUrl", true), FDInfoMessage.JDBC_CONNECTION_STRING);

        this.jdbcDriverFileArg =
            this.parser.addHelp(this.parser.addStringOption("jar", "jdbcDriver", true), FDInfoMessage.JDBC_DRIVER);

        this.jdbcDriverClassArg =
            this.parser.addHelp(this.parser.addStringOption("class", "jdbcClass", true), FDInfoMessage.JDBC_CLASS);

        this.loginTimeoutArg =
            this.parser.addHelp(this.parser.addIntegerOption("t", "timeout", false), FDInfoMessage.TIME_OUT);

        this.connRetriesArg =
            this.parser.addHelp(this.parser.addIntegerOption("n", "retry", false), FDInfoMessage.RETRY_CONNECTION);

        this.propArg =
            this.parser.addHelp(this.parser.addStringOption("prop", "addProp", false), FDInfoMessage.ADDITIONAL_PROPS);

        this.tnsNamesORAFileNameArg =
            this.parser.addHelp(this.parser.addStringOption("tns", "tnsName", false), FDInfoMessage.ORACLE_ORA);

        this.scriptFileArg =
            this.parser.addHelp(this.parser.addStringOption("script", "scriptFile", false), FDInfoMessage.SQL_FILE);

        this.queryArg =
            this.parser.addHelp(this.parser.addStringOption("query", "querySQL", false), FDInfoMessage.SQL_QUERY);

        this.scriptSeperatorArg =
            this.parser.addHelp(this.parser.addStringOption("sep", "scriptSeperator", false), FDInfoMessage.SQL_SEPARATOR);

        this.outputSeperatorArg =
            this.parser.addHelp(this.parser.addStringOption("out", "outputSeperator", false), FDInfoMessage.OUTPUT_SEPARATOR);

        this.sqlPasswordArg =
            this.parser.addHelp(this.parser.addStringOption("sqlpwd", "sqlPassword", false), FDInfoMessage.DIRECTLY_SQL_PWD);
    }

    @Override
    public int run(String[] args) throws Exception {
        FDLogger.LOG.entry();
        super.run(args);

        int returnCode = ErrorCodes.OK;

        String userName = this.parser.getOptionValue(this.userArg);
        String password = this.parser.getOptionValue(this.passArg);
        password = Maxim.deMaxim(password);
        String jdbcUrl = this.parser.getOptionValue(this.jdbcConnStrArg);
        String jarFile = this.parser.getOptionValue(this.jdbcDriverFileArg);
        String className = this.parser.getOptionValue(this.jdbcDriverClassArg);
        String sqlPassword = this.parser.getOptionValue(this.sqlPasswordArg);
        if (sqlPassword != null && !sqlPassword.isEmpty()) {
            sqlPassword = Maxim.deMaxim(sqlPassword);
        }
        Integer temp;
        temp = this.parser.getOptionValue(this.loginTimeoutArg);
        if (temp != null) {
            this.connectionInterval = temp;
        }

        temp = this.parser.getOptionValue(this.connRetriesArg);
        if (temp != null) {
            this.nConnRetries = temp;
        }

        String prop = this.parser.getOptionValue(this.propArg);

        String tnsNamesORAFileName = this.parser.getOptionValue(this.tnsNamesORAFileNameArg);
        if (tnsNamesORAFileName != null) {
            // check if the tnsNamesORAFile exists
            File tnsNamesORAFile = new File(tnsNamesORAFileName).getCanonicalFile();
            if (!tnsNamesORAFile.exists()) {
                FDLogger.error("tnsNamesORA file (" + tnsNamesORAFileName + ") does not exist");
                return ErrorCodes.ERROR;
            }
            // load tnsNamesOra file
            System.setProperty("oracle.net.tns_admin", tnsNamesORAFile.getParent());
            FDLogger.info("Using tnsNamesORA file (" + tnsNamesORAFileName + ")");
        }

        // load driver class
        try {
            if (jarFile.trim().length() > 0) {
                FDLogger.info("Loading JAR-File: " + jarFile + " ...");
                if (!new File(jarFile).exists()) {
                    // classpath hacker
                    // load jar file
                    ClasspathHacker.addFile(FileUtility.fileFromUri(new URI(jarFile)));
                }
                URL[] urls = new URL[1];
                urls[0] = new File(jarFile).toURI().toURL();
                URLClassLoader loader = URLClassLoader.newInstance(urls);

                FDLogger.info("Loading Class from JAR-File: " + className + " ...");
                Driver theDriver = (Driver) Class.forName(className, true, loader).newInstance();
                CustomJdbcDriver driver = new CustomJdbcDriver();
                driver.setTheDriver(theDriver);
                DriverManager.registerDriver(driver);
            } else {
                FDLogger.info("Loading Class from Classpath: " + className + " ...");
                Driver driver = (Driver) Class.forName(className).newInstance();
                DriverManager.registerDriver(driver);
            }
            FDLogger.info("Loading successful.");
            // set connection timeout
            if (this.connectionInterval > 0) {
                DriverManager.setLoginTimeout(this.connectionInterval);
            }

        } catch (ClassNotFoundException e) {
            FDLogger.error("Couldn't find driver class", e);
            returnCode = ErrorCodes.ERROR;
            return returnCode;
        }

        // connect to database
        boolean isConnected = false;
        int i = 0;
        FDLogger.info("Connecting to database ...");
        do {
            try {
                java.util.Properties info = new java.util.Properties();
                info.put("user", userName);
                info.put("password", password);
                if (prop != null) {
                    String[] properties = prop.trim().split(";");
                    for (String property : properties) {
                        String[] s = property.trim().split("=");
                        info.put(s[0].trim(), s[1].trim());
                    }
                }
                this.connection = DriverManager.getConnection(jdbcUrl, info);

                this.connection.setAutoCommit(false);

                isConnected = true;
            } catch (SQLException e) {
                ++i;
                FDLogger.LOG.warn("Couldn't connect to database after " + i + " retries", e);
            }
        } while (!(isConnected || i > this.nConnRetries));

        if (!isConnected) {
            FDLogger.error("Failed to connect to database with given information!");
            returnCode = ErrorCodes.ERROR;
            return returnCode;
        }

        String scriptFile = this.parser.getOptionValue(this.scriptFileArg);
        String scriptSeperator = this.parser.getOptionValue(this.scriptSeperatorArg);
        if (scriptSeperator == null || scriptSeperator.trim().isEmpty()) {
            scriptSeperator = ";";
        }
        // execute
        return execute(returnCode, sqlPassword, scriptFile, scriptSeperator);
    }

    private int execute(int returnCode, String sqlPassword, String scriptFile, String scriptSeperator) throws Exception,
        SQLException {
        SQLRunner sqlRunner = null;
        try {
            FDLogger.info("Executing SQL query/script ...");
            sqlRunner = new SQLRunner(this.connection);
            sqlRunner.setStopOnError(true);
            if (scriptFile != null) {
                sqlRunner.setScriptDelimiter(scriptSeperator);
                File tempFile = FileUtility.fileFromUri(new URI(scriptFile));
                if (sqlPassword != null && !sqlPassword.isEmpty()) {
                    try {
                        tempFile = File.createTempFile("SQLScript" + System.currentTimeMillis(), ".sql");
                        tempFile.deleteOnExit();
                        String tempSqlContent =
                            FileUtils.readFileToString(FileUtility.fileFromUri(new URI(scriptFile)));
                        String sqlContent = tempSqlContent.replaceAll("@Password@", sqlPassword);
                        FileUtils.writeStringToFile(tempFile, sqlContent);
                    } catch (IOException e) {
                        FDLogger.LOG.warn("Cannot replace SQLPassword in file URI: " + scriptFile, e);
                    }
                }
                sqlRunner.executeScript(tempFile);

            } else {
                String query = this.parser.getOptionValue(this.queryArg);
                String outputSeperator = this.parser.getOptionValue(this.outputSeperatorArg);
                if (outputSeperator == null) {
                    outputSeperator = "#!#";
                }
                SQLResult result = sqlRunner.executeSQL(query);
                this.printResult(result, outputSeperator);
            }

            sqlRunner.commit();
            FDLogger.info("Executed SQL query/script successfully.");
        } catch (SQLException e) {
            sqlRunner.rollback();
            FDLogger.error(e);
            if (e.getMessage().contains(" exist")) {
                returnCode = ErrorCodes.SQL_EXISTS_ERROR;
            } else {
                returnCode = ErrorCodes.EXCEPTION;
            }

        } catch (URISyntaxException e) {
            FDLogger.error(String.format("Invalid URI file \"%s\" !!!", scriptFile), e);
            returnCode = ErrorCodes.PARAMSMISMATCH;
        } finally {
            if (sqlRunner != null) {
                sqlRunner.close();
            }
        }
        return FDLogger.LOG.exit(returnCode);
    }

    private void printResult(SQLResult result, String outputSeperator) {
        int i = 0;
        FDLogger.info("Processing Resultset ...");
        for (Row row : result.getTable().getRowList()) {
            StringBuffer sb = new StringBuffer();
            sb.append(++i).append(outputSeperator);
            for (Column<String, String> col : row.getColumn()) {
                sb.append(col.getKey()).append('=').append(col.getValue()).append(outputSeperator);
            }
            String str = sb.toString();
            str = str.substring(0, str.length() - outputSeperator.length());
            FDLogger.info(str);
        }
        if (result.getUpdateCount() > 0) {
            FDLogger.info("Processing affected rows ...");
            FDLogger.info(String.format("%d rows updated", result.getUpdateCount()));
            i += result.getUpdateCount();
        }
        FDLogger.info("Affected Rows: " + i);
        FDLogger.info("done.");
    }

}
