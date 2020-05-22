package it.polimi.ingsw.view;

import it.polimi.ingsw.model.updates.*;

public interface UpdateHandler { // Implements Visitor Pattern
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
