package model.values;

import model.types.IType;
import model.types.RefType;

public class RefValue implements IValue {
    private int address;
    private IType locationType;

    public RefValue(Integer address, IType locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress() {
        return this.address;
    }

    public IType getLocationType() {
        return this.locationType;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setLocationType(IType locationType) {
        this.locationType = locationType;
    }

    @Override
    public IType getType() {
        return new RefType(locationType);
    }

    @Override
    public boolean equals(IValue other) {
        return other instanceof RefValue && this.address == ((RefValue) other).getAddress() &&
                this.locationType.equals(((RefValue) other).getLocationType());
    }

    @Override
    public String toString() {
        return "(" + address + ", " + locationType.toString() + ")";
    }

    @Override
    public IValue deepCopy() {
        return new RefValue(this.address, this.locationType.deepCopy());
    }
}
