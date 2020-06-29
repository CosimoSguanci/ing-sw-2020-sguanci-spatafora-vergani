package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.network.ObjectListener;
import it.polimi.ingsw.network.ObjectListenerDelegate;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.net.Socket;

/**
 * This class implements the capability of listen asynchronously the client-server stream to catch {@link Command} objects sent from client to server.
 * Composition-over-inheritance principle is applied to let this class extend Observable (the {@link ClientHandler} must
 * observe an instance of this class in order to catch commands and to notify the {@link it.polimi.ingsw.controller.Controller} about new commands sent from client to server),
 * but also share code with {@link it.polimi.ingsw.network.client.UpdateListener}.
 *
 * @author Cosimo Sguanci
 * @see ObjectListenerDelegate
 */
public class CommandListener extends Observable<Command> implements ObjectListener, Runnable {
    /**
     * The delegate that actually reads the Socket to catch the objects sent from the server
     */
    private final ObjectListenerDelegate objectListenerDelegate;
    private final Socket socket;
    private final Server server; // needed to handle clients disconnection
    private boolean isActive;

    /**
     * This method is useful to check if the communication between client and server is still in progress.
     *
     * @return true if the socket is still open, false otherwise.
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * Active flag setter
     *
     * @param active the new value of the active flag
     */
    public void setIsActive(boolean active) {
        this.isActive = active;
    }

    /**
     * CommandListener constructor: a new instance of {@link ObjectListenerDelegate} is created in order
     * to actually listen to the stream.
     *
     * @param socket the channel between client and server.
     * @param server server instance, needed to handle clients disconnections.
     */
    public CommandListener(Socket socket, Server server) {
        objectListenerDelegate = new ObjectListenerDelegate(socket);
        this.socket = socket;
        this.server = server;
    }

    /**
     * Classic overridden version of run method (from {@link Runnable} interface). Note that
     * the channel is listen on a separate thread (the new Thread is every time a new client connects
     * to the server).
     */
    @Override
    public void run() {
        this.isActive = true;
        objectListenerDelegate.listen(this);
    }

    /**
     * Forwards {@link Command} objects to the {@link ClientHandler} instance if a command is received.
     * Otherwise, it checks if the object received is a ping message (health checks sent from clients), and remove
     * the client from the list of client which haven't sent a ping message in the last timeout period yet.
     *
     * @param command the object received
     * @see Server#removeFromPingWaitingList(Socket)
     */
    @Override
    public void forwardNotify(Object command) {
        if (command instanceof String && command.equals(Server.PING_MSG)) {
            server.removeFromPingWaitingList(socket);
        } else {
            if (command instanceof Command) {
                notify((Command) command);
            }
        }
    }

    /**
     * Callback called when the listen timeout expires for a client. If the server was waiting for a ping message (and it hasn't received one),
     * the socket is closed and the Player is "marked" as disconnected, otherwise it is added to the waiting list to check if it's still alive.
     *
     * @throws IOException if socket.close() encounters some unexpected error.
     * @see Server#addToPingWaitingList(Socket)
     */
    @Override
    public void handleConnectionTimeoutExpired() throws IOException {
        if (server.waitingPingFrom(socket)) {
            socket.close();
        } else {
            server.addToPingWaitingList(socket);
        }
    }

    /**
     * Callback called when a client disconnects (read exception or socket.close done from handleConnectionTimeoutExpired).
     * It delegates connection reset handling to {@link Server}.
     *
     * @see Server#handleConnectionReset(Socket)
     */
    @Override
    public void handleConnectionReset() {
        server.handleConnectionReset(socket);
    }
}
