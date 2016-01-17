package com.freelancer.xwatch.cli;

import java.io.*;
import java.util.*;

import com.freelancer.xwatch.cli.OptionException.*;
import com.freelancer.xwatch.utils.*;

public class CommandLineParser {

    private String[] remainingArgs;
    private final Map<String, Option<?>> options = new HashMap<String, Option<?>>(10);
    private final Map<String, List<?>> values = new HashMap<String, List<?>>(10);
    private final List<String> optionHelpStringsRequired = new ArrayList<String>();
    private final List<String> optionHelpStringsOptional = new ArrayList<String>();
    private String examples;
    private String description;

    /**
     * Add the specified Option to the list of accepted options
     */
    public final <T> Option<T> addOption(Option<T> opt) {
        if (opt.shortForm() != null) {
            this.options.put("-" + opt.shortForm(), opt);
        }
        this.options.put("--" + opt.longForm(), opt);
        return opt;
    }

    /**
     * Convenience method for adding a string option.
     *
     * @return the new Option
     */
    public final Option<String> addStringOption(String shortForm, String longForm, boolean required) {
        return this.addOption(new Option.StringOption(shortForm, longForm, required));
    }

    /**
     * Convenience method for adding a string option.
     *
     * @return the new Option
     */
    public final Option<String> addStringOption(String longForm, boolean required) {
        return this.addOption(new Option.StringOption(longForm, required));
    }

    /**
     * Convenience method for adding a string option.
     *
     * @return the new Option
     */
    public final Option<String> addPasswordOption(String shortForm, String longForm, boolean required) {
        return this.addOption(new Option.PasswordOption(shortForm, longForm, required));
    }

    /**
     * Convenience method for adding a string option.
     *
     * @return the new Option
     */
    public final Option<String> addPasswordOption(String longForm, boolean required) {
        return this.addOption(new Option.PasswordOption(longForm, required));
    }

    /**
     * Convenience method for adding an integer option.
     *
     * @return the new Option
     */
    public final Option<Integer> addIntegerOption(String shortForm, String longForm, boolean required) {
        return this.addOption(new Option.IntegerOption(shortForm, longForm, required));
    }

    /**
     * Convenience method for adding an integer option.
     *
     * @return the new Option
     */
    public final Option<Integer> addIntegerOption(String longForm, boolean required) {
        return this.addOption(new Option.IntegerOption(longForm, required));
    }

    /**
     * Convenience method for adding a long integer option.
     *
     * @return the new Option
     */
    public final Option<Long> addLongOption(String shortForm, String longForm, boolean required) {
        return this.addOption(new Option.LongOption(shortForm, longForm, required));
    }

    /**
     * Convenience method for adding a long integer option.
     *
     * @return the new Option
     */
    public final Option<Long> addLongOption(String longForm, boolean required) {
        return this.addOption(new Option.LongOption(longForm, required));
    }

    /**
     * Convenience method for adding a double option.
     *
     * @return the new Option
     */
    public final Option<Double> addDoubleOption(String shortForm, String longForm, boolean required) {
        return this.addOption(new Option.DoubleOption(shortForm, longForm, required));
    }

    /**
     * Convenience method for adding a double option.
     *
     * @return the new Option
     */
    public final Option<Double> addDoubleOption(String longForm, boolean required) {
        return this.addOption(new Option.DoubleOption(longForm, required));
    }

    /**
     * Convenience method for adding a boolean option.
     *
     * @return the new Option
     */
    public final Option<Boolean> addBooleanOption(String shortForm, String longForm, boolean required) {
        return this.addOption(new Option.BooleanOption(shortForm, longForm, required));
    }

    /**
     * Convenience method for adding a boolean option.
     *
     * @return the new Option
     */
    public final Option<Boolean> addBooleanOption(String longForm, boolean required) {
        return this.addOption(new Option.BooleanOption(longForm, required));
    }

    /**
     * Equivalent to {@link #getOptionValue(Option, Object) getOptionValue(o, null)}.
     *
     * @throws Exception
     */
    public final <T> T getOptionValue(Option<T> o) throws Exception {
        return this.getOptionValue(o, o.getDefaultValue());
    }

    /**
     * @return the parsed value of the given Option, or the given default 'def' if the option was
     *         not set
     * @throws Exception
     */
    public final <T> T getOptionValue(Option<T> o, T def) throws Exception {
        List<?> v = this.values.get(o.longForm());

        if (v == null) {
            if (o.isRequired()) {
                throw new MissingRequiredOptionException("-" + o.shortForm() + ", --" + o.longForm());
            }
            return def;
        } else if (v.isEmpty()) {
            if (o.isRequired()) {
                throw new MissingRequiredOptionException("-" + o.shortForm() + ", --" + o.longForm());
            }
            if (o.getDefaultValue().toString().equals("true")) {
                return o.getDefaultValue();
            }
            return null;
        } else {

            /*
             * Cast should be safe because Option.parseValue has to return an instance of type T or
             * null
             */
            @SuppressWarnings("unchecked")
            T result = (T) v.remove(0);
            return result;
        }
    }

    /**
     * @return A Collection giving the parsed values of all the occurrences of the given Option, or
     *         an empty Collection if the option was not set.
     * @throws Exception
     */
    public final <T> Collection<T> getOptionValues(Option<T> option) throws Exception {
        Collection<T> result = new ArrayList<T>();

        while (true) {
            T o = this.getOptionValue(option, null);

            if (o == null) {
                return result;
            } else {
                result.add(o);
            }
        }
    }

