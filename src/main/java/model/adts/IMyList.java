package model.adts;

import java.util.List;

public interface IMyList<T> {
    List<T> getAll();
    void add(T elem);
}
