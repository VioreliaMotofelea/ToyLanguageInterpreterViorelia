package model.expressions;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import model.adts.IMyHeap;
import model.adts.IMyMap;
import model.types.IType;
import model.types.IntType;
import model.values.IValue;
import model.values.IntValue;

public class ArithmeticExpression implements IExpression {
    private final char operator;
    private final IExpression expression1;
    private final IExpression expression2;

    public ArithmeticExpression(char operator, IExpression expression1, IExpression expression2) {
        this.operator = operator;
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public String toString() {
        return expression1.toString() + " " + operator + " " + expression2.toString();
    }

    @Override
    public IValue evaluate(IMyMap<String, IValue> symTable, IMyHeap<IValue> heap) throws ExpressionException, AdtsException {
        var leftValue = expression1.evaluate(symTable, heap);
        var rightValue = expression2.evaluate(symTable, heap);

        if (!leftValue.getType().equals(new IntType()) ||
            !rightValue.getType().equals(new IntType())) {
            throw new ExpressionException("First or second operand is not an integer");
        }

        int leftInt = ((IntValue) leftValue).getValue();
        int rightInt = ((IntValue) rightValue).getValue();

        switch (operator) {
            case '+':
                return new IntValue(leftInt + rightInt);
            case '-':
                return new IntValue(leftInt - rightInt);
            case '*':
                return new IntValue(leftInt * rightInt);
            case '/':
                if (rightInt == 0) {
                    throw new ExpressionException("Division by zero");
                }
                return new IntValue(leftInt / rightInt);
            default:
                throw new ExpressionException("Invalid operator: " + operator);
        }
    }

    @Override
    public IExpression deepCopy() {
        return new ArithmeticExpression(operator, expression1.deepCopy(), expression2.deepCopy());
    }

    @Override
    public IType typecheck(IMyMap<String, IType> typeEnv) throws ExpressionException, AdtsException {
        IType type1 = expression1.typecheck(typeEnv);
        IType type2 = expression2.typecheck(typeEnv);

        if (type1.equals(new IntType()) && type2.equals(new IntType())) {
            return new IntType();
        } else {
            throw new ExpressionException("One of the operand is not an integer");
        }
    }
}
