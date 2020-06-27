package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.view.UpdateHandler;

import java.util.Map;

/**
 * This class represents an update sent from Server to Client every time new initial info have been chosen (so the game state changed)
 * (example: a Player chose its nickname and color)
 *
 * @author Cosimo Sguanci
 */
public class InitialInfoUpdate extends Update {

    /**
     * Association between players nicknames and colors (Nickname : Color)
     */
    private final Map<String, PrintableColor> initialInfo;

    public InitialInfoUpdate(Map<String, PrintableColor> initialInfo) {
        super(null);
        this.initialInfo = initialInfo;
    }

    /**
     * Current Initial Info Getter
     *
     * @return a map containing the current nickname - color association
     */
    public Map<String, PrintableColor> getInitialInfo() {
        return this.initialInfo;
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

