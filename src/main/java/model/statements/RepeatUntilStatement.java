package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IMyHeap;
import model.adts.IMyMap;
import model.adts.IMyStack;
import model.expressions.IExpression;
import model.states.ProgramState;
import model.types.BoolType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;

public class RepeatUntilStatement implements IStatement {
    private final IStatement statement;
    private final IExpression expression;

    public RepeatUntilStatement(IStatement statement, IExpression expression) {
        this.statement = statement;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "repeat " + statement.toString() + " until (" + expression.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException, AdtsException {
        IMyStack<IStatement> executionStack = state.getExeStack();
        IMyMap<String, IValue> symbolTable = state.getSymTable();
        IMyHeap<IValue> heap = state.getHeap();

        IValue conditionValue = expression.evaluate(symbolTable, heap);
        if (!(conditionValue instanceof BoolValue)) {
            throw new StatementException("The expression in Repeat-Until must evaluate to a boolean.");
        }

        boolean condition = ((BoolValue) conditionValue).getValue();

        if (!condition) {
            executionStack.push(this);
            executionStack.push(statement);
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new RepeatUntilStatement(statement.deepCopy(), expression.deepCopy());
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        IType conditionType = expression.typecheck(typeEnv);
        if (!conditionType.equals(new BoolType())) {
            throw new StatementException("The condition of Repeat-Until must be of type bool.");
        }
        statement.typecheck(typeEnv);
        return typeEnv;
    }
}
