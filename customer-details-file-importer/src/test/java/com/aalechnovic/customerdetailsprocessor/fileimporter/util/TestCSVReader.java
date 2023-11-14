package com.aalechnovic.customerdetailsprocessor.fileimporter.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestCSVReader {

        public static List<String> readCsvFile(String csvFilePath) {
            List<String> lines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return lines;
        }

}
