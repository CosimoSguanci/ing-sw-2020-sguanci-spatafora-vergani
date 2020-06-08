package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;
import org.mockito.Mockito;

public class UpdateTest {
    public static UpdateHandler getMockUpdateHandler() {
        return Mockito.mock(UpdateHandler.class);
    }
}
