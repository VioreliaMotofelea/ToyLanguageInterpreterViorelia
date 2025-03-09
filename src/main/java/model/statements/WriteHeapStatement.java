package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IMyMap;
import model.expressions.IExpression;
import model.states.ProgramState;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class WriteHeapStatement implements IStatement {
    private final String variableName;
    private final IExpression expression;

    public WriteHeapStatement(String variableName, IExpression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "wH(" + variableName + ", " + expression.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException, AdtsException {
        if (!state.getSymTable().contains(variableName)) {
            throw new StatementException("The variable " + variableName + " is not defined.");
        }

        IType variableType = state.getSymTable().get(variableName).getType();
        if (!(variableType instanceof RefType)) {
            throw new StatementException("It is not a RefType.");
        }

        RefValue variableValue = (RefValue) state.getSymTable().get(variableName);
        int address = variableValue.getAddress();
        if (!state.getHeap().exists(address)) {
            throw new StatementException("The address " + address + " is not defined in the Heap.");
        }

        IValue expressionValue = expression.evaluate(state.getSymTable(), state.getHeap());
        if (!expressionValue.getType().equals(variableValue.getLocationType())) {
            throw new StatementException("The type of the evaluated expression does not match the variable's location type.");
        }

        state.getHeap().set(address, expressionValue);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new WriteHeapStatement(variableName, expression.deepCopy());
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        if (!typeEnv.contains(variableName)) {
            throw new StatementException("WriteHeapStatement: " +
                    "the variable " + variableName + " is not declared");
        }

        IType variableType =  typeEnv.get(variableName);
        if (!(variableType instanceof RefType)) {
            throw new StatementException("WriteHeapStatement: " +
                    "the variable " + variableName + " must be a RefType");
        }

        IType innerType = ((RefType) variableType).getInner();
        IType expressionType = expression.typecheck(typeEnv);
        if (!innerType.equals(expressionType)) {
            throw new StatementException("WriteHeapStatement: " +
                    "the inner type of the reference does not match the expression type");
        }

        return typeEnv;
    }
}
