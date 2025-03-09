package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IMyMap;
import model.expressions.IExpression;
import model.states.ProgramState;
import model.types.BoolType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;

public class IfStatement implements IStatement {
    private final IExpression expression;
    private final IStatement thenStatement;
    private final IStatement elseStatement;

    public IfStatement(IExpression expression, IStatement thenStatement, IStatement elseStatement) {
        this.expression = expression;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public String toString() {
        return "if (" + expression.toString() + ") then {" + thenStatement.toString() + "} " +
                "else {" + elseStatement.toString() + "}";
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException, AdtsException {
        IValue expressionValue = this.expression.evaluate(state.getSymTable(), state.getHeap());
        if (!(expressionValue.getType() instanceof BoolType)) {
            throw new StatementException(expressionValue.toString() + " is not boolean");
        }

        if (((BoolValue) expressionValue).getValue()) {
            state.getExeStack().push(thenStatement);
        } else {
            state.getExeStack().push(elseStatement);
        }
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new IfStatement(expression.deepCopy(), thenStatement, elseStatement);
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        IType expressionType = expression.typecheck(typeEnv);
        if (!expressionType.equals(new BoolType())) {
            throw new StatementException("The condition of IF has not the type bool");
        }

        thenStatement.typecheck(typeEnv.deepCopy());
        elseStatement.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }
}
