package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IMyMap;
import model.adts.IMyStack;
import model.expressions.IExpression;
import model.states.ProgramState;
import model.types.IType;
import model.values.IValue;

public class SwitchStatement implements IStatement {
    private final IExpression expression;
    private final IExpression caseExpression1;
    private final IStatement caseStatement1;
    private final IExpression caseExpression2;
    private final IStatement caseStatement2;
    private final IStatement defaultStatement;

    public SwitchStatement(IExpression expression, IExpression caseExpression1, IStatement caseStatement1,
                           IExpression caseExpression2, IStatement caseStatement2, IStatement defaultStatement) {
        this.expression = expression;
        this.caseExpression1 = caseExpression1;
        this.caseStatement1 = caseStatement1;
        this.caseExpression2 = caseExpression2;
        this.caseStatement2 = caseStatement2;
        this.defaultStatement = defaultStatement;
    }

    @Override
    public String toString() {
        return "switch(" + expression + ") (case " + caseExpression1 + ": " + caseStatement1 +
                ") (case " + caseExpression2 + ": " + caseStatement2 +
                ") (default: " + defaultStatement + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException, AdtsException {
        IMyStack<IStatement> stack = state.getExeStack();
        IMyMap<String, IValue> symTable = state.getSymTable();

        IValue exprValue = expression.evaluate(symTable, state.getHeap());
        IValue case1Value = caseExpression1.evaluate(symTable, state.getHeap());
        IValue case2Value = caseExpression2.evaluate(symTable, state.getHeap());

        if (exprValue.equals(case1Value)) {
            stack.push(caseStatement1);
        } else if (exprValue.equals(case2Value)) {
            stack.push(caseStatement2);
        } else {
            stack.push(defaultStatement);
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new SwitchStatement(expression.deepCopy(), caseExpression1.deepCopy(), caseStatement1.deepCopy(),
                caseExpression2.deepCopy(), caseStatement2.deepCopy(), defaultStatement.deepCopy());
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        IType exprType = expression.typecheck(typeEnv);
        IType case1Type = caseExpression1.typecheck(typeEnv);
        IType case2Type = caseExpression2.typecheck(typeEnv);

        if (!exprType.equals(case1Type) || !exprType.equals(case2Type)) {
            throw new StatementException("Switch expression and case expressions must have the same type.");
        }

        caseStatement1.typecheck(typeEnv.deepCopy());
        caseStatement2.typecheck(typeEnv.deepCopy());
        defaultStatement.typecheck(typeEnv.deepCopy());

        return typeEnv;
    }
}
