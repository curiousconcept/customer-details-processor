package com.aalechnovic.customerdetailsprocessor.fileimporter.fs;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class FileLoadStrategyImplTest {

    private final FileLoader fileLoader = new FileLoadStrategyImpl();

    @Test
    void canLoadFile_withText() throws IOException {
        try(InputStream is = fileLoader.readFileAsInputStream("src/test/resources/test")){
            String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            assertThat(text).isEqualTo("test");
        }
    }
}