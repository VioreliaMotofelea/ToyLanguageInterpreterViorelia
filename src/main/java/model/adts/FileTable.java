package model.adts;

import model.values.StringValue;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class FileTable implements IFileTable {
    private final Map<StringValue, BufferedReader> fileTable;
    public FileTable() {
        this.fileTable = new HashMap<>();
    }

    @Override
    public void put(StringValue key, BufferedReader value) {
        fileTable.put(key, value);
    }

    @Override
    public BufferedReader get(StringValue key) {
        return fileTable.get(key);
    }

    @Override
    public boolean contains(StringValue key) {
        return fileTable.containsKey(key);
    }

    @Override
    public void remove(StringValue key) {
        fileTable.remove(key);
    }

    @Override
    public Map<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (StringValue key : fileTable.keySet()) {
            str.append(key.toString()).append(" -> ").append(fileTable.get(key).toString());
            str.append(" ");
        }

        return str.toString();
    }
}
