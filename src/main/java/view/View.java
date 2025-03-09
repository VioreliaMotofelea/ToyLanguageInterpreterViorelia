package view;

import view.commands.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class View {
    Map<String, Command> commands;
    public View() {
        this.commands = new HashMap<>();
    }

    public void addCommand(String commandName, Command command) {
        commands.put(commandName, command);
    }

    private void printMenu() {
        for (Command command : commands.values()) {
            String line = String.format("%4s: %s", command.getKey(), command.getDescription());
            System.out.println(line);
        }
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            this.printMenu();
            System.out.println("\nInput the option: ");
            String chosenKey = scanner.nextLine();

            if (!commands.containsKey(chosenKey)) {
                System.out.println("Unknown command");
                continue;
            }
            commands.get(chosenKey).execute();
        }
    }
}
