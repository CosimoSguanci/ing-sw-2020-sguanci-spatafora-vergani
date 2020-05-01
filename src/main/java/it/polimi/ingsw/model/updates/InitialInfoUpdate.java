package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.PrintableColour;
import it.polimi.ingsw.view.UpdateHandler;

import java.util.List;

public class InitialInfoUpdate extends PlayerSpecificUpdate {

    public final List<String> selectedNicknames;
    public final List<PrintableColour> selectableColors;

    public InitialInfoUpdate(String playerID, List<String> selectedNicknames, List<PrintableColour> selectableColors) {
        super(playerID);
        this.selectedNicknames = selectedNicknames;
        this.selectableColors = selectableColors;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }

}

