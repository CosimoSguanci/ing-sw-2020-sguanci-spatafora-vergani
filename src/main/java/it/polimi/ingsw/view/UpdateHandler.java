package it.polimi.ingsw.view;

import it.polimi.ingsw.model.updates.*;

/**
 * This interface contains the signature to force the concrete UpdateHandler to implement the methods to handle
 * all possible updates that can be received by the {@link View} from the Server, by using a Visitor Pattern.
 * This interface represent in practice the "abstract Visitor", while {@link it.polimi.ingsw.view.cli.CliUpdateHandler} and
 * {@link it.polimi.ingsw.view.gui.GuiUpdateHandler} are the "concrete Visitors".
 */
public interface UpdateHandler {
    void handle(GamePhaseUpdate update);
    void handle(InitialInfoUpdate update);
    void handle(MatchStartedUpdate update);
    void handle(GodsUpdate update);
    void handle(BoardUpdate update);
    void handle(ErrorUpdate update);
    void handle(TurnUpdate update);
    void handle(WinUpdate update);
    void handle(LoseUpdate update);
    void handle(ServerUnreachableUpdate update);
    void handle(DisconnectedPlayerUpdate update);
}
