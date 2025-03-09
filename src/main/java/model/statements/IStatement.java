package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IMyMap;
import model.states.ProgramState;
import model.types.IType;

public interface IStatement {
    ProgramState execute(ProgramState state) throws StatementException, ExpressionException, AdtsException;
    IStatement deepCopy();
    IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException;
}
