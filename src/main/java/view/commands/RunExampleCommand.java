package view.commands;

import controller.Controller;

public class RunExampleCommand extends Command {
    private final Controller controller;
    private boolean hasRun;

    public RunExampleCommand(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
        this.hasRun = false;
    }

    @Override
    public void execute() {
        try {
            if (hasRun) {
                throw new RuntimeException("\nProgram has already run once!\n");
            }
            this.controller.allStep();
            this.hasRun = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
