package model.adts;

import exceptions.AdtsException;

import java.util.Stack;

public interface IMyStack<T> {
    T pop() throws AdtsException;
    void push(T element);
    int getSize();
    boolean isEmpty();
    Stack<T> getContent();
}
