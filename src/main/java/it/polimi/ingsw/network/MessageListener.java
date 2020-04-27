package it.polimi.ingsw.network;

import it.polimi.ingsw.observer.Observable;

import java.io.ObjectInputStream;
import java.net.Socket;

public class MessageListener extends Observable<Object> implements Runnable {
    private Socket socket;

    public MessageListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (true) { // isActive
                Object inputObject = objectInputStream.readObject();
                notify(inputObject); // input Object could be an Update or a Command
            }
        } catch (Exception e){
            //setActive(false);
            e.printStackTrace();
        }
    }
}
