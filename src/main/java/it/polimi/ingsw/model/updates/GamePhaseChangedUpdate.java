package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.view.UpdateHandler;

public class GamePhaseChangedUpdate extends Update {
    public final GamePhase newGamePhase;

    public GamePhaseChangedUpdate(GamePhase newGamePhase) {
        super(null, null);
        this.newGamePhase = newGamePhase;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
