package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

import java.util.List;
import java.util.Map;

/**
 * This class represents an update sent from Server to Client every time new Gods have been chosen (so the game state changed)
 *
 * @author Cosimo Sguanci
 */
public class GodsUpdate extends Update {

    /**
     * Gods which are still available to select, the God Chooser chose them but they've not been assigned yet
     */
    private final List<String> selectableGods;

    /**
     * Association between players and gods that have been selected (Nickname : godName)
     */
    private final Map<String, String> selectedGods;

    public GodsUpdate(List<String> selectableGods, Map<String, String> selectedGods) {
        super(null);
        this.selectableGods = selectableGods;
        this.selectedGods = selectedGods;
    }

    /**
     * SelectableGods getter
     *
     * @return the list of available Gods (String rep)
     */
    public List<String> getSelectableGods() {
        return this.selectableGods;
    }

    /**
     * SelectedGods getter
     *
     * @return the list of selected Gods (String rep)
     */
    public Map<String, String> getSelectedGods() {
        return this.selectedGods;
    }

    /**
     * Utility method used to implement Visitor Pattern for Updates handling.
     *
     * @param handler which handle the updates to update the View
     * @see UpdateHandler
     */
    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
