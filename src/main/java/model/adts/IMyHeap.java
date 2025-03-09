package model.adts;

import exceptions.AdtsException;

import java.util.Map;

public interface IMyHeap<IValue> {
    int allocate(IValue value);
    void delete(int address) throws AdtsException;
    boolean exists(int address);
    void set(int address, IValue value) throws AdtsException;
    IValue getValue(int address) throws AdtsException;
    Map<Integer, IValue> getContent();
    void setContent(Map<Integer, IValue> newContent);
}
