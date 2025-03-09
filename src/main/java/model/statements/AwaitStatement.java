package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IBarrierTable;
import model.adts.IMyMap;
import model.adts.IMyStack;
import model.states.ProgramState;
import model.types.IType;
import model.types.IntType;
import model.util.Pair;
import model.values.IValue;
import model.values.IntValue;

import java.util.List;

public class AwaitStatement implements IStatement {
    private final String variable;
    public AwaitStatement(String variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return "await(" + variable + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException, AdtsException {
        IMyStack<IStatement> exeStack = state.getExeStack();
        IMyMap<String, IValue> symTable = state.getSymTable();
        IBarrierTable barrierTable = state.getBarrierTable();

        if (!symTable.contains(variable)) {
            throw new StatementException("Variable " + variable + " is not defined in the symbol table.");
        }

        IValue value = symTable.get(variable);
        if (!(value instanceof IntValue)) {
            throw new StatementException("Variable " + variable + " is not of type int.");
        }

        int foundIndex = ((IntValue) value).getValue();

        if (!barrierTable.containsKey(foundIndex)) {
            throw new StatementException("Index " + foundIndex + " not found in the BarrierTable.");
        }

        var barrierEntry = barrierTable.get(foundIndex); // the pair
        int N1 = barrierEntry.getFirst();
        List<Integer> L1 = barrierEntry.getSecond();

        int NL = L1.size();

        if (N1 > NL) {
            if (!L1.contains(state.getId())) {
                L1.add(state.getId());
                barrierTable.update(foundIndex, new Pair<>(N1, L1));
            }

            exeStack.push(this);
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new AwaitStatement(variable);
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        if (!typeEnv.contains(variable)) {
            throw new StatementException("Variable " + variable + " is not defined.");
        }

        IType type = typeEnv.get(variable);
        if (!type.equals(new IntType())) {
            throw new StatementException("Variable " + variable + " must be of type int.");
        }

        return typeEnv;
    }
}
