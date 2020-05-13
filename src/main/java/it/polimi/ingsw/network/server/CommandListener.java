package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.network.ObjectListener;
import it.polimi.ingsw.network.ObjectListenerDelegate;
import it.polimi.ingsw.observer.Observable;

import java.net.Socket;

public class CommandListener extends Observable<Command> implements ObjectListener, Runnable {
    private final ObjectListenerDelegate objectListenerDelegate;
    private final Socket socket;
    private final Server server; // needed to handle clients disconnection
    private boolean isActive;

    public boolean isActive() {
        return this.isActive;
    }

    public void setIsActive(boolean active) {
        this.isActive = active;
    }

    public CommandListener(Socket socket, Server server) {
        objectListenerDelegate = new ObjectListenerDelegate(socket);
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        this.isActive = true;
        objectListenerDelegate.listen(this);
    }

    @Override
    public void forwardNotify(Object update) {
        notify((Command) update);
    }

    @Override
    public void handleConnectionReset() {
        server.handleConnectionReset(socket);
    }
}
