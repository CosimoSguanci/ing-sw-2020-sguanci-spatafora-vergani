package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.PrintableColour;
import it.polimi.ingsw.view.UpdateHandler;

import java.util.Map;

public class SelectedInitialInfoUpdate extends BroadcastUpdate {
    public final Map<String, PrintableColour> initialInfo; // Nickname : Color

    public SelectedInitialInfoUpdate(Map<String, PrintableColour> initialInfo) {
        super(null);
        this.initialInfo = initialInfo;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}

