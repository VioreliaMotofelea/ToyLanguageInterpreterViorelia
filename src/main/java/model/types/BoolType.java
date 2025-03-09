package model.types;

import model.values.BoolValue;
import model.values.IValue;

public class BoolType implements IType {
    public BoolType() {}

    @Override
    public boolean equals(IType other) {
        return other instanceof BoolType;
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public IValue defaultValue() {
        return new BoolValue(false);
    }

    @Override
    public IType deepCopy() {
        return new BoolType();
    }
}
