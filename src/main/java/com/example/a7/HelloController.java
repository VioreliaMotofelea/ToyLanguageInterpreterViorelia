package com.example.a7;

import controller.Controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.adts.*;
import model.statements.IStatement;
import model.states.ProgramState;
import model.util.Pair;
import model.values.IValue;
import model.values.StringValue;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class HelloController implements Initializable {
    @FXML
    private ListView<String> execution_stack_view;
    @FXML
    private TableView<Map.Entry<String, IValue>> symbol_table_view;
    @FXML
    private TableColumn<Map.Entry<String, IValue>, String> symbol_table_names;
    @FXML
    private TableColumn<Map.Entry<String, IValue>, String> symbol_table_values;
    @FXML
    private ListView<String> output_view;
    @FXML
    private ListView<String> file_table_view;
    @FXML
    private TableView<Map.Entry<Integer, IValue>> heap_table_view;
    @FXML
    private TableColumn<Map.Entry<Integer, IValue>, Integer> heap_table_addresses;
    @FXML
    private TableColumn<Map.Entry<Integer, IValue>, String> heap_table_values;

    @FXML
    private TableView<Map.Entry<Integer, Pair<Integer, List<Integer>>>> barrier_table_view;
    @FXML
    private TableColumn<Map.Entry<Integer, Pair<Integer, List<Integer>>>, Integer> barrier_table_index_column;
    @FXML
    private TableColumn<Map.Entry<Integer, Pair<Integer, List<Integer>>>, Integer> barrier_table_value_column;
    @FXML
    private TableColumn<Map.Entry<Integer, Pair<Integer, List<Integer>>>, String> barrier_table_list_column;

    @FXML
    private Label program_states_counter;
    @FXML
    private Button execute_button;
    @FXML
    private ListView<Integer> program_id_view;

    private Controller controller;

    public Controller get_controller() {
        return this.controller;
    }

    public void set_controller(Controller controller) {
        this.controller = controller;
        populate_program_states_counter();
        populate_id_view();
        execute_button.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.controller = null;

        heap_table_addresses.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        heap_table_values.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue() + " "));

        symbol_table_names.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey() + " "));
        symbol_table_values.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue() + " "));

        barrier_table_index_column.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        barrier_table_value_column.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getValue().getFirst()).asObject());
        barrier_table_list_column.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getSecond().toString()));

        program_id_view.setOnMouseClicked(mouseEvent -> change_program_state_handler(get_selected_program()));

        execute_button.setDisable(true);
    }

    private void change_program_state_handler(ProgramState current_program_state) {
        if (current_program_state == null) {
            return;
        }
        try {
            populate_program_states_counter();
            populate_id_view();
            populate_heap_table_view(current_program_state);
            populate_output_view(current_program_state);
            populate_file_table_view(current_program_state);
            populate_execution_stack_view(current_program_state);
            populate_symbol_table_view(current_program_state);
            populate_barrier_table_view(current_program_state);
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR, e.getMessage());
            error.show();
        }
    }

    public void execute_one_step_handler(ActionEvent actionEvent) {
        if (controller == null) {
            Alert error = new Alert(Alert.AlertType.ERROR, "No Program Selected!");
            error.show();
            execute_button.setDisable(true);
            return;
        }

        ProgramState program_state = get_selected_program();
        if (program_state != null && !program_state.isNotCompleted()) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Nothing To Execute!");
            error.show();
            return;
        }

        try {
            controller.oneStepForAllProgram(controller.getRepository().getProgramStateList());
            change_program_state_handler(program_state);

            if (controller.getRepository().getProgramStateList().isEmpty()) {
                execute_button.setDisable(true);
            }
        } catch (InterruptedException e) {
            Alert error = new Alert(Alert.AlertType.ERROR, e.getMessage());
            error.show();
        }
    }

    private void populate_program_states_counter() {
        program_states_counter.setText("Number of Program States: " + controller.getRepository().getProgramStateList().size());
    }

    private void populate_heap_table_view(ProgramState program) {
        heap_table_view.setItems(FXCollections.observableList(new ArrayList<>(program.getHeap().getContent().entrySet())));
        heap_table_view.refresh();
    }

    private void populate_output_view(ProgramState program) throws Exception {
        output_view.setItems(FXCollections.observableArrayList(program.getOut().getAll().stream()
                .map(Object::toString).collect(Collectors.toList())));
    }

    public void populate_file_table_view(ProgramState program) {
        file_table_view.setItems(FXCollections.observableArrayList(program.getFileTable().getFileTable().keySet()
                .stream().map(StringValue::toString).collect(Collectors.toList())));
    }

    private void populate_id_view() {
        program_id_view.setItems(FXCollections.observableArrayList(controller.getRepository().getProgramStateList().stream().map(ProgramState::getId).collect(Collectors.toList())));
        program_id_view.refresh();
    }

    private void populate_execution_stack_view(ProgramState program) {
        IMyStack<IStatement> stack = program.getExeStack();
        List<String> stack_output = new ArrayList<>();

        for (IStatement statement : stack.getContent()) {
            stack_output.add(statement.toString());
        }

        Collections.reverse(stack_output);
        execution_stack_view.setItems(FXCollections.observableArrayList(stack_output));
    }

    private void populate_symbol_table_view(ProgramState program) {
        symbol_table_view.setItems(FXCollections.observableList(new ArrayList<>(program.getSymTable().getContent().entrySet())));
        symbol_table_view.refresh();
    }

    private void populate_barrier_table_view(ProgramState program) {
        barrier_table_view.setItems(FXCollections.observableList(
                new ArrayList<>(program.getBarrierTable().getContent().entrySet())
        ));
        barrier_table_view.refresh();
    }


    private ProgramState get_selected_program() {
        if (program_id_view.getSelectionModel().getSelectedIndex() == -1) {
            return controller.getRepository().getProgramStateList().stream().findFirst().orElse(null);
        } else {
            int selected_id = program_id_view.getSelectionModel().getSelectedItem();
            return controller.getRepository().getProgramStateList().stream()
                    .filter(programState -> programState.getId() == selected_id)
                    .findFirst().orElse(null);
        }
    }
}
