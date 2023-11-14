package com.aalechnovic.customerdetailsprocessor.fileimporter.fs;

import com.aalechnovic.customerdetailsprocessor.fileimporter.util.TestIOFilesContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IOFilesContainerInitializationTest {

    @Autowired
    private IOFilesContainerInitControlAspect IOFilesContainerInitControlAspect;

    @Autowired
    private IOFilesContainer ioFilesContainer;

    @Test
    public void testInitializationAspectFirewalls_whenObjectIsntInitialized() {
        assertThrows(IllegalStateException.class, ioFilesContainer::getReader);
    }

    @Test
    public void testInitializationAspectIsSilent_whenObjectIsInitialized() {
        ioFilesContainer.initialize("bla");
        ioFilesContainer.getReader();
    }

    @TestConfiguration
    @Import(value = {AnnotationAwareAspectJAutoProxyCreator.class})
    static class TestConfig {

        @Bean
        IOFilesContainer ioFilesContainer(){
            return new TestIOFilesContainer();
        }

        @Bean
        IOFilesContainerInitControlAspect ioFilesContainerInitControlAspect(){
            return new IOFilesContainerInitControlAspect();
        }
    }
}