package com.freelancer.xwatch.cli;

/**
 * Base class for exceptions that may be thrown when options are parsed
 */
public abstract class OptionException extends RuntimeException {
    /**
         *
         */
    private static final long serialVersionUID = 5905355350760607807L;

    OptionException(String msg) {
        super(msg);
    }

    /**
     * Thrown when the parsed command-line contains an option that is not recognised.
     * <code>getMessage()</code> returns an error string suitable for reporting the error to the
     * user (in English).
     */
    public static class MissingRequiredOptionException extends OptionException {
        /**
             *
             */
        private static final long serialVersionUID = 7016709763855361146L;

        /**
             *
             */

        MissingRequiredOptionException(String optionName) {
            this(optionName, "Missing required option '" + optionName + "'");
        }

        MissingRequiredOptionException(String optionName, String msg) {
            super(msg);
            this.optionName = optionName;
        }

        /**
         * @return the name of the option that was unknown (e.g. "-u")
         */
        public String getOptionName() {
            return this.optionName;
        }

        private final String optionName;
    }

    /**
     * Thrown when the parsed command-line contains an option that is not recognised.
     * <code>getMessage()</code> returns an error string suitable for reporting the error to the
     * user (in English).
     */
    public static class UnknownOptionException extends OptionException {
        /**
             *
             */
        private static final long serialVersionUID = 1462366625321918437L;

        UnknownOptionException(String optionName) {
            this(optionName, "Unknown option '" + optionName + "'");
        }

        UnknownOptionException(String optionName, String msg) {
            super(msg);
            this.optionName = optionName;
        }

        /**
         * @return the name of the option that was unknown (e.g. "-u")
         */
        public String getOptionName() {
            return this.optionName;
        }

        private final String optionName;
    }

    /**
     * Thrown when the parsed commandline contains multiple concatenated short options, such as
     * -abcd, where one is unknown. <code>getMessage()</code> returns an english human-readable
     * error string.
     *
     * @author Vidar Holen
     */
    public static class UnknownSuboptionException extends UnknownOptionException {
        /**
             *
             */
        private static final long serialVersionUID = -8218894447958641383L;
        private char suboption;

        UnknownSuboptionException(String option, char suboption) {
            super(option, "Illegal option: '" + suboption + "' in '" + option + "'");
            this.suboption = suboption;
        }

        public char getSuboption() {
            return this.suboption;
        }
    }

    /**
     * Thrown when the parsed commandline contains multiple concatenated short options, such as
     * -abcd, where one or more requires a value. <code>getMessage()</code> returns an english
     * human-readable error string.
     *
     * @author Vidar Holen
     */
    public static class NotFlagException extends UnknownOptionException {
        /**
             *
             */
        private static final long serialVersionUID = 2788326387242279539L;
        private char notflag;

        NotFlagException(String option, char unflaggish) {
            super(option, "Illegal option: '" + option + "', '" + unflaggish + "' requires a value");
            this.notflag = unflaggish;
        }

        /**
         * @return the first character which wasn't a boolean (e.g 'c')
         */
        public char getOptionChar() {
            return this.notflag;
        }
    }

    /**
     * Thrown when an illegal or missing value is given by the user for an option that takes a
     * value. <code>getMessage()</code> returns an error string suitable for reporting the error to
     * the user (in English).
     *
     * No generic class can ever extend <code>java.lang.Throwable</code>, so we have to return
     * <code>Option&lt;?&gt;</code> instead of <code>Option&lt;T&gt;</code>.
     */
    public static class IllegalOptionValueException extends OptionException {

        /**
             *
             */
        private static final long serialVersionUID = -6702257234442433694L;

        public <T> IllegalOptionValueException(Option<T> opt, String value) {
            super("Illegal value '" + value + "' for option " + (opt.shortForm() != null ? "-" + opt.shortForm() + ", "
                : "") + "--" + opt.longForm());
            this.option = opt;
            this.value = value;
        }

        /**
         * @return the name of the option whose value was illegal (e.g. "-u")
         */
        public Option<?> getOption() {
            return this.option;
        }

        /**
         * @return the illegal value
         */
        public String getValue() {
            return this.value;
        }

        private final Option<?> option;
        private final String value;
    }

}
