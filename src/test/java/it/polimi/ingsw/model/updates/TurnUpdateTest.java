package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TurnUpdateTest {
    @Test
    public void getCurrentPlayerTest() {
        Match match = new Match(2);
        Model model = new Model(match);
        Player player = new Player("id", model, match);

        TurnUpdate turnUpdate = new TurnUpdate(player);

        assertEquals(player, turnUpdate.getCurrentPlayer());
    }

    @Test
    public void handleUpdateTest() {
        UpdateHandler updateHandler = UpdateTest.getMockUpdateHandler();
        Match match = new Match(2);
        Model model = new Model(match);
        Player player = new Player("id", model, match);

        TurnUpdate turnUpdate = new TurnUpdate(player);
        turnUpdate.handleUpdate(updateHandler);

        verify(updateHandler, times(1)).handle(turnUpdate);
    }
}
