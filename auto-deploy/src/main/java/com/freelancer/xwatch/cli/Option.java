package com.freelancer.xwatch.cli;

import java.text.*;
import java.util.*;

import org.apache.commons.lang3.*;

import com.freelancer.xwatch.cli.OptionException.*;
import com.freelancer.xwatch.crypto.*;

/**
 * Representation of a command-line option
 *
 * @param T Type of data configured by this option
 */
public abstract class Option<T> {

    private final String shortForm;
    private final String longForm;
    private final boolean wantsValue;
    private final boolean required;

    protected Option(String longForm, boolean wantsValue, boolean required) {
        this(null, longForm, wantsValue, required);
    }

    private Option(String shortForm, String longForm, boolean wantsValue, boolean required) {
        if (longForm == null) {
            throw new IllegalArgumentException("Null longForm not allowed");
        }
        this.shortForm = shortForm;
        this.longForm = longForm;
        this.wantsValue = wantsValue;
        this.required = required;
    }

    public boolean isRequired() {
        return this.required;
    }

    public String shortForm() {
        return this.shortForm;
    }

    public String longForm() {
        return this.longForm;
    }

    /**
     * Tells whether or not this option wants a value
     */
    public boolean wantsValue() {
        return this.wantsValue;
    }

    public final T getValue(String arg, Locale locale) throws IllegalOptionValueException {
        if (this.wantsValue) {
            if (arg == null) {
                throw new IllegalOptionValueException(this, "");
            }
        }
        return this.parseValue(arg, locale);
    }

    /**
     * Override to extract and convert an option value passed on the command-line
     */
    protected T parseValue(String arg, Locale locale) throws IllegalOptionValueException {

        return null;
    }

    /**
     * Override to define default value returned by getValue if option does not want a value
     */
    protected T getDefaultValue() {
        return null;
    }

    protected void guardRequiredValue(String arg) {
        if (this.isRequired() && StringUtils.isBlank(arg)) {
            throw new MissingRequiredOptionException("-" + this.shortForm() + ", --" + this.longForm());
        }
    }

    /**
     * An option that expects a boolean value
     */
    public static class BooleanOption extends Option<Boolean> {
        public BooleanOption(String shortForm, String longForm, boolean required) {
            super(shortForm, longForm, true, required);
        }

        public BooleanOption(String longForm, boolean required) {
            super(longForm, true, required);
        }

        @Override
        public Boolean parseValue(String arg, Locale lcoale) {
            this.guardRequiredValue(arg);
            if (StringUtils.isBlank(arg) && !this.isRequired()) {
                return null;
            }

            try {
                return Boolean.parseBoolean(arg);
            } catch (Exception e) {
                throw new IllegalOptionValueException(this, arg);
            }
        }

        @Override
        public Boolean getDefaultValue() {
            return null;
        }
    }

    /**
     * An option that expects an integer value
     */
    public static class IntegerOption extends Option<Integer> {
        public IntegerOption(String shortForm, String longForm, boolean required) {
            super(shortForm, longForm, true, required);
        }

        public IntegerOption(String longForm, boolean required) {
            super(longForm, true, required);
        }

        @Override
        protected Integer parseValue(String arg, Locale locale) throws IllegalOptionValueException {
            if (StringUtils.isBlank(arg) && !this.isRequired()) {
                return null;
            }
            try {
                return new Integer(arg);
            } catch (NumberFormatException e) {
                throw new IllegalOptionValueException(this, arg);
            }
        }
    }

    /**
     * An option that expects a long integer value
     */
    public static class LongOption extends Option<Long> {
        public LongOption(String shortForm, String longForm, boolean required) {
            super(shortForm, longForm, true, required);
        }

        public LongOption(String longForm, boolean required) {
            super(longForm, true, required);
        }

        @Override
        protected Long parseValue(String arg, Locale locale) throws IllegalOptionValueException {
            if (StringUtils.isBlank(arg) && !this.isRequired()) {
                return null;
            }
            try {
                return new Long(arg);
            } catch (NumberFormatException e) {
                throw new IllegalOptionValueException(this, arg);
            }
        }
    }

    /**
     * An option that expects a floating-point value
     */
    public static class DoubleOption extends Option<Double> {
        public DoubleOption(String shortForm, String longForm, boolean required) {
            super(shortForm, longForm, true, required);
        }

        public DoubleOption(String longForm, boolean required) {
            super(longForm, true, required);
        }

        @Override
        protected Double parseValue(String arg, Locale locale) throws IllegalOptionValueException {
            if (StringUtils.isBlank(arg) && !this.isRequired()) {
                return null;
            }
            try {
                NumberFormat format = NumberFormat.getNumberInstance(locale);
                Number num = format.parse(arg);
                return new Double(num.doubleValue());
            } catch (ParseException e) {
                throw new IllegalOptionValueException(this, arg);
            }
        }
    }

    /**
     * An option that expects a string value
     */
    public static class StringOption extends Option<String> {
        public StringOption(String shortForm, String longForm, boolean required) {
            super(shortForm, longForm, true, required);
        }

        public StringOption(String longForm, boolean required) {
            super(longForm, true, required);
        }

        @Override
        protected String parseValue(String arg, Locale locale) {
            this.guardRequiredValue(arg);
            return arg;
        }
    }

    /**
     * An option that expects a password value
     */
    public static class PasswordOption extends Option<String> {
        public PasswordOption(String shortForm, String longForm, boolean required) {
            super(shortForm, longForm, true, required);
        }

        public PasswordOption(String longForm, boolean required) {
            super(longForm, true, required);
        }

        @Override
        protected String parseValue(String arg, Locale locale) {
            this.guardRequiredValue(arg);
            return Maxim.deMaxim(arg);
        }
    }

    /**
     * An optional option that expects no value
     */
    public static class SwitchOption extends Option<Boolean> {
        public SwitchOption(String shortForm, String longForm) {
            super(shortForm, longForm, false, false);
        }

        public SwitchOption(String longForm) {
            super(longForm, false, false);
        }

        @Override
        public Boolean parseValue(String arg, Locale lcoale) {
            return Boolean.TRUE; // the switch is present
        }

        @Override
        public Boolean getDefaultValue() {
            return Boolean.FALSE; // the switch is not present
        }
    }
}
