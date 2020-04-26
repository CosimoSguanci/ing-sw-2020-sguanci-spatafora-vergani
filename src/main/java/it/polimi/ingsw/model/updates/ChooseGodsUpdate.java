package it.polimi.ingsw.model.updates;

import java.util.List;

public class ChooseGodsUpdate extends PlayerSpecificUpdate {

    public final boolean isGodChooser;
    public final List<String> selectableGods;

    public ChooseGodsUpdate(String playerID, boolean isGodChooser, List<String> selectableGods) {
        super(playerID);
        this.isGodChooser = isGodChooser;
        this.selectableGods = !isGodChooser ? selectableGods : null;
    }
}
