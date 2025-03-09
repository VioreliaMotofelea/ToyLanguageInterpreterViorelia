package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IMyMap;
import model.expressions.IExpression;
import model.states.ProgramState;
import model.types.IType;
import model.values.IValue;

public class AssignmentStatement implements IStatement {
    private final String variable;
    private final IExpression expression;

    public AssignmentStatement(String variable, IExpression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return variable + " = " + expression.toString();
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException, AdtsException {
        if (!state.getSymTable().contains(this.variable)) {
            throw new StatementException("The variable is not defined");
        }

        IValue evalValue = expression.evaluate(state.getSymTable(), state.getHeap());
        IType varType = state.getSymTable().get(this.variable).getType();

        if (!(evalValue.getType().equals(varType))) {
            throw new StatementException("The used variable " + variable + " was not declared before");
        }

        state.getSymTable().put(this.variable, evalValue);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new AssignmentStatement(variable, expression.deepCopy());
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        IType variableType = typeEnv.get(variable);
        IType expressionType = expression.typecheck(typeEnv);

        if (!variableType.equals(expressionType)) {
            throw new StatementException("AssignmentStatement: " +
                    "right hand side and left hand side have different types");
        }
        return typeEnv;
    }
}
