package com.freelancer.xwatch.runner;

import com.freelancer.xwatch.cli.*;
import com.freelancer.xwatch.utils.*;

public class FeatureRunner {
    private final IFeatureSelector featureSelector;

    public FeatureRunner(IFeatureSelector featureSelector) {
        this.featureSelector = featureSelector;
    }

    public int run() {
        int errorCode = ErrorCodes.OK;

        IFeature feature = null;
        try {
            feature = this.featureSelector.getExecutingFeature();
            feature.initialize();
            errorCode = feature.run(this.featureSelector.getFeatureArguments());

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
