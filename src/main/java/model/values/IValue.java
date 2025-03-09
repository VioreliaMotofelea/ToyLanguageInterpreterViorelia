package model.values;

import model.adts.IClonable;
import model.types.IType;

public interface IValue extends IClonable {
    IType getType();
    boolean equals(IValue other);
    IValue deepCopy();
}
