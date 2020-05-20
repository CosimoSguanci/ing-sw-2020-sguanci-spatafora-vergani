package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.view.UpdateHandler;

import java.util.List;

public class InitialInfoUpdate extends Update {

    private static final long serialVersionUID = 7676143030084758028L;

    public final List<String> selectedNicknames;
    public final List<PrintableColor> selectableColors;

    public InitialInfoUpdate(String playerID, List<String> selectedNicknames, List<PrintableColor> selectableColors) {
        super(playerID, null);
        this.selectedNicknames = selectedNicknames;
        this.selectableColors = selectableColors;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }

}

