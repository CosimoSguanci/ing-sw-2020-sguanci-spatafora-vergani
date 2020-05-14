package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.updates.ServerUnreachableUpdate;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.network.ObjectListener;
import it.polimi.ingsw.network.ObjectListenerDelegate;
import it.polimi.ingsw.observer.Observable;

import java.net.Socket;

public class UpdateListener extends Observable<Update> implements ObjectListener, Runnable {
    private final ObjectListenerDelegate objectListenerDelegate;
    private boolean isActive;

    public boolean isActive() {
        return this.isActive;
    }

    public void setIsActive(boolean active) {
        this.isActive = active;
    }

    public UpdateListener(Socket socket) {
        objectListenerDelegate = new ObjectListenerDelegate(socket);
    }

    @Override
    public void run() {
        this.isActive = true;
        objectListenerDelegate.listen(this);
    }

    @Override
    public void forwardNotify(Object update) {
        notify((Update) update);
    }

    @Override
    public void handleConnectionReset() {

        ServerUnreachableUpdate serverUnreachableUpdate = new ServerUnreachableUpdate();
        notify(serverUnreachableUpdate); // notify the view

    }
}

