package com.freelancer.xwatch.runner;

import java.util.*;

import org.apache.commons.lang3.*;

import com.freelancer.xwatch.utils.*;
import com.google.common.base.*;
import com.google.common.collect.*;

public class HasDirectiveFeatureSelector implements IFeatureSelector {
    private String[] args;
    private Map<String, String> directives = Maps.newHashMap(); // <PackageDirective,
                                                                // PackageFullName>

    /**
     * @param args The first argument is Package Directive, e.g. "FileSystem" mapped to
     *        "com.freelancer.xwatch.io" The second argument is the Feature Name, e.g. "Archive".
     */
    public HasDirectiveFeatureSelector(String[] args) {
        FDLogger.debug("args = " + args == null ? "" : StringUtils.join(args));
        Preconditions.checkNotNull(args, "args cannot be null");
        Preconditions.checkArgument(args.length >= 2, "Package directive and Feature name are required.");

        this.args = args;
    }

    @Override
    public IFeature getExecutingFeature() throws Exception {
        String packageName = this.resolvePackageDirective(this.args[0]);
        String className = String.format("%s.%s", packageName, this.args[1]);

        FDLogger.debug("Loading class, className = " + className);
        Class<?> clazz = Class.forName(className);
        return (IFeature) clazz.newInstance();
    }

    /**
     * Strip off the first two arguments
     */
    @Override
    public String[] getFeatureArguments() {
        // strip off the first two arguments
        return Arrays.copyOfRange(this.args, 2, this.args.length);
    }

    /**
     * Map a directive to a package, directive is case-insensitive.
     */
    public void registerDirective(String directive, String packageFullName) {
        Preconditions.checkNotNull(directive);
        Preconditions.checkNotNull(packageFullName);
        this.directives.put(directive.toLowerCase().trim(), packageFullName);
    }

    /**
     * Get the package name based on the friendly directive name
     */
    private String resolvePackageDirective(String directive) throws Exception {
        String key = StringUtils.trimToEmpty(directive).toLowerCase();
        if (this.directives.containsKey(key)) {
            return this.directives.get(key);
        }

        throw new IllegalArgumentException(String.format("'%s' is not a valid directive.", directive));
    }
}
