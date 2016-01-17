package com.freelancer.xwatch.runner;

public interface IFeatureSelector {

    /**
     * @return the arguments to be used in the feature after excluding directives.
     */
    public String[] getFeatureArguments();

    /**
     * @return the Feature to be executed.
     * @throws Exception
     */
    public IFeature getExecutingFeature() throws Exception;

}
