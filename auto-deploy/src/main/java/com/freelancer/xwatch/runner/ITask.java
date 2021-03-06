package com.freelancer.xwatch.runner;

public interface ITask {

    void initialize();

    int run(String[] args) throws Exception;

    void finalize(int errorCode);

    void printUsage();

    void printDescription(boolean isOneliner);
}
