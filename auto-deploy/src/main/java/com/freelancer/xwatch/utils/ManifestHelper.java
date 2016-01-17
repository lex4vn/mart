package com.freelancer.xwatch.utils;

import org.apache.logging.log4j.*;

public class ManifestHelper {

    public static void printVersion(Class<?> clazz, Logger logger) {
        Package objPackage = clazz.getPackage();
        String name = objPackage.getImplementationTitle();
        String version = objPackage.getImplementationVersion();
        logger.info(String.format("Package: %s. Version: %s.", name, version));
    }
}
