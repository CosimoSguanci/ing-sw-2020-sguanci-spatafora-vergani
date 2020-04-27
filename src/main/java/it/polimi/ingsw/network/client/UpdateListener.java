package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.network.ObjectListener;
import it.polimi.ingsw.network.ObjectListenerDelegate;
import it.polimi.ingsw.observer.Observable;

import java.net.Socket;

public class UpdateListener extends Observable<Update> implements ObjectListener, Runnable {
    private ObjectListenerDelegate objectListenerDelegate;

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
        try {
            notify((Update) update);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

