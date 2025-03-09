package model.expressions;

import exceptions.ExpressionException;
import model.adts.IMyHeap;
import model.adts.IMyMap;
import model.types.IType;
import model.values.IValue;

public class ValueExpression implements IExpression {
    private final IValue value;
    public ValueExpression(IValue value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public IValue evaluate(IMyMap<String, IValue> symTable, IMyHeap<IValue> heap) throws ExpressionException {
        return value;
    }

    @Override
    public IExpression deepCopy() {
        return new ValueExpression(value);
    }

    @Override
    public IType typecheck(IMyMap<String, IType> typeEnv) throws ExpressionException {
        return value.getType();
    }
}
