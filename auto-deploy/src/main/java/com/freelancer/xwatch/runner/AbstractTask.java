package com.freelancer.xwatch.runner;

import com.freelancer.xwatch.cli.*;
import com.freelancer.xwatch.utils.*;

public abstract class AbstractTask implements ITask {

    protected final CommandLineParser parser = new CommandLineParser();
    protected Option<String> logLevelArg;
    private String baseLoggerName = "com.freelancer.xwatch";

    @Override
    public void initialize() {
        this.logLevelArg =
            this.parser.addHelp(this.parser.addStringOption("ll", "loglevel", false), "The custom log level to be used (OFF, TRACE, DEBUG, INFO, WARN, ERROR, FATAL).");
    }

    @Override
    public int run(String[] args) throws Exception {

        FDLogger.info("STARTED: " + this.getClass().getName());

        // parse arguments - if parameter ar not valid an excption is thrown
        this.parser.parse(args);
        this.parser.printParams();

        LogUtils.configureLogLevel(this.parser.getOptionValue(this.logLevelArg), this.baseLoggerName);
        return ErrorCodes.OK;
    }

    @Override
    public void finalize(int errorCode) {
        // log footer
        FDLogger.info("ENDED: " + this.getClass().getName());
    }

    @Override
    public void printUsage() {
        this.parser.printUsage(this.getPackageNameFromClass(), this.getClass().getSimpleName());
    }

    @Override
    public void printDescription(boolean isOneliner) {
        this.parser.printDescription(isOneliner);
    }

    private String getPackageNameFromClass() {
        String className = this.getClass().getName();
        int index = className.lastIndexOf('.');
        if (index > 0) {
            return className.substring(className.lastIndexOf('.', index - 1) + 1, index);
        }

        return "";
    }

    public String getBaseLoggerName() {
        return this.baseLoggerName;
    }

    public void setBaseLoggerName(String baseLoggerName) {
        this.baseLoggerName = baseLoggerName;
    }

}
