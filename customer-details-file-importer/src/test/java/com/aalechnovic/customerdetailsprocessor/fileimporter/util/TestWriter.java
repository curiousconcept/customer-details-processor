package com.aalechnovic.customerdetailsprocessor.fileimporter.util;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestWriter extends Writer {
    private final StringWriter stringWriter = new StringWriter();
    private final List<String> writtenLines = new ArrayList<>();

    @Override
    public void write(char[] cbuf, int off, int len) {
        stringWriter.write(cbuf, off, len);
    }

    @Override
    public void write(String str) {
        stringWriter.write(str);
    }

    @Override
    public void write(String str, int off, int len) {
        stringWriter.write(str, off, len);
    }

    @Override
    public void write(int c)  {
        stringWriter.write(c);
    }

    @Override
    public void flush() {
        String[] lines = stringWriter.toString().split("\\r?\\n");
        writtenLines.addAll(Arrays.asList(lines));
        stringWriter.getBuffer().setLength(0);  
    }

    @Override
    public void close() {
        
    }

    public List<String> getWrittenLines() {
        return writtenLines;  
    }
}
