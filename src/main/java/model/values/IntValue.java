package model.values;

import model.types.IType;
import model.types.IntType;

public class IntValue implements IValue {
    private final int value;
    public IntValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public IType getType() {
        return new IntType();
    }

    @Override
    public boolean equals(IValue other) {
        return other.getType() instanceof IntType &&
                this.getValue() == ((IntValue) other).getValue();
    }

    @Override
    public String toString() {
        return Integer.toString(value); // convert the integer value to a string
    }

    @Override
    public IValue deepCopy() {
        return new IntValue(this.value);
    }
}
