package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.model.ErrorType;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GodsUpdateTest {
    @Test
    public void getSelectedSelectableGodsTest() {
        List<String> selectable = new ArrayList<>();
        Map<String, String> selected = new HashMap<>();

        selectable.add("god1");
        selectable.add("god2");
        selectable.add("god3");

        selected.put("nick1", "god1");

        GodsUpdate godsUpdate = new GodsUpdate(selectable, selected);

        assertEquals(selectable, godsUpdate.getSelectableGods());
        assertEquals(selected, godsUpdate.getSelectedGods());
    }

    @Test
    public void handleUpdateTest() {
        UpdateHandler updateHandler = UpdateTest.getMockUpdateHandler();
        GodsUpdate godsUpdate = new GodsUpdate(new ArrayList<>(), new HashMap<>());
        godsUpdate.handleUpdate(updateHandler);

        verify(updateHandler, times(1)).handle(godsUpdate);
    }
}
