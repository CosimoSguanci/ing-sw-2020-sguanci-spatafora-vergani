package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WinUpdateTest {
    @Test
    public void getWinnerPlayerTest() {
        Match match = new Match(2);
        Model model = new Model(match);
        Player player = new Player("id", model, match);

        WinUpdate winUpdate = new WinUpdate(player);

        assertEquals(player, winUpdate.getWinnerPlayer());
    }

    @Test
    public void handleUpdateTest() {
        UpdateHandler updateHandler = UpdateTest.getMockUpdateHandler();
        Match match = new Match(2);
        Model model = new Model(match);
        Player player = new Player("id", model, match);

        WinUpdate winUpdate = new WinUpdate(player);
        winUpdate.handleUpdate(updateHandler);

        verify(updateHandler, times(1)).handle(winUpdate);
    }
}
