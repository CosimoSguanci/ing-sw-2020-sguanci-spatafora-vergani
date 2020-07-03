package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.updates.ServerUnreachableUpdate;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.network.client.UpdateListener;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ObserverTest implements Observer<Update> {
    @Test
    public void observerTest() {
        ObserverTest observerTest = Mockito.spy(new ObserverTest());
        UpdateListener updateListener = new UpdateListener(null);
        updateListener.addObserver(observerTest);
        ServerUnreachableUpdate serverUnreachableUpdate = new ServerUnreachableUpdate();
        updateListener.notify(serverUnreachableUpdate);
        verify(observerTest, times(1)).update(serverUnreachableUpdate);
    }

    @Override
    public void update(Update message) {
        System.out.println("Update method was called!");
    }
}
