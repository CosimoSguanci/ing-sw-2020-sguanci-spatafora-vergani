package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MatchStartedUpdateTest {

    @Test
    public void handleUpdateTest() {
        UpdateHandler updateHandler = UpdateTest.getMockUpdateHandler();
        MatchStartedUpdate matchStartedUpdate = new MatchStartedUpdate("board");
        matchStartedUpdate.handleUpdate(updateHandler);

        verify(updateHandler, times(1)).handle(matchStartedUpdate);
    }
}
