package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ServerUnreachableUpdateTest {
    @Test
    public void handleUpdateTest() {
        UpdateHandler updateHandler = UpdateTest.getMockUpdateHandler();
        ServerUnreachableUpdate serverUnreachableUpdate = new ServerUnreachableUpdate();
        serverUnreachableUpdate.handleUpdate(updateHandler);

        verify(updateHandler, times(1)).handle(serverUnreachableUpdate);
    }
}
