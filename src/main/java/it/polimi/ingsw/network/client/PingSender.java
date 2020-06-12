package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.server.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ScheduledExecutorService;

public class PingSender implements Runnable {

    private final ObjectOutputStream objectOutputStream;
    private final ScheduledExecutorService scheduler;

    PingSender(ObjectOutputStream objectOutputStream, ScheduledExecutorService scheduler) {
        this.objectOutputStream = objectOutputStream;
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        try {
            this.objectOutputStream.writeObject(Server.PING_MSG);
        } catch(IOException e) {
            scheduler.shutdown();
        }
    }
}
