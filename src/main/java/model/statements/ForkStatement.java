package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IMyMap;
import model.adts.MyStack;
import model.states.ProgramState;
import model.types.IType;

public class ForkStatement implements IStatement {
    IStatement statement;
    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }

    @Override
    public String toString() {
        return "fork(" + statement.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException, AdtsException {
        return new ProgramState(
                new MyStack<>(),
                state.getSymTable().deepCopy(),
                state.getOut(),
                this.statement,
                state.getFileTable(),
                state.getHeap(),
                state.getBarrierTable());
    }

    @Override
    public IStatement deepCopy() {
        return new ForkStatement(statement.deepCopy());
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        statement.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }
}
