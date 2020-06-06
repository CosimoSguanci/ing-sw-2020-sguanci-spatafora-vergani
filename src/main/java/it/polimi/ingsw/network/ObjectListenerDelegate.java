package it.polimi.ingsw.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ObjectListenerDelegate {
    private final Socket socket;

    public ObjectListenerDelegate(Socket socket) {
        this.socket = socket;
    }

    public void listen(ObjectListener objectListener) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (objectListener.isActive()) {

              try {
                    Object inputObject = objectInputStream.readObject();
                    objectListener.forwardNotify(inputObject);

                } catch(SocketTimeoutException e) {
                    objectListener.handleConnectionTimeoutExpired();
                }
            }
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();

            if(objectListener.isActive()) {
                objectListener.handleConnectionReset();
            }
            else {
                objectListener.setIsActive(false);
            }

        }
    }
}
