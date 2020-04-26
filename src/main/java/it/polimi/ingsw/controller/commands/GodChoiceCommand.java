package it.polimi.ingsw.controller.commands;

import java.util.List;

public class GodChoiceCommand extends Command {
    private List<String> chosenGods;
    private boolean isGodChooser;
    private String playerID;

    public GodChoiceCommand(List<String> chosenGods, boolean isGodChooser) {
        this.chosenGods = chosenGods;
        this.isGodChooser = isGodChooser;
    }

    public List<String> getChosenGods() {
        return chosenGods;
    }

    public boolean isGodChooser() {
        return isGodChooser;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getPlayerID() {
        return this.playerID;
    }
}