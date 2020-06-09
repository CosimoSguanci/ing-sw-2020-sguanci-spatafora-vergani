package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.updates.ServerUnreachableUpdate;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.network.ObjectListener;
import it.polimi.ingsw.network.ObjectListenerDelegate;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UpdateListener extends Observable<Update> implements ObjectListener, Runnable {
    private final ObjectListenerDelegate objectListenerDelegate;

    private final ObjectOutputStream objectOutputStream;

    private final Socket socket;
    private boolean isActive;

    public boolean isActive() {
        return this.isActive;
    }

    public void setIsActive(boolean active) {
        this.isActive = active;
    }

    public UpdateListener(Socket socket, ObjectOutputStream objectOutputStream) {
        this.socket = socket;
        this.objectOutputStream = objectOutputStream;
        objectListenerDelegate = new ObjectListenerDelegate(this.socket);
    }

    @Override
    public void run() {
        this.isActive = true;
        objectListenerDelegate.listen(this);
    }

    @Override
    public void forwardNotify(Object update) {
        /*if(update instanceof String && update.equals(Server.PING_MSG)) {
            try {
                objectOutputStream.writeObject(Server.PONG_MSG);
            } catch (IOException e) {
                handleConnectionReset();
            }
        }
        else {
            if(update instanceof Update) {
                notify((Update) update);
            }
        }*/
        if(update instanceof Update) {
            notify((Update) update);
        }
    }

    @Override
    public void handleConnectionTimeoutExpired() throws IOException {
        if(!isActive()) {
            socket.close();
        }
    }

    @Override
    public void handleConnectionReset() {

        ServerUnreachableUpdate serverUnreachableUpdate = new ServerUnreachableUpdate();
        notify(serverUnreachableUpdate); // notify the view

    }
}

