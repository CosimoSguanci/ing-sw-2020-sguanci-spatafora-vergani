package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

import java.util.Map;

public class SelectedGodsUpdate extends Update {
    public final Map<String, String> selectedGods; // Nickname : GodName

    public SelectedGodsUpdate(Map<String, String> selectedGods) {
        super(null, null);
        this.selectedGods = selectedGods;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
