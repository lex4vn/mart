package com.freelancer.xwatch.utils;

public class ErrorCodes {
    /**
     * * General Error-Code for successful processing.
     */
    public final static int OK = 0;

    /**
     * The Constant WARNING.
     */
    public final static int WARNING = 2;

    /**
     * The Constant ERROR.
     */
    public final static int ERROR = 4;

    /**
     * The Constant SEVERE.
     */
    public final static int SEVERE = 16;

    /**
     * The Constant PARAMSMISMATCH specifies a mismatching number of parameters or a wrong/invalid
     * parameter.
     */
    public final static int PARAMSMISMATCH = 32;

    /**
     * The Constant EXCEPTION. TODO Errorcodes should be less or equals 255 since some OS cannot
     * handle higher values
     */
    public final static int EXCEPTION = 999;

    /**
     * * Application specific Error-Codes.
     */
    // FCM XML
    public final static int XML_OK = 0;

    /**
     * The Constant XPATH_OK.
     */
    public final static int XPATH_OK = 0;
    // FCM Text
    /**
     * The Constant TEXT_OK.
     */
    public final static int TEXT_OK = 0;

    public final static int SQL_EXISTS_ERROR = 12;
}
