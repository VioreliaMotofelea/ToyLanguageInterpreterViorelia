package model.adts;

import model.util.Pair;

import java.util.List;
import java.util.Map;

public interface IBarrierTable {
    int getNextFreeLocation();
    void put(int index, Pair<Integer, List<Integer>> value);
    Pair<Integer, java.util.List<Integer>> get(int index);
    void update(int index, Pair<Integer, java.util.List<Integer>> value);
    boolean containsKey(int index);
    Map<Integer, Pair<Integer, List<Integer>>> getContent();
}
