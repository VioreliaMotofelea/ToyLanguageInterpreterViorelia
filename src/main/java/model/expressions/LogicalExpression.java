package model.expressions;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import model.adts.IMyHeap;
import model.adts.IMyMap;
import model.types.BoolType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;

public class LogicalExpression implements IExpression {
    private final IExpression expression1;
    private final IExpression expression2;
    private final LogicalOperation operation;

    public LogicalExpression(IExpression expression1, IExpression expression2, LogicalOperation operation) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operation = operation;
    }

    @Override
    public String toString() {
        return expression1.toString() + " " + operation.toString().toLowerCase() + " " + expression2.toString();
    }

    @Override
    public IValue evaluate(IMyMap<String, IValue> symTable, IMyHeap<IValue> heap) throws ExpressionException, AdtsException {
        var leftExpressionValue = expression1.evaluate(symTable, heap);
        var rightExpressionValue = expression2.evaluate(symTable, heap);

        if (!leftExpressionValue.getType().equals(new BoolType()) ||
            !rightExpressionValue.getType().equals(new BoolType())) {
            throw new ExpressionException("Left or right not bool type");
        }

        boolean b1 = ((BoolValue) leftExpressionValue).getValue();
        boolean b2 = ((BoolValue) rightExpressionValue).getValue();

        if (operation == LogicalOperation.AND) {
            return new BoolValue(b1 && b2);
        } else if (operation == LogicalOperation.OR) {
            return new BoolValue(b1 || b2);
        } else {
            throw new ExpressionException("Unknown operation");
        }
    }

    @Override
    public IExpression deepCopy() {
        return new LogicalExpression(expression1, expression2, operation);
    }

    @Override
    public IType typecheck(IMyMap<String, IType> typeEnv) throws ExpressionException, AdtsException {
        IType type1 = expression1.typecheck(typeEnv);
        IType type2 = expression2.typecheck(typeEnv);

        if (type1.equals(new BoolType()) && type2.equals(new BoolType())) {
            return new BoolType();
        } else {
            throw new ExpressionException("One of the operands is not a boolean");
        }
    }
}
