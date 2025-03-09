package model.adts;

import exceptions.AdtsException;
import model.values.IValue;

import java.util.Map;
import java.util.Stack;

public class MyStack<T> implements IMyStack<T> {
    private Stack<T> stack;
    public MyStack() {
        stack = new Stack<T>();
    }

    @Override
    public T pop() throws AdtsException {
        if (stack.isEmpty()) {
            throw new AdtsException("The stack is empty");
        }
        return stack.pop();
    }

    @Override
    public void push(T element) {
        stack.push(element);
    }

    @Override
    public int getSize() {
        return stack.size();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public Stack<T> getStack() {
        return this.stack;
    }

    public void setStack(Stack<T> stack) {
        this.stack = stack;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        var reversed = stack.stream().toList().reversed();

        for (T item : reversed) {
            str.append(item);
            str.append(" ");
        }

        return str.toString();
    }

    @Override
    public Stack<T> getContent() {
        return this.stack;
    }
}
