package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.view.UpdateHandler;

import java.util.Map;

public class InitialInfoUpdate extends Update {
    private final Map<String, PrintableColor> initialInfo; // Nickname : Color

    public InitialInfoUpdate(Map<String, PrintableColor> initialInfo) {
        super(null, null);
        this.initialInfo = initialInfo;
    }

    public Map<String, PrintableColor> getInitialInfo() {
        return this.initialInfo;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}

