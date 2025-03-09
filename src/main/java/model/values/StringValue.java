package model.values;

import model.types.IType;
import model.types.StringType;

public class StringValue implements IValue {
    private final String value;
    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public boolean equals(IValue other) {
        return other.getType() instanceof StringType &&
                this.getValue() == ((StringValue) other).getValue();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public IValue deepCopy() {
        return new StringValue(this.value);
    }
}
