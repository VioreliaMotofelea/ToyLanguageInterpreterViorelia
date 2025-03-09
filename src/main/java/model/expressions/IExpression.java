package model.expressions;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import model.adts.IMyHeap;
import model.adts.IMyMap;
import model.types.IType;
import model.values.IValue;

public interface IExpression {
    IValue evaluate(IMyMap<String, IValue> symTable, IMyHeap<IValue> heap) throws ExpressionException, AdtsException;
    IExpression deepCopy();
    IType typecheck(IMyMap<String, IType> typeEnv) throws ExpressionException, AdtsException;
}
