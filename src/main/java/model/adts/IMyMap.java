package model.adts;

import exceptions.AdtsException;

import java.util.Map;

public interface IMyMap<K, V> {
    V get(K key) throws AdtsException;
    void put(K key, V value);
    boolean contains(K key);
    void remove(K key) throws AdtsException;
    Map<K, V> getContent();
    void setContent(Map<K, V> newContent);
    void update(K key, V value) throws AdtsException;
    IMyMap<K, V> deepCopy();
}
