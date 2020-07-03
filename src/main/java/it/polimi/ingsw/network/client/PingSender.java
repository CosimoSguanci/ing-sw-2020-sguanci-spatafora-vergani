package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.server.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ScheduledExecutorService;


/**
 * This class implements a Ping-message sender form Client to Server. In particular, Client sends
 * this kind of messages continuously, to prove that it is currently online and always available on
 * socket connection: in this way, if a client disconnects the server will not receive Ping messages
 * anymore, so it will immediately notify all other clients connected to the match that it is
 * impossible to continue playing.
 *
 * @author Andrea Mario Vergani
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 */
public class PingSender implements Runnable {

    private final ObjectOutputStream objectOutputStream;
    private final ScheduledExecutorService scheduler;


    /**
     * The constructor creates a PingSender object; ping messages are scheduled to be sent on a
     * given stream (from client to server) at specific times.
     *
     * @param objectOutputStream stream where ping messages must be sent
     * @param scheduler          contains time and settings for ping message sending
     */
    PingSender(ObjectOutputStream objectOutputStream, ScheduledExecutorService scheduler) {
        this.objectOutputStream = objectOutputStream;
        this.scheduler = scheduler;
    }

    /**
     * This method performs, on a thread, sending of ping messages to server.
     */
    @Override
    public void run() {
        try {

            synchronized (this.objectOutputStream) {
                this.objectOutputStream.writeObject(Server.PING_MSG);
            }

        } catch (IOException e) {
            scheduler.shutdown();
        }
    }
}