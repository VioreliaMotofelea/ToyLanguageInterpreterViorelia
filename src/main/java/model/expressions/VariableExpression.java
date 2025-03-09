package model.expressions;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import model.adts.IMyHeap;
import model.adts.IMyMap;
import model.types.IType;
import model.values.IValue;

public class VariableExpression implements IExpression {
    private final String variable;
    public VariableExpression(String variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return this.variable;
    }

    @Override
    public IValue evaluate(IMyMap<String, IValue> symTable, IMyHeap<IValue> heap) throws ExpressionException, AdtsException {
        return symTable.get(variable);
    }

    @Override
    public IExpression deepCopy() {
        return new VariableExpression(variable);
    }

    @Override
    public IType typecheck(IMyMap<String, IType> typeEnv) throws ExpressionException, AdtsException {
        return typeEnv.get(variable);
    }
}
