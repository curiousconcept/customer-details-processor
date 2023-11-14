package com.aalechnovic.customerdetailsprocessor.fileimporter.util;

import com.aalechnovic.customerdetailsprocessor.fileimporter.fs.IOFilesContainer;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class TestIOFilesContainer implements IOFilesContainer {

    private final Reader testReader;
    private final Writer testWriter;
    private boolean isInitialized;

    public TestIOFilesContainer(String initialText) {
        testReader = new TestReader(initialText);
        this.testWriter = new TestWriter();
    }

    public TestIOFilesContainer() {
        this.testReader = Reader.nullReader();
        this.testWriter = new TestWriter();
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void initialize(String inputFileDirectory) {
        isInitialized = true;
    }

    public void initialize(String inputFileDirectory, boolean createOutputFile) {
        isInitialized = true;
    }

    @Override
    public Reader getReader() {
        return testReader;
    }

    @Override
    public Writer getWriter() {
        throw new UnsupportedOperationException("Test version doesn't support this operation");    }

    @Override
    public String getOutputFilePath() {
        throw new UnsupportedOperationException("Test version doesn't support this operation");
    }

    @Override
    public void writeNewLine(String line)  {
        try {
            testWriter.write(line + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void releaseResources() {
        throw new UnsupportedOperationException("Test version doesn't support this operation");
    }
}
