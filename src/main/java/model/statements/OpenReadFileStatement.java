package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IMyMap;
import model.expressions.IExpression;
import model.states.ProgramState;
import model.types.IType;
import model.types.StringType;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenReadFileStatement implements IStatement {
    private final IExpression expression;
    public OpenReadFileStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "openRFile(" + expression.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException, AdtsException {
        IValue expressionValue = expression.evaluate(state.getSymTable(), state.getHeap());
        if (!(expressionValue.getType() instanceof StringType)) {
            throw new StatementException("The expression is not a string");
        }

        StringValue filename = ((StringValue) expressionValue);
        if (state.getFileTable().contains(filename)) {
            throw new StatementException("File already open");
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename.getValue()));
            state.getFileTable().put(filename, br); // add the BufferedReader to the FileTable
        } catch (FileNotFoundException e) {
            throw new StatementException("File not found: " + filename);
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new OpenReadFileStatement(expression.deepCopy());
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        IType expressionType = expression.typecheck(typeEnv);
        if (!expressionType.equals(new StringType())) {
            throw new StatementException("OpenReadFileStatement: " +
                    "the filename must be a string");
        }
        return typeEnv;
    }
}
