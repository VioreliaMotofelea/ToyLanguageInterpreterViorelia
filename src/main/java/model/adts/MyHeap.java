package model.adts;

import exceptions.AdtsException;
import model.values.IValue;

import java.util.HashMap;
import java.util.Map;

public class MyHeap implements IMyHeap<IValue> {
    private final Map<Integer, IValue> values;
    private int freeAddress;

    public MyHeap() {
        this.values = new HashMap<>();
        this.freeAddress = 1;
    }

    @Override
    public int allocate(IValue value) {
        this.values.put(freeAddress++, value);
        return freeAddress - 1;
    }

    @Override
    public void delete(int address) throws AdtsException {
        if (!values.containsKey(address)) {
            throw new AdtsException("Address " + address + " not found in the heap.");
        }
        values.remove(address);
    }

    @Override
    public boolean exists(int address) {
        return values.containsKey(address);
    }

    @Override
    public void set(int address, IValue value) throws AdtsException {
        if (!values.containsKey(address)) {
            throw new AdtsException("Address " + address + " not found in the heap.");
        }
        this.values.put(address, value);
    }

    @Override
    public IValue getValue(int address) throws AdtsException {
        if (!values.containsKey(address)) {
            throw new AdtsException("Address " + address + " not found in the heap.");
        }
        return values.get(address);
    }

    @Override
    public Map<Integer, IValue> getContent() {
        return values;
    }

    @Override
    public void setContent(Map<Integer, IValue> newContent) {
        this.values.clear();
        this.values.putAll(newContent);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (Integer address : values.keySet()) {
            str.append(address.toString()).append(" -> ").append(values.get(address).toString());
            str.append(" ");
        }

        return str.toString();
    }
}
