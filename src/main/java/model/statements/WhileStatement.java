package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IMyMap;
import model.expressions.IExpression;
import model.states.ProgramState;
import model.types.BoolType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;

public class WhileStatement implements IStatement {
    private final IExpression expression;
    private final IStatement statement;

    public WhileStatement(IExpression expression, IStatement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public String toString() {
        return "while (" + expression.toString() + ") " + statement.toString();
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException, AdtsException {
        IValue condition = expression.evaluate(state.getSymTable(), state.getHeap());
        if (!(condition.getType() instanceof BoolType)) {
            throw new StatementException("Condition expression is not a boolean.");
        }

        BoolValue boolCondition = (BoolValue) condition;
        if (boolCondition.getValue()) {
            state.getExeStack().push(this);
            state.getExeStack().push(statement);
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new WhileStatement(expression.deepCopy(), statement.deepCopy());
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        IType expressionType = expression.typecheck(typeEnv);
        if (!expressionType.equals(new BoolType())) {
            throw new StatementException("The condition of WHILE has not the type bool");
        }

        statement.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }
}
