package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

import java.util.List;
import java.util.Map;

public class GodsUpdate extends Update {
    private static final long serialVersionUID = -3321537110343022582L;
    private final List<String> selectableGods;
    private final Map<String, String> selectedGods; // Nickname : GodName

    public GodsUpdate(List<String> selectableGods, Map<String, String> selectedGods) {
        super( null);
        this.selectableGods = selectableGods;
        this.selectedGods = selectedGods;
    }

    public List<String> getSelectableGods() {
        return this.selectableGods;
    }

    public Map<String, String> getSelectedGods() {
        return this.selectedGods;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
