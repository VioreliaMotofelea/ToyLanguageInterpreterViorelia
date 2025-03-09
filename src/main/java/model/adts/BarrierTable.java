package model.adts;

import model.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class BarrierTable implements IBarrierTable {
    private final Map<Integer, Pair<Integer, List<Integer>>> barrierTable;
    private int nextFreeLocation;
    private final ReentrantLock lock = new ReentrantLock();

    public BarrierTable() {
        this.barrierTable = new HashMap<>();
        this.nextFreeLocation = 0;
    }

    @Override
    public int getNextFreeLocation() {
        lock.lock();
        try {
            return nextFreeLocation++;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(int index, Pair<Integer, List<Integer>> value) {
        lock.lock();
        try {
            barrierTable.put(index, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Pair<Integer, List<Integer>> get(int index) {
        lock.lock();
        try {
            return barrierTable.get(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(int index, Pair<Integer, List<Integer>> value) {
        lock.lock();
        try {
            barrierTable.put(index, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsKey(int index) {
        lock.lock();
        try {
            return barrierTable.containsKey(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Map<Integer, Pair<Integer, List<Integer>>> getContent() {
        lock.lock();
        try {
            return new HashMap<>(barrierTable);
        } finally {
            lock.unlock();
        }
    }
}
