package com.example.a7;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import model.adts.*;
import model.expressions.*;
import model.statements.*;
import model.states.ProgramState;
import model.types.*;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;
import repository.IRepository;
import repository.Repository;
import view.View;
import view.commands.RunExampleCommand;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SecondaryController implements Initializable {
    @FXML
    private Button select_button;
    @FXML
    private ListView<IStatement> select_programs_list_view;

    private HelloController main_window_controller;

    public HelloController get_main_window_controller() {
        return main_window_controller;
    }

    public void set_main_window_controller(HelloController main_window_controller) {
        this.main_window_controller = main_window_controller;
    }

    private Map<String, IStatement> programMap = new HashMap<>();

    @FXML
    private void select_program() {
        try {
            IStatement selectedProgram = select_programs_list_view.getSelectionModel().getSelectedItem();
            if (selectedProgram == null) {
                throw new Exception("No program selected!");
            }

            int index = select_programs_list_view.getSelectionModel().getSelectedIndex();

            IRepository repository = new Repository("log" + (index + 1) + ".txt");

            IMyStack<IStatement> exeStack = new MyStack<>();
            IMyMap<String, IValue> symTable = new MyMap<>();
            IMyList<IValue> out = new MyList<>();
            IFileTable fileTable = new FileTable();
            IMyHeap<IValue> heap = new MyHeap();
            IBarrierTable barrierTable = new BarrierTable();

            IMyMap<String, IType> typeEnvironment = new MyMap<>();
            selectedProgram.typecheck(typeEnvironment);

            ProgramState currentProgramState = new ProgramState(
                    exeStack,
                    symTable,
                    out,
                    selectedProgram,
                    fileTable,
                    heap,
                    barrierTable);

            repository.add(currentProgramState);
            Controller controller = new Controller(repository);

            String description = "";
            String key = String.valueOf(index);
            var command = new RunExampleCommand(key, description, controller);
            View view = new View();
            view.addCommand(key, command);

            main_window_controller.set_controller(controller);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }

    private static Map<String, IStatement> getPredefinedPrograms() {
        Map<String, IStatement> programMap = new HashMap<>();

        IStatement example1 = new CompoundStatement(
                new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(
                        new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))
                )
        );
        programMap.put("Example 1: int v; v = 2; Print(v);", example1);

        IStatement example2 = new CompoundStatement(
                new VariableDeclarationStatement("a", new IntType()),
                new CompoundStatement(
                        new VariableDeclarationStatement("b", new IntType()),
                        new CompoundStatement(
                                new AssignmentStatement("a", new ArithmeticExpression('+', new ValueExpression(new IntValue(2)),
                                        new ArithmeticExpression('*', new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5))))),
                                new CompoundStatement(
                                        new AssignmentStatement("b", new ArithmeticExpression('+', new VariableExpression("a"), new ValueExpression(new IntValue(1)))),
                                        new PrintStatement(new VariableExpression("b"))
                                )
                        )
                )
        );
        programMap.put("Example 2: int a; int b; a = 2 + 3 * 5; b = a + 1; Print(b);", example2);

        IStatement example3 = new CompoundStatement(
                new VariableDeclarationStatement("a", new BoolType()),
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(
                                new AssignmentStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(
                                        new IfStatement(new VariableExpression("a"),
                                                new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                                                new AssignmentStatement("v", new ValueExpression(new IntValue(3)))
                                        ),
                                        new PrintStatement(new VariableExpression("v"))
                                )
                        )
                )
        );
        programMap.put("Example3: bool a; int v; a = true; if (a) then {v = 2} else {v = 3}; Print(v);", example3);

        IStatement example4 = new CompoundStatement(
                new VariableDeclarationStatement("varf", new StringType()),
                new CompoundStatement(
                        new AssignmentStatement("varf", new ValueExpression(new StringValue("test.in"))),
                        new CompoundStatement(
                                new OpenReadFileStatement(new VariableExpression("varf")),
                                new CompoundStatement(
                                        new VariableDeclarationStatement("varc", new IntType()),
                                        new CompoundStatement(
                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("varc")),
                                                        new CompoundStatement(
                                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                                new CompoundStatement(
                                                                        new PrintStatement(new VariableExpression("varc")),
                                                                        new CloseReadFileStatement(new VariableExpression("varf"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        programMap.put("Example 4: string varf; varf = 'test.in'; openRFile(varf); int varc; readFile(varf, varc); Print(varc)); readFile(varf, varc); Print(varc)); closeRFile(varf);", example4);

        IStatement example5 = new CompoundStatement(
                new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(
                        new AssignmentStatement("v", new ValueExpression(new IntValue(4))),
                        new CompoundStatement(
                                new WhileStatement(
                                        new RelationalExpression(">", new VariableExpression("v"), new ValueExpression(new IntValue(0))),
                                        new CompoundStatement(
                                                new PrintStatement(new VariableExpression("v")),
                                                new AssignmentStatement("v", new ArithmeticExpression(
                                                        '-',
                                                        new VariableExpression("v"),
                                                        new ValueExpression(new IntValue(1))
                                                ))
                                        )
                                ),
                                new PrintStatement(new VariableExpression("v"))
                        )
                )
        );
        programMap.put("Example 5: int v; v = 4; while (v > 0) { Print(v); v = v - 1; } Print(v);", example5);

        IStatement example6 = new CompoundStatement(
                new VariableDeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(
                        new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(
                                new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(
                                        new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(
                                                new PrintStatement(new VariableExpression("v")),
                                                new PrintStatement(new VariableExpression("a"))
                                        )
                                )
                        )
                )
        );
        programMap.put("Example 6: Ref int v; new(v, 20); Ref Ref int a; new(a, v); Print(v); Print(a);", example6);

        IStatement example7 = new CompoundStatement(
                new VariableDeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(
                        new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(
                                new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(
                                        new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(
                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                                new PrintStatement(new ArithmeticExpression(
                                                        '+',
                                                        new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))),
                                                        new ValueExpression(new IntValue(5))))
                                        )
                                )
                        )
                )
        );
        programMap.put("Example 7: Ref int v; new(v, 20); Ref Ref int a; new(a, v); Print(rH(v)); Print(rH(rH(a)) + 5);", example7);

        IStatement example8 = new CompoundStatement(
                new VariableDeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(
                        new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(
                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                new CompoundStatement(
                                        new WriteHeapStatement("v", new ValueExpression(new IntValue(30))),
                                        new PrintStatement(new ArithmeticExpression(
                                                '+',
                                                new ReadHeapExpression(new VariableExpression("v")),
                                                new ValueExpression(new IntValue(5))
                                        ))
                                )
                        )
                )
        );
        programMap.put("Example 8: Ref int v; new(v, 20); Print(rH(v)); wH(v, 30); Print(rH(v) + 5);", example8);

        IStatement example9 = new CompoundStatement(
                new VariableDeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(
                        new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(
                                new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(
                                        new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(
                                                new NewStatement("v", new ValueExpression(new IntValue(30))),
                                                new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))))
                                        )
                                )
                        )
                )
        );
        programMap.put("Example 9: Ref int v; new(v, 20); Ref Ref int a; new(a, v); new(v, 30); Print(rH(rH(a)));", example9);

        IStatement example10 = new CompoundStatement(
                new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new RefType(new IntType())),
                        new CompoundStatement(
                                new AssignmentStatement("v", new ValueExpression(new IntValue(10))),
                                new CompoundStatement(
                                        new NewStatement("a", new ValueExpression(new IntValue(22))),
                                        new CompoundStatement(
                                                new ForkStatement(
                                                        new CompoundStatement(
                                                                new WriteHeapStatement("a", new ValueExpression(new IntValue(30))),
                                                                new CompoundStatement(
                                                                        new AssignmentStatement("v", new ValueExpression(new IntValue(32))),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new VariableExpression("v")),
                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                                )
                                        )
                                )
                        )
                )
        );
        programMap.put("Example 10: int v; Ref int a; v = 10; new(a, 22); fork(wH(a, 30); v = 32; print(v); Print(rH(A))); Print(v); Print(rH(a));", example10);

        IStatement example11 = new CompoundStatement(
                new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(
                        new VariableDeclarationStatement("x", new IntType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("y", new IntType()),
                                new CompoundStatement(
                                        new AssignmentStatement("v", new ValueExpression(new IntValue(0))),
                                        new CompoundStatement(
                                                new RepeatUntilStatement(
                                                        new CompoundStatement(
                                                                new ForkStatement(
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new VariableExpression("v")),
                                                                                new AssignmentStatement("v", new ArithmeticExpression(
                                                                                        '-',
                                                                                        new VariableExpression("v"),
                                                                                        new ValueExpression(new IntValue(1))
                                                                                ))
                                                                        )
                                                                ),
                                                                new AssignmentStatement("v", new ArithmeticExpression(
                                                                        '+',
                                                                        new VariableExpression("v"),
                                                                        new ValueExpression(new IntValue(1))
                                                                ))
                                                        ),
                                                        new RelationalExpression(
                                                                "==",
                                                                new VariableExpression("v"),
                                                                new ValueExpression(new IntValue(3))
                                                        )
                                                ),
                                                new CompoundStatement(
                                                        new AssignmentStatement("x", new ValueExpression(new IntValue(1))),
                                                        new CompoundStatement(
                                                                new NoOperationStatement(),
                                                                new CompoundStatement(
                                                                        new AssignmentStatement("y", new ValueExpression(new IntValue(3))),
                                                                        new CompoundStatement(
                                                                                new NoOperationStatement(),
                                                                                new PrintStatement(
                                                                                        new ArithmeticExpression(
                                                                                                '*',
                                                                                                new VariableExpression("v"),
                                                                                                new ValueExpression(new IntValue(10))
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        programMap.put("", example11);

        IStatement example12 = new CompoundStatement(
                new VariableDeclarationStatement("v1", new RefType(new IntType())),
                new CompoundStatement(
                        new VariableDeclarationStatement("v2", new RefType(new IntType())),
                        new CompoundStatement(
                                new VariableDeclarationStatement("v3", new RefType(new IntType())),
                                new CompoundStatement(
                                        new NewStatement("v1", new ValueExpression(new IntValue(2))),
                                        new CompoundStatement(
                                                new NewStatement("v2", new ValueExpression(new IntValue(3))),
                                                new CompoundStatement(
                                                        new NewStatement("v3", new ValueExpression(new IntValue(4))),
                                                        new CompoundStatement(
                                                                new VariableDeclarationStatement("cnt", new IntType()),
                                                                new CompoundStatement(
                                                                        new NewBarrierStatement("cnt", new ReadHeapExpression(new VariableExpression("v2"))),
                                                                        new CompoundStatement(
                                                                                new ForkStatement(
                                                                                        new CompoundStatement(
                                                                                                new AwaitStatement("cnt"),
                                                                                                new CompoundStatement(
                                                                                                        new WriteHeapStatement("v1",
                                                                                                                new ArithmeticExpression(
                                                                                                                        '*',
                                                                                                                        new ReadHeapExpression(new VariableExpression("v1")),
                                                                                                                        new ValueExpression(new IntValue(10))
                                                                                                                )
                                                                                                        ),
                                                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v1")))
                                                                                                )
                                                                                        )
                                                                                ),
                                                                                new CompoundStatement(
                                                                                        new ForkStatement(
                                                                                                new CompoundStatement(
                                                                                                        new AwaitStatement("cnt"),
                                                                                                        new CompoundStatement(
                                                                                                                new WriteHeapStatement("v2",
                                                                                                                        new ArithmeticExpression(
                                                                                                                                '*',
                                                                                                                                new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                                new ValueExpression(new IntValue(10))
                                                                                                                        )
                                                                                                                ),
                                                                                                                new CompoundStatement(
                                                                                                                        new WriteHeapStatement("v2",
                                                                                                                                new ArithmeticExpression(
                                                                                                                                        '*',
                                                                                                                                        new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                                        new ValueExpression(new IntValue(10))
                                                                                                                                )
                                                                                                                        ),
                                                                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v2")))
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        ),
                                                                                        new CompoundStatement(
                                                                                                new AwaitStatement("cnt"),
                                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v3")))
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        programMap.put("Example 12: ", example12);

        return programMap;
        //return new IStatement[]{example1, example2, example3, example4, example5, example6, example7, example8, example9, example10};
    }

    private void display_programs() {
        //IStatement[] programs = getPredefinedPrograms();
        programMap = getPredefinedPrograms();
        select_programs_list_view.setItems(FXCollections.observableArrayList(programMap.values()));
        select_programs_list_view.getSelectionModel().select(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        display_programs();
    }
}
