package com.aalechnovic.customerdetailsprocessor.fileimporter.fs;

import org.springframework.context.annotation.Bean;

public class FSConfig {

    @Bean
    IOFilesContainerInitControlAspect initializationAspect(){
        return new IOFilesContainerInitControlAspect();
    }

    @Bean
    IOFilesContainer ioFilesContainer(){
        return new IOFilesContainerImpl();
    }
}