    /**
     * @return the non-option arguments
     */
    public final String[] getRemainingArgs() {
        return this.remainingArgs;
    }

    /**
     * Extract the options and non-option arguments from the given list of command-line arguments.
     * The default locale is used for parsing options whose values might be locale-specific.
     */
    public final void parse(String[] argv) throws OptionException {
        this.parse(argv, Locale.getDefault());
    }

    /**
     * Extract the options and non-option arguments from the given list of command-line arguments.
     * The specified locale is used for parsing options whose values might be locale-specific.
     */
    public final void parse(String[] argv, Locale locale) throws OptionException {

        List<Object> otherArgs = new ArrayList<Object>();
        int position = 0;
        while (position < argv.length) {
            String curArg = argv[position];
            if (curArg.startsWith("-")) {
                if (curArg.equals("--")) { // end of options
                    position += 1;
                    break;
                }
                String valueArg = null;

                int equalsPos = curArg.indexOf("=");
                if (equalsPos != -1) {
                    valueArg = curArg.substring(equalsPos + 1);
                    curArg = curArg.substring(0, equalsPos);
                }

                Option<?> opt = this.options.get(curArg);
                if (opt == null) {
                    throw new UnknownOptionException(curArg);
                }

                if (opt.wantsValue()) {
                    if (valueArg == null) {
                        position += 1;
                        if (position < argv.length) {
                            valueArg = argv[position];
                        }
                    }

                    this.addValue(opt, valueArg, locale);
                } else {
                    this.addValue(opt, null, locale);
                }

                position += 1;
            } else {
                otherArgs.add(curArg);
                position += 1;
            }
        }
        for (; position < argv.length; ++position) {
            otherArgs.add(argv[position]);
        }

        this.remainingArgs = new String[otherArgs.size()];
        this.remainingArgs = otherArgs.toArray(this.remainingArgs);
    }

    private <T> void addValue(Option<T> opt, String valueArg, Locale locale) throws IllegalOptionValueException {

        T value = opt.getValue(valueArg, locale);
        String lf = opt.longForm();

        /*
         * Cast is typesafe because the only location we add elements to the values map is in this
         * method.
         */
        @SuppressWarnings("unchecked")
        List<T> v = (List<T>) this.values.get(lf);

        if (v == null) {
            v = new ArrayList<T>();
            this.values.put(lf, v);
        }

        v.add(value);
    }

    public <T> Option<T> addHelp(Option<T> option, String helpString) {
        if (option.isRequired()) {
            this.optionHelpStringsRequired.add("\t-" + option.shortForm() + ", --" + option.longForm() + "\r\n\t\t"
                + helpString + "\r\n");
        } else {
            this.optionHelpStringsOptional.add("\t-" + option.shortForm() + ", --" + option.longForm() + "\r\n\t\t"
                + helpString + "\r\n");
        }
        return option;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the name of the executing jar file. If this is run in Eclipse, the result will be
     *         "classes"!
     */
    private String getCurrentJarFile() {
        try {
            File jar =
                new File(CommandLineParser.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            return jar.getName();

        } catch (Exception e) {
            return "YourJarTool.jar";
        }
    }

    public void printUsage(String packageName, String feature) {
        this.printDescription();

        FDLogger.info("\r\nUSAGE: java -jar " + this.getCurrentJarFile() + " " + packageName + " " + feature
            + " [arguments]");

        if (this.optionHelpStringsRequired.size() > 0) {
            FDLogger.info("\r\nREQUIRED ARGUMENTS:\r\n");
        }

        for (String help : this.optionHelpStringsRequired) {
            FDLogger.info(help);
        }

        if (this.optionHelpStringsOptional.size() > 0) {
            FDLogger.info("\r\nOPTIONAL ARGUMENTS:\r\n");
        }

        for (String help : this.optionHelpStringsOptional) {
            FDLogger.info(help);
        }

        if (this.examples != null && this.examples.length() > 0) {
            FDLogger.info("\r\nEXAMPLES:\r\n");
            FDLogger.info(this.examples);
        }
    }

    public void printDescription() {
        this.printDescription(false);
    }

    public void printDescription(boolean isOneliner) {
        if (this.description != null && this.description.length() > 0) {
            if (!isOneliner) {
                FDLogger.info("\r\nDESCRIPTION:\r\n");
                FDLogger.info(this.description);
            } else {
                FDLogger.info(" - " + this.description);
            }
        }
    }

    public void printParams() {
        FDLogger.debug("Print input parameters:");
        Set<String> isPrinted = new HashSet<String>();
        for (String argument : this.options.keySet()) {

            if (!(this.options.get(argument) instanceof Option.PasswordOption)) {
                String key = this.options.get(argument).longForm();

                if (!isPrinted.add(key)) {
                    continue; // Prevent the info being showed twice (long form,
                              // short form)
                }

                String value = "";

                if (this.values.containsKey(key)) {
                    value = this.values.get(key).toString();
                }

                // Remove first '[' and last ']'
                if (value.length() > 2) {
                    value = value.substring(1, value.length() - 1);
                }
                FDLogger.debug(key + " = " + value);
                // FDLogger.info(key + " = " + value, MsgTypes.PARAM); tqt will
                // fix later
            }
        }
    }
}
