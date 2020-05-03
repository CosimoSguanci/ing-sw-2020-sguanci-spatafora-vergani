package it.polimi.ingsw.network;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

public class ObjectListenerDelegate {
    private final Socket socket;

    public ObjectListenerDelegate(Socket socket) {
        this.socket = socket;
    }

    public void listen(ObjectListener objectListener) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (objectListener.isActive()) { // todo use Client.isActive() and Server.isActive() ?
                Object inputObject = objectInputStream.readObject();
                objectListener.forwardNotify(inputObject);
            }
        } catch (Exception e){ // TODO catch SocketException?
            objectListener.setIsActive(false);
            objectListener.handleConnectionReset();
        }
    }
}
