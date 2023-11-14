package com.aalechnovic.customerdetailsprocessor.fileimporter.fs;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
public class IOFilesContainerInitControlAspect {


    @Pointcut("execution(* com.aalechnovic.customerdetailsprocessor.fileimporter.fs.IOFilesContainer+.*(..)) && " +
              "!execution(* com.aalechnovic.customerdetailsprocessor.fileimporter.fs.IOFilesContainer+.initialize(..)) &&" +
              "!execution(* com.aalechnovic.customerdetailsprocessor.fileimporter.fs.IOFilesContainer+.destroy(..)) &&" +
              "!execution(* com.aalechnovic.customerdetailsprocessor.fileimporter.fs.IOFilesContainer+.releaseResources(..))")
    public void ioFilesContainerMethods() {}

    @Before("ioFilesContainerMethods() && target(ioFilesContainer)")
    public void checkInitialization(IOFilesContainer ioFilesContainer) {
        if (!ioFilesContainer.isInitialized()) {
            throw new IllegalStateException("IOFilesContainer has not been initialized.");
        }
    }
}