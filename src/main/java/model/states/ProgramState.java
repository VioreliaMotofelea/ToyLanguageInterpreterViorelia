package model.states;

import model.adts.*;
import model.statements.IStatement;
import model.values.IValue;

public class ProgramState {
    private IMyStack<IStatement> exeStack;
    private IMyMap<String, IValue> symTable;
    private IMyList<IValue> out;
    private IFileTable fileTable;
    private IMyHeap<IValue> heap;
    private IStatement originalProgram;
    private static int nextId = 1;
    private final int id;

    private IBarrierTable barrierTable;

    public ProgramState(IMyStack<IStatement> exeStack,
                        IMyMap<String, IValue> symTable,
                        IMyList<IValue> out,
                        IStatement originalProgram,
                        IFileTable fileTable,
                        IMyHeap<IValue> heap,
                        IBarrierTable barrierTable) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.originalProgram = originalProgram.deepCopy();
        this.exeStack.push(originalProgram);
        this.id = getNextId();

        this.barrierTable = barrierTable;
    }

    public IMyStack<IStatement> getExeStack() {
        return this.exeStack;
    }
    public IMyMap<String, IValue> getSymTable() {
        return this.symTable;
    }
    public IMyList<IValue> getOut() {
        return this.out;
    }
    public IFileTable getFileTable() {
        return this.fileTable;
    }
    public IMyHeap<IValue> getHeap() {
        return this.heap;
    }

    public IBarrierTable getBarrierTable() {
        return barrierTable;
    }

    public void setExeStack(IMyStack<IStatement> exeStack) { this.exeStack = exeStack; }
    public void setSymTable(IMyMap<String, IValue> symTable) { this.symTable = symTable; }
    public void setOut(IMyList<IValue> out) { this.out = out; }
    public void setHeap(IMyHeap<IValue> heap) { this.heap = heap; }
    public void setOriginalProgram(IStatement originalProgram) { this.originalProgram = originalProgram; }
    public void setFileTable(IFileTable fileTable) { this.fileTable = fileTable; }

    public void setBarrierTable(IBarrierTable barrierTable) {
        this.barrierTable = barrierTable;
    }

    public ProgramState oneStep() throws Exception {
        if (exeStack.isEmpty()) {
            throw new Exception("The program state is empty");
        }
        IStatement currentStatement = exeStack.pop();
        return currentStatement.execute(this);
    }

    public int getId() {
        return this.id;
    }
    public static synchronized int getNextId() {
        return nextId++;
    }
    public boolean isNotCompleted() {
        return !this.exeStack.isEmpty();
    }

    @Override
    public String toString() {
        return "Program State - Current program with id: " + id + "\n" +
                "Execution Stack = { " + exeStack.toString() + "}\n" +
                "Symbol Table = { " + symTable.toString() + "}\n" +
                "File Table = { " + fileTable.toString() + "}\n" +
                "Heap = { " + heap.toString() + "}\n" +
                "Output = { " + out.toString() + "}\n";
    }
}
