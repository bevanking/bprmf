package com.echo8.bprmf.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

import com.echo8.bprmf.BPRMF;
import com.echo8.bprmf.data.CsvDataFileFormat;
import com.echo8.bprmf.data.DataFileFormat;
import com.echo8.bprmf.data.DataLoader;
import com.echo8.bprmf.data.MovieLensDataFileFormat;

public class Main {

    public static void main(String[] args) throws Exception {
        Options options = CommandLineOptions.get();
        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(CommandLineOptions.OPTION_HELP)) {
            printHelp(options);
            return;
        }

        BPRMF bprmf = getBprmf(cmd);

        if (cmd.hasOption(CommandLineOptions.OPTION_TRAIN)
                && !cmd.hasOption(CommandLineOptions.OPTION_RECOMMEND)) {
            File trainFile = new File(cmd.getOptionValue(CommandLineOptions.OPTION_TRAIN));
            DataFileFormat dataFileFormat = getTrainFileFormat(cmd);
            File modelFile = getOutputFile(cmd);

            bprmf.setFeedbackData(DataLoader.loadFromFile(trainFile, dataFileFormat));
            bprmf.train();
            bprmf.save(new ObjectOutputStream(new FileOutputStream(modelFile)));
        } else if (cmd.hasOption(CommandLineOptions.OPTION_RECOMMEND)
                && !cmd.hasOption(CommandLineOptions.OPTION_TRAIN)) {
            List<String> rawUserIdList = getRawUserIdList(cmd);

            File modelFile = getModelFile(cmd);
            File resultFile = getOutputFile(cmd);

            bprmf.load(new ObjectInputStream(new FileInputStream(modelFile)));

            PrintWriter writer = new PrintWriter(resultFile);

            for (String rawUserId : rawUserIdList) {
                Integer userId = bprmf.getFeedbackData().rawUserIdToUserId(rawUserId);

                if (userId != null) {
                    for (int i = 0; i < bprmf.getFeedbackData().getNumItems(); i++) {
                        float score = bprmf.predict(userId, i);
                        writer.println(String.format("%s,%s,%s", rawUserId, bprmf
                                .getFeedbackData().getRawItemId(i), score));
                    }
                }
            }

            writer.close();
        } else {
            throw new ParseException(
                    "You must run bprmf with either the '-train' or '-recommend' option.");
        }
    }

    private static List<String> getRawUserIdList(CommandLine cmd) throws IOException {
        return FileUtils
                .readLines(new File(cmd.getOptionValue(CommandLineOptions.OPTION_RECOMMEND)));
    }

    private static File getModelFile(CommandLine cmd) throws ParseException {
        String modelFilePath = cmd.getOptionValue(CommandLineOptions.OPTION_MODEL);
        if (modelFilePath != null) {
            return new File(modelFilePath);
        } else {
            throw new ParseException(
                    "You must use the '-model' option to specify the model file used when making recommendations.");
        }
    }

    private static File getOutputFile(CommandLine cmd) throws ParseException {
        String outputFilePath = cmd.getOptionValue(CommandLineOptions.OPTION_OUTPUT);
        if (outputFilePath != null) {
            return new File(outputFilePath);
        } else {
            throw new ParseException(
                    "You must use the '-output' option to specify where the model/results will be saved to.");
        }
    }

    private static DataFileFormat getTrainFileFormat(CommandLine cmd) throws ParseException {
        String trainFileFormat = cmd.getOptionValue(CommandLineOptions.OPTION_TRAINFORMAT);
        if (trainFileFormat != null) {
            switch (trainFileFormat) {
                case "csv":
                    return new CsvDataFileFormat();
                case "movielens":
                    return new MovieLensDataFileFormat();
                default:
                    throw new ParseException("Did not recognize this train file format: "
                            + trainFileFormat);
            }
        } else {
            return new CsvDataFileFormat();
        }
    }

    private static BPRMF getBprmf(CommandLine cmd) {
        BPRMF bprmf = new BPRMF();

        String learnRate = cmd.getOptionValue(CommandLineOptions.OPTION_LEARNRATE);
        if (learnRate != null) {
            bprmf.setLearnRate(Float.parseFloat(learnRate));
        }

        String iters = cmd.getOptionValue(CommandLineOptions.OPTION_ITERS);
        if (iters != null) {
            bprmf.setNumIterations(Integer.parseInt(iters));
        }

        String factors = cmd.getOptionValue(CommandLineOptions.OPTION_FACTORS);
        if (factors != null) {
            bprmf.setNumFactors(Integer.parseInt(factors));
        }

        String regBias = cmd.getOptionValue(CommandLineOptions.OPTION_REGBIAS);
        if (regBias != null) {
            bprmf.setRegBias(Float.parseFloat(regBias));
        }

        String regU = cmd.getOptionValue(CommandLineOptions.OPTION_REGU);
        if (regU != null) {
            bprmf.setRegU(Float.parseFloat(regU));
        }

        String regI = cmd.getOptionValue(CommandLineOptions.OPTION_REGI);
        if (regI != null) {
            bprmf.setRegI(Float.parseFloat(regI));
        }

        String regJ = cmd.getOptionValue(CommandLineOptions.OPTION_REGJ);
        if (regJ != null) {
            bprmf.setRegJ(Float.parseFloat(regJ));
        }

        if (cmd.hasOption(CommandLineOptions.OPTION_SKIPJUPDATE)) {
            bprmf.setUpdateJ(false);
        }

        return bprmf;
    }

    private static void printHelp(Options options) {
        new HelpFormatter().printHelp("bprmf", options);
    }
}
