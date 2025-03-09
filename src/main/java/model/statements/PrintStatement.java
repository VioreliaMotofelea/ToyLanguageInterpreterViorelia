package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IMyMap;
import model.expressions.IExpression;
import model.states.ProgramState;
import model.types.IType;
import model.values.IValue;

public class PrintStatement implements IStatement {
    private final IExpression expression;
    public PrintStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "Print(" + expression.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException, AdtsException {
        IValue result = expression.evaluate(state.getSymTable(), state.getHeap());
        state.getOut().add(result);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new PrintStatement(expression.deepCopy());
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        expression.typecheck(typeEnv);
        return typeEnv;
    }
}
