package it.polimi.ingsw.model.messages;

import java.io.Serializable;
import java.util.Map;

public class SelectedGodsUpdate implements Serializable {
    private Map<String, String> selectedGods; // Nickname: GodName

    public SelectedGodsUpdate(Map<String, String> selectedGods) {
        this.selectedGods = selectedGods;
    }

    public Map<String, String> getSelectedGods() {
        return this.selectedGods;
    }
}
