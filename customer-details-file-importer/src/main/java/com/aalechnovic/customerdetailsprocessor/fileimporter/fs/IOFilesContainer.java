package com.aalechnovic.customerdetailsprocessor.fileimporter.fs;

import org.springframework.beans.factory.DisposableBean;

import java.io.Reader;
import java.io.Writer;

public interface IOFilesContainer extends DisposableBean {

    boolean isInitialized();

    void initialize(String inputFileDirectory);

    Reader getReader();

    Writer getWriter();

    String getOutputFilePath();

    void writeNewLine(String line);

    void releaseResources();

    @Override
    default void destroy() {
        releaseResources();}
}
