package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IMyMap;
import model.states.ProgramState;
import model.types.IType;

public class VariableDeclarationStatement implements IStatement {
    private final String name;
    private final IType type;

    public VariableDeclarationStatement(String name, IType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return type.toString() + " " + name;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException {
        if (state.getSymTable().contains(name)) {
            throw new StatementException("Variable " + name + " is already declared");
        }

        state.getSymTable().put(name, type.defaultValue());
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new VariableDeclarationStatement(name, type);
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        typeEnv.put(name, type);
        return typeEnv;
    }
}
