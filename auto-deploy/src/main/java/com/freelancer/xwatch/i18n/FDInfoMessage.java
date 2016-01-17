package com.freelancer.xwatch.i18n;

public final class FDInfoMessage {

    public static final String DIRECTLY_SQL_PWD = "The password which will be used directly in SQL statement";
    public static final String OUTPUT_SEPARATOR = "If a query returns multiple values, this parameter defines which string should be used to separate the columns of a returned results.";
    public static final String SQL_SEPARATOR = "If you execute a script containing multiple queries/statements, JDBCTOOL executes them statement-by-statement. The statements in the scripts are usually separated by ';' or similar. For MSSQL usually 'GO' is used.";
    public static final String SQL_QUERY = "SQL-query to execute (e.g. 'SELECT * FROM myTable')";
    public static final String SQL_FILE = "The script file will be executed";
    public static final String ORACLE_ORA = "Points to the tnsnames.ora file (if available). Parameter has only effect, when type = 'ORACLE-THIN'/'ORACLE-OCI' and parameter -usetns is used at the command line. Connection method to Oracle databases using the tnsnames.ora.";
    public static final String ADDITIONAL_PROPS = "Additional property=value pairs used when connecting via JDBC";
    public static final String RETRY_CONNECTION = "The number of connection attempts before aborting";
    public static final String TIME_OUT = "Time interval in seconds during which JDBCTOOL.jar tries to establish a connection to the database.";
    public static final String JDBC_CLASS = "Name of the class to load when initializing the JDBC connection. E.g 'com.microsoft.sqlserver.jdbc.SQLServerDriver'";
    public static final String JDBC_DRIVER = "URI pointing to the vendor-specific JDBC jar-file. E.g 'file:///C:/Automic/Agents/windows/bin/sqljdbc4.jar'";
    public static final String JDBC_CONNECTION_STRING = "JDBC connection string. E.g.: jdbc:sqlserver://myServer:1433;databaseName=myDB";
    public static final String DATABASE_PASSWORD = "The database password";
    public static final String DATABASE_USER = "The database user name";

}
