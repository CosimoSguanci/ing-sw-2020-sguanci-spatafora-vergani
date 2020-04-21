package it.polimi.ingsw.model.messages;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ChooseGodsUpdate implements Serializable {
    private String playerID;
    private boolean isGodChooser;

    private List<String> selectableGods;

    public ChooseGodsUpdate(String playerID, boolean isGodChooser, List<String> selectableGods) {
        this.playerID = playerID;
        this.isGodChooser = isGodChooser;
        this.selectableGods = !isGodChooser ? selectableGods : null;
    }

    public String getPlayerID() {
        return this.playerID;
    }

    public boolean isGodChooser() {
        return this.isGodChooser;
    }

    public List<String> getSelectableGods() {
        return this.selectableGods;
    }
}
