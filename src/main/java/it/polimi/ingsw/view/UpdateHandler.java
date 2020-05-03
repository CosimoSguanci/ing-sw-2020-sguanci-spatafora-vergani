package it.polimi.ingsw.view;

import it.polimi.ingsw.model.updates.*;

public interface UpdateHandler { // Implements Visitor Pattern
    void handle(GamePhaseChangedUpdate update);
    void handle(InitialInfoUpdate update);
    void handle(SelectedInitialInfoUpdate update);
    void handle(MatchStartedUpdate update);
    void handle(ChooseGodsUpdate update);
    void handle(SelectedGodsUpdate update);
    void handle(GamePreparationUpdate update);
    void handle(BoardUpdate update);
    void handle(ErrorUpdate update);
    void handle(PlayerUpdate update);
    void handle(TurnUpdate update);
    void handle(WinUpdate update);
    void handle(LoseUpdate update);
    void handle(ServerUnreachableUpdate update);
    void handle(DisconnectedPlayerUpdate update);
}
