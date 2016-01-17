package com.freelancer.xwatch.utils;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.apache.commons.lang3.*;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.*;
import org.apache.logging.log4j.core.config.*;

public class LogUtils {
    private static final boolean IS_UNIX = !System.getProperty("os.name").toLowerCase().contains("windows");

    /**
     * Set custom log level for the specified logger and update all related logger instances
     */
    public static void configureLogLevel(String customLevel, String loggerName) {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        if (IS_UNIX) {
            final Map<String, Appender> map = ctx.getConfiguration().getAppenders();
            for (String key : map.keySet()) {
                Appender appender = map.get(key);
                String fileName = null;

                if (appender instanceof RollingFileAppender) {
                    fileName = ((RollingFileAppender) appender).getFileName();
                } else if (appender instanceof FileAppender) {
                    fileName = ((FileAppender) appender).getFileName();
                } else if (appender instanceof FileManager) {
                    fileName = ((FileManager) appender).getFileName();
                } else if (appender instanceof RandomAccessFileAppender) {
                    fileName = ((RandomAccessFileAppender) appender).getFileName();
                } else if (appender instanceof RandomAccessFileManager) {
                    fileName = ((RandomAccessFileManager) appender).getFileName();
                } else if (appender instanceof RollingRandomAccessFileAppender) {
                    fileName = ((RollingRandomAccessFileAppender) appender).getFileName();
                }

                if (fileName != null) {
                    File logFile = new File(fileName);
                    if (logFile.exists()) {

                        try {
                            Class<?> fspClass = Class.forName("java.util.prefs.FileSystemPreferences");

                            Method chmodMethod = fspClass.getDeclaredMethod("chmod", String.class, Integer.TYPE);
                            chmodMethod.setAccessible(true);
                            chmodMethod.invoke(null, logFile.getCanonicalPath(), 0777);
                            chmodMethod.invoke(null, logFile.getParentFile().getCanonicalPath(), 0777);

                        } catch (Exception e) {

                        }
                    }
                }
            }
        }

        if (!StringUtils.isEmpty(customLevel)) {
            FDLogger.LOG.warn("Custom log level: " + customLevel);
            try {
                Level level = Level.getLevel(customLevel);
                if (level == null) {
                    FDLogger.error("Invalid custom LogLevel " + customLevel);
                    return;
                }
                LoggerConfig loggerConfig = config.getLoggerConfig(loggerName);
                loggerConfig.setLevel(level);
                ctx.updateLoggers();

                FDLogger.debug(String.format("Set level %s for logger %s", customLevel, loggerName));

            } catch (Exception e) {
                FDLogger.error(String.format("Failed to set custom log level: %s.", customLevel), e);
            }
        }
    }
}
