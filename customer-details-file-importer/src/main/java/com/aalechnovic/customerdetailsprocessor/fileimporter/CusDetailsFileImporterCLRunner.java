package com.aalechnovic.customerdetailsprocessor.fileimporter;

import com.aalechnovic.customerdetailsprocessor.fileimporter.csv.CSVFileRecordsReaderImpl;
import com.aalechnovic.customerdetailsprocessor.fileimporter.csv.CSVFileRecordsWriterImpl;
import com.aalechnovic.customerdetailsprocessor.fileimporter.fs.IOFilesContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

public class CusDetailsFileImporterCLRunner implements CommandLineRunner {

    private final IOFilesContainer ioFilesContainer;
    private final CusDetailsFileRecordsProcessor cusDetailsFileRecordsProcessor;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CusDetailsFileImporterCLRunner(IOFilesContainer ioFilesContainer,
                                          CusDetailsFileRecordsProcessor cusDetailsFileRecordsProcessor) {
        this.ioFilesContainer = ioFilesContainer;
        this.cusDetailsFileRecordsProcessor = cusDetailsFileRecordsProcessor;
    }

    @Override
    public void run(String... args) {
        if (args.length == 0){
            logger.info("No arguments supplied. Terminating..");
            return;
        }

        if (args.length > 1){
            logger.info("Expecting only one argument. Terminating..");
            return;
        }

        String filename = args[0];

        logger.info("Starting file import located at [ {} ]", filename);

        try {
            ioFilesContainer.initialize(filename);
        } catch (Exception e) {
            logger.error("File or file path is wrong please check the file exists at [ {} ] with reason [ {} ]", filename, e.getMessage());
            return;
        }

        var fileProcessingPublisher = cusDetailsFileRecordsProcessor.processFileRecords(new CSVFileRecordsReaderImpl(ioFilesContainer.getReader()),
                                                                                        new CSVFileRecordsWriterImpl(ioFilesContainer.getWriter()));

        fileProcessingPublisher.blockLast();

        logger.info("Finished importing file [ {} ]", filename);
        logger.info("Report [ {} ]",  ioFilesContainer.getOutputFilePath());

    }
}
