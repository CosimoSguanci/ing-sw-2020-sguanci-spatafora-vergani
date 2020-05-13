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

                  if(!objectListener.isActive()) {
                      socket.close();
                  }

                }

            }
        } catch (IOException | ClassNotFoundException e){
            objectListener.setIsActive(false);
            objectListener.handleConnectionReset();
        }
    }
}
