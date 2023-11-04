package com.aalechnovic.customerdetailsprocessor.fileimporter.fs;

import java.io.InputStream;

public interface FileLoader {

    InputStream readFileAsInputStream(String filePath);
}
