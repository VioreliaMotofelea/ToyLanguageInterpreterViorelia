package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IMyMap;
import model.adts.IMyStack;
import model.states.ProgramState;
import model.types.IType;

public class CompoundStatement implements IStatement {
    private final IStatement firstStatement;
    private final IStatement secondStatement;

    public CompoundStatement(IStatement firstStatement, IStatement secondStatement) {
        this.firstStatement = firstStatement;
        this.secondStatement = secondStatement;
    }

    @Override
    public String toString() {
        return "(" + firstStatement.toString() + "; " + secondStatement.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException {
        IMyStack<IStatement> stack = state.getExeStack();

        stack.push(secondStatement);
        stack.push(firstStatement);

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new CompoundStatement(firstStatement.deepCopy(), secondStatement.deepCopy());
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        return secondStatement.typecheck(firstStatement.typecheck(typeEnv));
    }
}
