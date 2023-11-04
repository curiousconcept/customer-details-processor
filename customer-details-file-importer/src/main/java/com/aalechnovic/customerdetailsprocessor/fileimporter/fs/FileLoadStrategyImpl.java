package com.aalechnovic.customerdetailsprocessor.fileimporter.fs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileLoadStrategyImpl implements FileLoader {
    @Override
    public InputStream readFileAsInputStream(String projectDirFilePath) {
        try {
            return Files.newInputStream(Paths.get(projectDirFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
