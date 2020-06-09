package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.server.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class PongSender implements Runnable {

    private ObjectOutputStream objectOutputStream;

    PongSender(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }

    @Override
    public void run() {
        try {
            this.objectOutputStream.writeObject(Server.PONG_MSG);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
