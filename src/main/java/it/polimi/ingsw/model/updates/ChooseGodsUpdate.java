package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

import java.util.List;

public class ChooseGodsUpdate extends Update {

    public final boolean isGodChooser;
    public final List<String> selectableGods; // Gods instead of strings?

    public ChooseGodsUpdate(String playerID, boolean isGodChooser, List<String> selectableGods) {
        super(playerID, null);
        this.isGodChooser = isGodChooser;
        this.selectableGods = !isGodChooser ? selectableGods : null;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }

}
