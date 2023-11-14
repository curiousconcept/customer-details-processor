package com.aalechnovic.customerdetailsprocessor.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Component
public class PortProviderImpl implements PortProvider {

    @Value("${server.port}")
    private Integer serverPort;

    public int getServerPort() {
        assertNotNull(serverPort);
        return serverPort;
    }
}




