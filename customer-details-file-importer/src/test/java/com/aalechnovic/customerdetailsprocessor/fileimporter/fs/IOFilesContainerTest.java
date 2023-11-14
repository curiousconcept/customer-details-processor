package com.aalechnovic.customerdetailsprocessor.fileimporter.fs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.aalechnovic.customerdetailsprocessor.fileimporter.util.FileLister.listFiles;
import static org.assertj.core.api.Assertions.assertThat;

class IOFilesContainerTest {

    public static final String SRC_TEST_RESOURCES_FILE_DIR = "src/test/resources/file";

    @AfterEach
    void tearDown() throws IOException {
        listFiles(SRC_TEST_RESOURCES_FILE_DIR).stream().filter(s -> s.contains("test_")).forEach(s -> {
            try {
                Files.delete(Paths.get(s));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void canReadFile_withText() throws IOException {
        final IOFilesContainerImpl filesContainer = new IOFilesContainerImpl();
        filesContainer.initialize(SRC_TEST_RESOURCES_FILE_DIR + "/test", false);

        char[] array = new char[4];
        int res = filesContainer.getReader().read(array);
        assertThat(res).isGreaterThan(0);
        assertThat(new String(array)).isEqualTo("test");
        int res2 = filesContainer.getReader().read(array);
        assertThat(res2).isEqualTo(-1);
        filesContainer.releaseResources();
    }

    @Test
    void canWriteFile_withText() throws IOException {
        final IOFilesContainer filesContainer = new IOFilesContainerImpl();
        filesContainer.initialize(SRC_TEST_RESOURCES_FILE_DIR + "/test");
        filesContainer.getWriter().write("hello");
        filesContainer.releaseResources();

        String outputFileName = filesContainer.getOutputFilePath();
        File f = new File(outputFileName);

        assertThat(f).isFile();
        Path outputFileNamePath = Paths.get(outputFileName);

        String content = Files.readString(outputFileNamePath);
        assertThat(content).isEqualTo("hello");
    }

    @Test
    void eachInstanceWritesUniqueFile_preserveHistoryBetweenInvocations() throws IOException, InterruptedException {
        String inDirFile = SRC_TEST_RESOURCES_FILE_DIR + "/test";

        final IOFilesContainer filesContainer = new IOFilesContainerImpl();
        filesContainer.initialize(inDirFile);
        filesContainer.releaseResources();

        String outputFileName = filesContainer.getOutputFilePath();

        // ensure we can keep milli precision(vs nano) for filename timestamp part without two
        // files being created at the same time(which would fail this test). Using nano precision would not need thread sleep below but
        // would result in long filenames

        Thread.sleep(1);
        final IOFilesContainer secondFilesContainer = new IOFilesContainerImpl();
        secondFilesContainer.initialize(inDirFile);
        secondFilesContainer.releaseResources();

        String secondOutputFileName = secondFilesContainer.getOutputFilePath();

        assertThat(outputFileName).isNotEqualTo(secondOutputFileName);

        var files = listFiles(SRC_TEST_RESOURCES_FILE_DIR);

        assertThat(files).hasSize(3).contains(inDirFile,  outputFileName, secondOutputFileName);
    }
}