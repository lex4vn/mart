package com.freelancer.xwatch.utils;

import org.apache.logging.log4j.*;

import com.freelancer.xwatch.utils.strings.*;

public final class FDLogger {

    public final static Logger LOG = LogManager.getLogger();

    private FDLogger() {}

    public final static void info(String message) {
        FDLogger.LOG.info(message);
    }

    public final static void debug(String message) {
        FDLogger.LOG.debug(message);
    }

    public final static void debug(String message, Throwable t) {
        if (LOG.isDebugEnabled()) {
            FDLogger.LOG.debug(StringUtils.isEmpty(message) ? "Unexpected error" : message, t);
        }
    }

    public static void debug(Throwable t) {
        FDLogger.debug("Unexpected error", t);
    }

    public final static void error(String message) {
        FDLogger.LOG.error(message);
    }

    public final static void error(String message, Throwable t) {
        FDLogger.LOG.error(message, t);
    }

    public static void error(Throwable t) {
        FDLogger.LOG.error(t);
    }

}
