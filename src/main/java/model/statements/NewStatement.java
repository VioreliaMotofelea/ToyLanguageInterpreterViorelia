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

public class NewStatement implements IStatement {
    private final String variableName;
    private final IExpression expression;

    public NewStatement(String variableName, IExpression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "new(" + variableName + ", " + expression.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException, AdtsException {
        if (!state.getSymTable().contains(variableName)) {
            throw new StatementException("Variable " + variableName + " is not defined");
        }

        IType variableType = state.getSymTable().get(variableName).getType();
        if (!(variableType instanceof RefType varRefType)) {
            throw new StatementException("Variable " + variableName + "is not a RefType.");
        }

        IValue expressionValue = expression.evaluate(state.getSymTable(), state.getHeap());
        if (!expressionValue.getType().equals(varRefType.getInner())) {
            if (expressionValue instanceof RefValue && ((RefValue) expressionValue).getLocationType().equals(varRefType.getInner())) {
                int address = ((RefValue) expressionValue).getAddress();
                state.getSymTable().put(variableName, new RefValue(address, varRefType.getInner()));
                return state;
            }
            throw new StatementException("Type of the evaluated expression does not match the locationType");
        }

        int address = state.getHeap().allocate(expressionValue);
        state.getSymTable().put(variableName, new RefValue(address, varRefType.getInner()));

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new NewStatement(variableName, expression.deepCopy());
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        IType variableType = typeEnv.get(variableName);
        IType expressionType = expression.typecheck(typeEnv);

        if (!variableType.equals(new RefType(expressionType))) {
            throw new StatementException("NewStatement: " +
                    "right hand side and left hand side have different types");
        }
        return typeEnv;
    }
}
