package model.types;

import model.adts.IClonable;
import model.values.IValue;

public interface IType extends IClonable {
    boolean equals(IType other);
    IValue defaultValue();
    IType deepCopy();
}
