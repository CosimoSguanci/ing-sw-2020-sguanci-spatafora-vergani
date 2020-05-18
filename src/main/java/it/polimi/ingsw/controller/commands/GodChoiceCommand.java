package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.CommandHandler;

import java.util.List;

public class GodChoiceCommand extends Command {
    private final List<String> chosenGods;
    //private final boolean isGodChooser;

    public GodChoiceCommand(List<String> chosenGods) { // boolean isGodChooser
        super(CommandType.SELECT);
        this.chosenGods = chosenGods;
        //this.isGodChooser = isGodChooser;
    }

    public List<String> getChosenGods() {
        return chosenGods;
    }

    /*public boolean isGodChooser() {
        return isGodChooser;
    }*/

    @Override
    public void handleCommand(CommandHandler handler) {
        handler.handle(this);
    }
}
