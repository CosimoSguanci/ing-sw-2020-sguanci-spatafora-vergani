package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LoseUpdateTest {

    @Test
    public void getLoserPlayerAndCauseTest() {
        Match match = new Match(2);
        Model model = new Model(match);
        Player player = new Player("id", model, match);
        LoseUpdate loseUpdate = new LoseUpdate(player, LoseUpdate.LoseCause.CANT_MOVE, false, "");
        assertEquals(player, loseUpdate.getLoserPlayer());
        assertSame(LoseUpdate.LoseCause.CANT_MOVE, loseUpdate.getLoseCause());
    }

    @Test
    public void handleUpdateTest() {
        UpdateHandler updateHandler = UpdateTest.getMockUpdateHandler();
        Match match = new Match(2);
        Model model = new Model(match);
        Player player = new Player("id", model, match);
        LoseUpdate loseUpdate = new LoseUpdate(player, LoseUpdate.LoseCause.CANT_MOVE, false, "");
        loseUpdate.handleUpdate(updateHandler);

        verify(updateHandler, times(1)).handle(loseUpdate);
    }

}
