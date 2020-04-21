package it.polimi.ingsw.controller;

import java.io.Serializable;
import java.util.List;

public class GodChoiceCommand implements Serializable {
    private List<String> chosenGods;
    private boolean isGodChooser;

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
}
