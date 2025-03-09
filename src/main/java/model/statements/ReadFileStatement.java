package model.statements;

import exceptions.AdtsException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adts.IMyMap;
import model.expressions.IExpression;
import model.states.ProgramState;
import model.types.IType;
import model.types.IntType;
import model.types.StringType;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStatement implements IStatement {
    private final IExpression expression;
    private final String variableName;

    public ReadFileStatement(IExpression expression, String variableName) {
        this.expression = expression;
        this.variableName = variableName;
    }

    @Override
    public String toString() {
        return "readFile(" + expression.toString() + ", " + variableName + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException, AdtsException {
        IMyMap<String, IValue> symTable = state.getSymTable();
        if (!symTable.contains(variableName)) {
            throw new StatementException("Variable name is not defined");
        }
        if (!symTable.get(variableName).getType().equals(new IntType())) {
            throw new StatementException("Variable is not of type INT");
        }

        IValue expressionValue = expression.evaluate(symTable, state.getHeap());
        if (!expressionValue.getType().equals(new StringType())) {
            throw new StatementException("Value read is not a string");
        }

        BufferedReader br = state.getFileTable().get(((StringValue) expressionValue));

        try {
            String readResult = br.readLine();
            if (readResult == "") {
                readResult = "0";
            }
            int parsedResult = Integer.parseInt(readResult);
            symTable.put(variableName, new IntValue(parsedResult));
            return null;
        } catch (IOException e) {
            throw new StatementException("I/O Exception trying to read file " + ((StringValue) expressionValue).getValue());
        }
    }

    @Override
    public IStatement deepCopy() {
        return new ReadFileStatement(expression.deepCopy(), variableName);
    }

    @Override
    public IMyMap<String, IType> typecheck(IMyMap<String, IType> typeEnv) throws StatementException, ExpressionException, AdtsException {
        IType expressionType = expression.typecheck(typeEnv);
        if (!expressionType.equals(new StringType())) {
            throw new StatementException("ReadFileStatement: " +
                    "the filename must be a string");
        }

        if (!typeEnv.contains(variableName)) {
            throw new StatementException("ReadFileStatement: " +
                    "the variable " + variableName + " is not declared");
        }

        IType variableType = typeEnv.get(variableName);
        if (!variableType.equals(new IntType())) {
            throw new StatementException("ReadFileStatement: " +
                    "the variable " + variableName + " must be of type int");
        }

        return typeEnv;
    }
}
