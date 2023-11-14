package com.aalechnovic.customerdetailsprocessor.fileimporter;

import com.aalechnovic.customerdetailsprocessor.fileimporter.api.util.WireMockConfig;
import com.aalechnovic.customerdetailsprocessor.fileimporter.fs.IOFilesContainer;
import com.aalechnovic.customerdetailsprocessor.fileimporter.util.TestCSVReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.aalechnovic.customerdetailsprocessor.fileimporter.util.FileLister.listFiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import({WireMockConfig.class})
class CusDetailsFileImporterIntegrationTest {

    @Autowired
    CusDetailsFileImporterCLRunner cusDetailsFileImporterCLRunner;

    @Autowired
    IOFilesContainer ioFilesContainer;

    @Autowired
    WireMockConfig wireMockConfig;

    private static final String CSV_FILE_DIR = "src/test/resources/csvfile";

    @AfterEach
    void tearDown() throws IOException {
        listFiles(CSV_FILE_DIR).stream().filter(s -> s.contains("test_")).forEach(s -> {
            try {
                Files.delete(Paths.get(s));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void loadTestFile() throws IOException {

        String csvFileName = "test";

        cusDetailsFileImporterCLRunner.run("src/test/resources/csvfile/" + csvFileName);

        ioFilesContainer.getWriter().flush();

        int actualPort = wireMockConfig.webServer().port();

        List<String> expectedLines = Arrays.asList(
                "Input File Line,Customer Ref,Status",
                "1,de29fb90-12c4-11e1-840d-7b25c5ee775a,success",
                "2,fl20fb90-12c4-11e1-840d-7b25c5ee775a,success",
                "3,fl21fb90-12c4-11e1-840d-7b25c5ee775a,409 Conflict from POST http://localhost:" + actualPort + "/v1/customers"
        );
        List<String> reportRecords = Files.readAllLines(Paths.get(ioFilesContainer.getOutputFilePath()));

        assertThat(reportRecords.subList(0,4)).isEqualTo(expectedLines);

        String actual = reportRecords.get(4);

        assertThat(actual).contains("[\"\"town\"\" : \"\"must not be null\"\"]");
        assertThat(actual).contains("[\"\"country\"\" : \"\"must not be null\"\"]");
        assertThat(actual).contains("[\"\"postCode\"\" : \"\"must not be null\"\"]");
        assertThat(actual).contains("[\"\"addressLineOne\"\" : \"\"must not be null\"\"]");
        assertThat(actual).contains("[\"\"customerName\"\" : \"\"must not be null\"\"]");


    }


}