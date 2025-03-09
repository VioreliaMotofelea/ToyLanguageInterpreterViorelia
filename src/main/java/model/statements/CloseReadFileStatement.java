package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IMyMap;
import model.expressions.IExpression;
import model.states.ProgramState;
import model.types.IType;
import model.types.StringType;
import model.values.IValue;
import model.values.StringValue;

import java.io.IOException;

public class CloseReadFileStatement implements IStatement {
    private final IExpression expression;
    public CloseReadFileStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "closeRFile(" + expression.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException, AdtsException {
        IValue expressionValue = this.expression.evaluate(state.getSymTable(), state.getHeap());

        if (!(expressionValue.getType() instanceof StringType)) {
            throw new StatementException("The evaluated expression is not a string");
        }

        StringValue filename = ((StringValue) expressionValue);
        if (!state.getFileTable().contains(filename)) {
            throw new StatementException("Can't close this " + filename);
        }

        try {
            state.getFileTable().get(filename).close();
        } catch (IOException io) {
            throw new StatementException("Error closing file " + filename);
        }

        state.getFileTable().remove(filename);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new CloseReadFileStatement(expression.deepCopy());
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        IType expressionType = expression.typecheck(typeEnv);
        if (!expressionType.equals(new StringType())) {
            throw new StatementException("CloseReadFileStatement: " +
                    "the filename must be a string");
        }
        return typeEnv;
    }
}
