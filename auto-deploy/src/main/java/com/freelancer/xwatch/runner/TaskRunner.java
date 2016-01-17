package com.freelancer.xwatch.runner;

import com.freelancer.xwatch.cli.*;
import com.freelancer.xwatch.utils.*;

public class TaskRunner {
    private final ITaskSelector taskSelector;

    public TaskRunner(ITaskSelector featureSelector) {
        this.taskSelector = featureSelector;
    }

    public int run() {
        int errorCode = ErrorCodes.OK;

        ITask feature = null;
        try {
            feature = this.taskSelector.getExecutingFeature();
            feature.initialize();
            errorCode = feature.run(this.taskSelector.getFeatureArguments());

        } catch (OptionException e) {
            FDLogger.error(e.getMessage(), e);
            feature.printUsage();
            errorCode = ErrorCodes.ERROR;

        } catch (ClassNotFoundException c) {
            FDLogger.error(c);
            errorCode = ErrorCodes.ERROR;

        } catch (Exception e) {
            FDLogger.error("Feature ended with an exception: ", e);
            errorCode = ErrorCodes.ERROR;

        } finally {
            if (feature != null) {
                feature.finalize(errorCode);
            }
        }

        return errorCode;
    }
}
