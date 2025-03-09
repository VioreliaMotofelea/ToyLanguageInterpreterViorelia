package controller;

import exceptions.*;
import model.states.ProgramState;
import repository.IRepository;
import utilities.GarbageCollector;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private final IRepository repository;
    private ExecutorService executorService;

    public Controller(IRepository repository) {
        this.repository = repository;
    }

    public void oneStepForAllProgram(List<ProgramState> programStates) throws InterruptedException {
//        programStates.forEach(programState -> {
//            try {
//                repository.logProgramStateExec(programState);
//            } catch (RepositoryException e) {
//                throw new RuntimeException(e);
//            }
//        });
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(2);
        }

        List<Callable<ProgramState>> callableList = programStates.stream()
                .map(programState -> (Callable<ProgramState>) programState::oneStep)
                .collect(Collectors.toList());

        List<ProgramState> newProgramStates = executorService.invokeAll(callableList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }).filter(Objects::nonNull).toList();

        //programStates.addAll(newProgramStates);
        programStates.forEach(programState -> {
            try {
                repository.logProgramStateExec(programState);
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
        });

        var currentProgramStates = removeCompletedProgram(repository.getProgramStateList());
        currentProgramStates.addAll(newProgramStates);
        repository.setProgramStateList(currentProgramStates);
    }

    public void allStep() throws ControllerException, RepositoryException, StatementException, AdtsException, ExpressionException, InterruptedException {
        executorService = Executors.newFixedThreadPool(2);

        List<ProgramState> programStates = removeCompletedProgram(repository.getProgramStateList());

        while (!programStates.isEmpty()) {
            programStates.forEach(programState -> {
                try {
                    repository.logProgramStateExec(programState);
                } catch (RepositoryException e) {
                    throw new RuntimeException(e);
                }
            });

            /*programStates.forEach(programState -> programState.getHeap().setContent(
                    GarbageCollector.safeGarbageCollector(
                            GarbageCollector.getAddressesFromSymTable(programState.getSymTable().getContent().values()),
                            programState.getHeap().getContent()
                    )
            ));*/

            programStates.forEach(programState -> programState.getHeap().setContent(
                    GarbageCollector.safeGarbageCollector(
                            getAllAddressesFromSymTables(),
                            programState.getHeap().getContent()
                    )
            ));

            oneStepForAllProgram(programStates);
            programStates = removeCompletedProgram(repository.getProgramStateList());
        }

        executorService.shutdown();
        repository.setProgramStateList(programStates);
    }

    public List<ProgramState> removeCompletedProgram(List<ProgramState> inProgramList) {
        return inProgramList.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }

    public List<Integer> getAllAddressesFromSymTables() {
        return this.repository.getProgramStateList().stream()
                .map(programState -> GarbageCollector.getAddressesFromSymTable(programState.getSymTable().getContent().values()))
                .reduce(Stream.of(0).collect(Collectors.toList()),
                        (accumulator, item) -> Stream.concat(accumulator.stream(), item.stream())
                                .collect(Collectors.toList()));
    }

    public ExecutorService getExecutorService() {
        return this.executorService;
    }

    public IRepository getRepository() {
        return this.repository;
    }
}
