package model.values;

import model.types.BoolType;
import model.types.IType;

public class BoolValue implements IValue {
    private final boolean value;
    public BoolValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public boolean equals(IValue other) {
        return other.getType() instanceof BoolType &&
                this.getValue() == ((BoolValue) other).getValue();
    }

    @Override
    public String toString() {
        return String.valueOf(value); // return the string representation of the boolean
    }

    @Override
    public IValue deepCopy() {
        return new BoolValue(this.value);
    }
}
