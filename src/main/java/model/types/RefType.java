package model.types;

import model.values.IValue;
import model.values.RefValue;

public class RefType implements IType {
    private IType inner;
    public RefType(IType inner) {
        this.inner = inner;
    }

    public IType getInner() {
        return this.inner;
    }

    public void setInner(IType inner) {
        this.inner = inner;
    }

    @Override
    public boolean equals(IType other) {
        return other instanceof RefType && this.inner.equals(((RefType) other).getInner());
    }

    @Override
    public IValue defaultValue() {
        return new RefValue(0, inner);
    }

    @Override
    public String toString() {
        return "Ref(" + inner.toString() + ")";
    }

    @Override
    public IType deepCopy() {
        return new RefType(this.inner.deepCopy());
    }
}
