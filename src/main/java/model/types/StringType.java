package model.types;

import model.values.IValue;
import model.values.StringValue;

public class StringType implements IType {
    public StringType() {}

    @Override
    public boolean equals(IType other) {
        return other instanceof StringType;
    }

    @Override
    public String toString() {
        return "string";
    }

    @Override
    public IValue defaultValue() {
        return new StringValue("");
    }

    @Override
    public IType deepCopy() {
        return new StringType();
    }
}
