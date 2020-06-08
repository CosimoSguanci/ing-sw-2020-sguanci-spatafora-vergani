package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.view.UpdateHandler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class InitialInfoUpdateTest {
    @Test
    public void getInitialInfoTest() {
        Map<String, PrintableColor> info = new HashMap<>();
        info.put("n1", PrintableColor.RED);
        info.put("n2", PrintableColor.GREEN);
        info.put("n3", PrintableColor.BLUE);

        InitialInfoUpdate initialInfoUpdate = new InitialInfoUpdate(info);

        assertEquals(info, initialInfoUpdate.getInitialInfo());
    }

    @Test
    public void handleUpdateTest() {
        UpdateHandler updateHandler = UpdateTest.getMockUpdateHandler();
        InitialInfoUpdate initialInfoUpdate = new InitialInfoUpdate(new HashMap<>());
        initialInfoUpdate.handleUpdate(updateHandler);

        verify(updateHandler, times(1)).handle(initialInfoUpdate);
    }
}
