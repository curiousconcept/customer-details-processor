package com.aalechnovic.customerdetailsprocessor.fileimporter.util;

import java.io.Reader;

public class TestReader extends Reader {
    private final String text;
    private int position;

    public TestReader(String text) {
        this.text = text;
        this.position = 0;
    }

    @Override
    public int read(char[] cbuf, int off, int len) {
        if (position >= text.length()) {
            return -1;
        }

        int charsToRead = Math.min(len, text.length() - position);
        text.getChars(position, position + charsToRead, cbuf, off);
        position += charsToRead;

        return charsToRead;
    }

    @Override
    public void close() {
        // No need to close anything in this implementation
    }
}
