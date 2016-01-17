package com.freelancer.xwatch;

import com.freelancer.xwatch.database.*;
import com.freelancer.xwatch.runner.*;
import com.freelancer.xwatch.utils.*;

public final class Main {

    public static void main(String[] args) {
        try {
            ManifestHelper.printVersion(Main.class, FDLogger.LOG);
            HasDirectiveFeatureSelector featureSelector = new HasDirectiveFeatureSelector(args);

            featureSelector.registerDirective("database", SQLFeature.class.getPackage().getName());

            FeatureRunner featureRunner = new FeatureRunner(featureSelector);
            System.exit(featureRunner.run());

        } catch (Exception e) {
            FDLogger.error(e);
            System.exit(ErrorCodes.ERROR);
        }
    }
}
