package com.aalechnovic.customerdetailsprocessor.fileimporter;

import com.aalechnovic.customerdetailsprocessor.fileimporter.api.CusDetailsApiClientConfig;
import com.aalechnovic.customerdetailsprocessor.fileimporter.api.CusDetailsApiClient;
import com.aalechnovic.customerdetailsprocessor.fileimporter.fs.FSConfig;
import com.aalechnovic.customerdetailsprocessor.fileimporter.fs.IOFilesContainer;
import com.aalechnovic.customerdetailsprocessor.fileimporter.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {FSConfig.class, CusDetailsApiClientConfig.class})
public class CusDetailsFileImporterConfig {

    @Bean
    public CusDetailsFileImporterCLRunner cusDetailsFileImporterCLRunner(IOFilesContainer ioFilesContainer,
                                                                         CusDetailsFileRecordsProcessor cusDetailsFileRecordsProcessor){
        return new CusDetailsFileImporterCLRunner(ioFilesContainer, cusDetailsFileRecordsProcessor);
    }

    @Bean
    public CusDetailsFileRecordsProcessor cusDetailsFileProcessorService(CusDetailsApiClient cusDetailsApiClient, Validator validator){
        return new CusDetailsFileRecordsProcessor(cusDetailsApiClient, validator);
    }
}
