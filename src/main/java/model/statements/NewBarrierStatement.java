package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IBarrierTable;
import model.adts.IMyHeap;
import model.adts.IMyMap;
import model.expressions.IExpression;
import model.states.ProgramState;
import model.types.IType;
import model.types.IntType;
import model.util.Pair;
import model.values.IValue;
import model.values.IntValue;

import java.util.ArrayList;

public class NewBarrierStatement implements IStatement {
    private final String variable;
    private final IExpression expression;

    public NewBarrierStatement(String variable, IExpression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "newBarrier(" + variable + ", " + expression + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException, AdtsException {
        IMyMap<String, IValue> symTable = state.getSymTable();
        IBarrierTable barrierTable = state.getBarrierTable();
        IMyHeap<IValue> heap = state.getHeap();

        IValue evaluated = expression.evaluate(symTable, heap);
        if (!(evaluated instanceof IntValue)) {
            throw new StatementException("Expression must evaluate to an integer.");
        }

        int number = ((IntValue) evaluated).getValue();

        int newLocation = barrierTable.getNextFreeLocation();
        barrierTable.put(newLocation, new Pair<>(number, new ArrayList<>()));

        if (symTable.contains(variable)) {
            symTable.update(variable, new IntValue(newLocation));
        } else {
            symTable.put(variable, new IntValue(newLocation));
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new NewBarrierStatement(variable, expression.deepCopy());
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        if (!typeEnv.contains(variable)) {
            typeEnv.put(variable, new IntType());
        } else if (!typeEnv.get(variable).equals(new IntType())) {
            throw new StatementException("Variable " + variable + " must be of type int.");
        }

        IType expressionType = expression.typecheck(typeEnv);
        if (!expressionType.equals(new IntType())) {
            throw new StatementException("Expression " + expression + " must evaluate to an int.");
        }

        return typeEnv;
    }
}
