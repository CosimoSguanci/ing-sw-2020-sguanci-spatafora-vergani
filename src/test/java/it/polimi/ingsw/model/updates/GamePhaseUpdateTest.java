package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.view.UpdateHandler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GamePhaseUpdateTest {

    @Test
    public void handleUpdateTest() {
        UpdateHandler updateHandler = UpdateTest.getMockUpdateHandler();
        GamePhaseUpdate gamePhaseUpdate = new GamePhaseUpdate(GamePhase.INITIAL_INFO);
        gamePhaseUpdate.handleUpdate(updateHandler);

        verify(updateHandler, times(1)).handle(gamePhaseUpdate);
    }
}
