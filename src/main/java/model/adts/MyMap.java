package model.adts;

import exceptions.AdtsException;

import java.util.HashMap;
import java.util.Map;

public class MyMap<K, V extends IClonable> implements IMyMap<K, V> {
    private final Map<K, V> map;
    public MyMap() {
        this.map = new HashMap<K, V>();
    }

    @Override
    public V get(K key) throws AdtsException {
        if (!map.containsKey(key)) {
            throw new AdtsException("Key not found");
        }
        return map.get(key);
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public boolean contains(K key) {
        return map.containsKey(key);
    }

    @Override
    public void remove(K key) throws AdtsException {
        if (!map.containsKey(key)) {
            throw new AdtsException("Key not found");
        }
        map.remove(key);
    }

    @Override
    public Map<K, V> getContent() {
        return this.map;
    }

    @Override
    public void setContent(Map<K, V> newContent) {
        this.map.clear();
        this.map.putAll(newContent);
    }

    @Override
    public void update(K key, V value) throws AdtsException {
        if (!map.containsKey(key)) {
            throw new AdtsException("Key " + key + " not defined in SymTable. Use `put` instead if this is a new key.");
        }
        map.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public IMyMap<K, V> deepCopy() {
        IMyMap<K, V> newMap = new MyMap<>();
        for (Map.Entry<K, V> entry : this.map.entrySet()) {
            newMap.put(entry.getKey(),(V) entry.getValue().deepCopy());
        }
        return newMap;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (K key : map.keySet()) {
            str.append(key.toString()).append(" -> ").append(map.get(key).toString());
            str.append(" ");
        }

        return str.toString();
    }
}
