package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.network.ObjectListener;
import it.polimi.ingsw.network.ObjectListenerDelegate;
import it.polimi.ingsw.observer.Observable;

import java.net.Socket;

public class CommandListener extends Observable<Command> implements ObjectListener, Runnable {
    private ObjectListenerDelegate objectListenerDelegate;

    public CommandListener(Socket socket) {
        objectListenerDelegate = new ObjectListenerDelegate(socket);
    }

    @Override
    public void run() {
        objectListenerDelegate.listen(this);
    }

    @Override
    public void forwardNotify(Object update) {
        try {
            notify((Command) update);
        } catch (Exception e){
            //setActive(false);
            e.printStackTrace();
        }
    }
}
