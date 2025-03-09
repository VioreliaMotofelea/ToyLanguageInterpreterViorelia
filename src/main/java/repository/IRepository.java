package repository;

import exceptions.RepositoryException;
import model.states.ProgramState;

import java.util.List;

public interface IRepository {
    List<ProgramState> getProgramStateList();
    void setProgramStateList(List<ProgramState> newStates);
    void add(ProgramState state);
    void logProgramStateExec(ProgramState programState) throws RepositoryException;
}
