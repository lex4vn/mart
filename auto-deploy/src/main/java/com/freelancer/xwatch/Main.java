package com.freelancer.xwatch;

import com.freelancer.xwatch.database.*;
import com.freelancer.xwatch.php.*;
import com.freelancer.xwatch.runner.*;
import com.freelancer.xwatch.utils.*;

public final class Main {

    public static void main(String[] args) {
        try {
            ManifestHelper.printVersion(Main.class, FDLogger.LOG);
            HasDirectiveTaskSelector featureSelector = new HasDirectiveTaskSelector(args);
            featureSelector.registerDirective("database", SQLTask.class.getPackage().getName());
            featureSelector.registerDirective("php", Install.class.getPackage().getName());
            TaskRunner featureRunner = new TaskRunner(featureSelector);
            System.exit(featureRunner.run());

        } catch (Exception e) {
            FDLogger.error(e);
            System.exit(ErrorCodes.ERROR);
        }
    }
}
