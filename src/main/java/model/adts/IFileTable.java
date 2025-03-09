package model.adts;

import model.values.StringValue;

import java.io.BufferedReader;
import java.util.Map;

public interface IFileTable {
    void put(StringValue key, BufferedReader value);
    BufferedReader get(StringValue key);
    boolean contains(StringValue key);
    void remove(StringValue key);
    Map<StringValue, BufferedReader> getFileTable();
}
