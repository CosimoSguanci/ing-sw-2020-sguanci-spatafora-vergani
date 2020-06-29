package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.updates.ServerUnreachableUpdate;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.network.ObjectListener;
import it.polimi.ingsw.network.ObjectListenerDelegate;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.net.Socket;

/**
 * This class implements the capability of listen asynchronously the client-server stream to catch {@link Update} objects sent from server to client.
 * Composition-over-inheritance principle is applied to let this class extend Observable (the {@link it.polimi.ingsw.view.View} must
 * observe an instance of this class in order to handle updates and notify the users about the game state), but also share code with {@link it.polimi.ingsw.network.server.CommandListener}.
 *
 * @author Cosimo Sguanci
 * @see ObjectListenerDelegate
 */
public class UpdateListener extends Observable<Update> implements ObjectListener, Runnable {
    /**
     * The delegate that actually reads the Socket to catch the objects sent from the server
     */
    private final ObjectListenerDelegate objectListenerDelegate;
    private final Socket socket;
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
     * UpdateListener constructor: a new instance of {@link ObjectListenerDelegate} is created in order
     * to actually listen to the stream.
     *
     * @param socket the channel between client and server.
     */
    public UpdateListener(Socket socket) {
        this.socket = socket;
        objectListenerDelegate = new ObjectListenerDelegate(this.socket);
    }

    /**
     * Classic overridden version of run method (from {@link Runnable} interface). Note that
     * the channel is listen on a separate thread (the new Thread is launched at application startup).
     */
    @Override
    public void run() {
        this.isActive = true;
        objectListenerDelegate.listen(this);
    }

    /**
     * Forwards notiy messages from {@link ObjectListenerDelegate} to the {@link it.polimi.ingsw.view.View}
     *
     * @param update the {@link Update} received from the server
     */
    @Override
    public void forwardNotify(Object update) {
        if (update instanceof Update) {
            notify((Update) update);
        }
    }

    /**
     * Callback called when the timeout for socket read is expired. If setIsActive(false) was called, close the relative socket.
     *
     * @throws IOException if socket.close() encounters an error.
     */
    @Override
    public void handleConnectionTimeoutExpired() throws IOException {
        if (!isActive()) {
            socket.close();
        }
    }

    /**
     * Callback called when the communication between client and server is interrupted. It notifies the {@link it.polimi.ingsw.view.View}, which shows
     * an error message to the user.
     */
    @Override
    public void handleConnectionReset() {
        ServerUnreachableUpdate serverUnreachableUpdate = new ServerUnreachableUpdate();
        notify(serverUnreachableUpdate); // notify the view
    }
}

