package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.CommandHandler;

import java.util.List;

public class GodChoiceCommand extends Command {
    private final List<String> chosenGods;

    public GodChoiceCommand(List<String> chosenGods) {
        super(CommandType.SELECT);
        this.chosenGods = chosenGods;
    }

    public List<String> getChosenGods() {
        return chosenGods;
    }

    @Override
    public void handleCommand(CommandHandler handler) {
        handler.handle(this);
    }
}
