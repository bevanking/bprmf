package com.echo8.bprmf.main;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import com.echo8.bprmf.conf.Defaults;

public class CommandLineOptions {
    public static final String HELP_OPTION = "help";
    public static final String TRAIN_OPTION = "train";
    public static final String RECOMMEND_OPTION = "recommend";
    public static final String OUTPUT_OPTION = "output";
    public static final String MODEL_OPTION = "model";
    public static final String TRAINFORMAT_OPTION = "trainformat";
    public static final String LEARNRATE_OPTION = "learnrate";
    public static final String ITERS_OPTION = "iters";
    public static final String FACTORS_OPTION = "factors";
    public static final String REGBIAS_OPTION = "regbias";
    public static final String REGU_OPTION = "regu";
    public static final String REGI_OPTION = "regi";
    public static final String REGJ_OPTION = "regj";
    public static final String SKIPJUPDATE_OPTION = "skipjupdate";

    @SuppressWarnings("static-access")
    public static Options get() {
        Options options = new Options();

        options.addOption(HELP_OPTION, false, "print this message");

        options.addOption(OptionBuilder
            .withArgName("file")
            .hasArg()
            .withDescription("train model with given file")
            .create(TRAIN_OPTION));

        options
            .addOption(OptionBuilder
                .withArgName("file")
                .hasArg()
                .withDescription(
                    "generate recommendations for users in given file")
                .create(RECOMMEND_OPTION));

        options.addOption(OptionBuilder
            .withArgName("file")
            .hasArg()
            .withDescription("output the model/recommendations to given file")
            .create(OUTPUT_OPTION));

        options.addOption(OptionBuilder
            .withArgName("file")
            .hasArg()
            .withDescription("model file to use when making recommendations")
            .create(MODEL_OPTION));

        options.addOption(OptionBuilder
            .withArgName("format")
            .hasArg()
            .withDescription(
                "format of training file: csv (default) or movielens")
            .create(TRAINFORMAT_OPTION));

        options.addOption(OptionBuilder
            .withArgName("value")
            .hasArg()
            .withDescription(
                String.format(
                    "set learn rate (default: %s)",
                    Defaults.DEFAULT_LEARN_RATE))
            .create(LEARNRATE_OPTION));

        options.addOption(OptionBuilder
            .withArgName("value")
            .hasArg()
            .withDescription(
                String.format(
                    "set number of training iterations (default: %s)",
                    Defaults.DEFAULT_NUM_ITERATIONS))
            .create(ITERS_OPTION));

        options.addOption(OptionBuilder
            .withArgName("value")
            .hasArg()
            .withDescription(
                String.format(
                    "set number of factors (default: %s)",
                    Defaults.DEFAULT_NUM_FACTORS))
            .create(FACTORS_OPTION));

        options.addOption(OptionBuilder
            .withArgName("value")
            .hasArg()
            .withDescription(
                String.format(
                    "set item bias regularization (default: %s)",
                    Defaults.DEFAULT_REG_BIAS))
            .create(REGBIAS_OPTION));

        options.addOption(OptionBuilder
            .withArgName("value")
            .hasArg()
            .withDescription(
                String.format(
                    "set user update regularization (default: %s)",
                    Defaults.DEFAULT_REG_U))
            .create(REGU_OPTION));

        options.addOption(OptionBuilder
            .withArgName("value")
            .hasArg()
            .withDescription(
                String.format(
                    "set item i update regularization (default: %s)",
                    Defaults.DEFAULT_REG_I))
            .create(REGI_OPTION));

        options.addOption(OptionBuilder
            .withArgName("value")
            .hasArg()
            .withDescription(
                String.format(
                    "set item j update regularization (default: %.5f)",
                    Defaults.DEFAULT_REG_J))
            .create(REGJ_OPTION));

        options.addOption(
            SKIPJUPDATE_OPTION,
            false,
            "skip the update for item j");

        return options;
    }
}
