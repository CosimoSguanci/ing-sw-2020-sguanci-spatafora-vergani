package it.polimi.ingsw.network;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ObjectListenerDelegate {
    private Socket socket;

    public ObjectListenerDelegate(Socket socket) {
        this.socket = socket;
    }

    public void listen(ObjectListener objectListener) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (true) { // isActive
                Object inputObject = objectInputStream.readObject();
                objectListener.forwardNotify(inputObject); // input Object could be an Update or a Command
            }
        } catch (Exception e){
            //setActive(false);
            e.printStackTrace();
        }
    }
}
