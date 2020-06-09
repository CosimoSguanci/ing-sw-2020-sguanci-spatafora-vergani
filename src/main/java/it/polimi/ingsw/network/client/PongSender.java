package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.server.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ScheduledExecutorService;

public class PongSender implements Runnable {

    private ObjectOutputStream objectOutputStream;
    private ScheduledExecutorService scheduler;

    PongSender(ObjectOutputStream objectOutputStream, ScheduledExecutorService scheduler) {
        this.objectOutputStream = objectOutputStream;
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        try {
            this.objectOutputStream.writeObject(Server.PONG_MSG);
        } catch(IOException e) {
            scheduler.shutdown();
            e.printStackTrace();
        }
    }
}
