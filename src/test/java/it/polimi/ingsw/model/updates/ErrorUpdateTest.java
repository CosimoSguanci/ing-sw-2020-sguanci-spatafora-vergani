package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.model.ErrorType;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ErrorUpdateTest {
    @Test
    public void getCurrentPlayerTest() {
        Match match = new Match(2);
        Model model = new Model(match);
        Player player = new Player("id", model, match);
        Map<String, String> inhibitor = new HashMap<>();
        inhibitor.put("name", "testName");
        inhibitor.put("description", "testDescription");
        inhibitor.put("power", "powerTest");

        ErrorUpdate errorUpdate = new ErrorUpdate(player, CommandType.MOVE, ErrorType.WRONG_GAME_PHASE, inhibitor);

        assertEquals(player, errorUpdate.getCurrentPlayer());
        assertEquals(inhibitor, errorUpdate.getInhibitorGod());
    }

    @Test
    public void handleUpdateTest() {
        UpdateHandler updateHandler = UpdateTest.getMockUpdateHandler();
        Match match = new Match(2);
        Model model = new Model(match);
        Player player = new Player("id", model, match);
        ErrorUpdate errorUpdate = new ErrorUpdate(player, CommandType.MOVE, ErrorType.WRONG_GAME_PHASE, null);
        errorUpdate.handleUpdate(updateHandler);

        verify(updateHandler, times(1)).handle(errorUpdate);
    }
}
