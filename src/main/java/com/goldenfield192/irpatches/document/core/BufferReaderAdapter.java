package com.goldenfield192.irpatches.document.core;

import cam72cam.mod.resource.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BufferReaderAdapter {
    private final BufferedReader reader;

    public BufferReaderAdapter(Identifier identifier) {
        try {
            this.reader = new BufferedReader(new InputStreamReader(identifier.getResourceStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
