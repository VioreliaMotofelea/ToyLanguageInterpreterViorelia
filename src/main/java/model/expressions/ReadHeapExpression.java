package model.expressions;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import model.adts.IMyHeap;
import model.adts.IMyMap;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class ReadHeapExpression implements IExpression {
    private final IExpression expression;
    public ReadHeapExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IValue evaluate(IMyMap<String, IValue> symTable, IMyHeap<IValue> heap) throws ExpressionException, AdtsException {
        IValue expressionValue = expression.evaluate(symTable, heap);
        if (!(expressionValue instanceof RefValue)) {
            throw new ExpressionException("The evaluation of the expression is not a RefType");
        }

        int address = ((RefValue) expressionValue).getAddress();
        if (!heap.exists(address)) {
            throw new ExpressionException("The address is not in the heap.");
        }

        return (IValue) heap.getValue(address);
    }

    @Override
    public IExpression deepCopy() {
        return new ReadHeapExpression(expression.deepCopy());
    }

    @Override
    public String toString() {
        return "rH(" + expression.toString() + ")";
    }

    @Override
    public IType typecheck(IMyMap<String, IType> typeEnv) throws ExpressionException, AdtsException {
        IType type = expression.typecheck(typeEnv);
        if (type instanceof RefType refType) {
            return refType.getInner();
        } else {
            throw new ExpressionException("The rH argument is not a RefType");
        }
    }
}
