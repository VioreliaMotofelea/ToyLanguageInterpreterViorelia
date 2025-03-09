package model.adts;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements IMyList<T> {
    private List<T> list;
    public MyList() {
        list = new ArrayList<T>();
    }

    @Override
    public List<T> getAll() {
        return this.list;
    }

    @Override
    public void add(T elem) {
        list.add(elem);
    }

    void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (T element : list) {
            str.append(element.toString());
            str.append(" ");
        }
        return str.toString();
    }
}
