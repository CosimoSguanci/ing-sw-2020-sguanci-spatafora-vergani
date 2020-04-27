package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.network.ObjectListener;
import it.polimi.ingsw.network.ObjectListenerDelegate;
import it.polimi.ingsw.observer.Observable;

import java.net.Socket;

public class CommandListener extends Observable<Command> implements ObjectListener, Runnable {
    private ObjectListenerDelegate objectListenerDelegate;

    private boolean isActive;

    public boolean isActive() {
        return this.isActive;
    }

    public void setIsActive(boolean active) {
        this.isActive = active;
    }

    public CommandListener(Socket socket) {
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
            notify((Command) update);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
