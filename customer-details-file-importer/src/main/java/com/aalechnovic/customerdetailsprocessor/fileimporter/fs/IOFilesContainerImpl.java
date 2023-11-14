package com.aalechnovic.customerdetailsprocessor.fileimporter.fs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class IOFilesContainerImpl implements IOFilesContainer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final String filenamePostfix = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    private Reader reader;
    private Writer writer = Writer.nullWriter();
    private String outputFilePath;

    private boolean initialized = false;

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void initialize(String inputFileDirectory) {
        initialize(inputFileDirectory, true);
    }

    public void initialize(String inputFileDirectory, boolean createOutputFile) {
        try {
            this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFileDirectory), StandardCharsets.UTF_8));

            if (createOutputFile) {
                this.outputFilePath = inputFileDirectory + "_" + filenamePostfix;
                this.writer = new OutputStreamWriter(new FileOutputStream(outputFilePath), StandardCharsets.UTF_8);
            }

            initialized = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Reader getReader() {
        return reader;
    }

    @Override
    public Writer getWriter() {
        return writer;
    }

    @Override
    public String getOutputFilePath() {
        return outputFilePath;
    }

    @Override
    public void writeNewLine(String line) {

        try {
            writer.write(line + System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void releaseResources() {
        logger.info("Releasing resources...");
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

