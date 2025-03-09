package repository;

import exceptions.RepositoryException;
import model.states.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private final List<ProgramState> states;
    private final String fileName;

    public Repository(String fileName) {
        this.states = new ArrayList<ProgramState>();
        this.fileName = fileName;
    }

    @Override
    public void add(ProgramState state) {
        states.add(state);
    }

    @Override
    public List<ProgramState> getProgramStateList() {
        return states;
    }

    @Override
    public void setProgramStateList(List<ProgramState> newStates) {
        states.clear();
        states.addAll(newStates);
    }

    @Override
    public void logProgramStateExec(ProgramState programState) throws RepositoryException {
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(this.fileName, true)));
            writer.println(programState.toString());
            writer.close();
        } catch (IOException io) {
            throw new RepositoryException("Cannot write to the file: " + fileName);
        }
    }
}
