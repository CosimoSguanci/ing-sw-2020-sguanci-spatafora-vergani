package it.polimi.ingsw.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * This class is used as delegate (composition-over-inheritance) to share common code which would be
 * otherwise duplicated. Specifically, this class handles a socket object stream, calling {@link ObjectListener}
 * callbacks when necessary.
 *
 * @author Cosimo Sguanci
 */
public class ObjectListenerDelegate {
    private final Socket socket;

    public ObjectListenerDelegate(Socket socket) {
        this.socket = socket;
    }

    /**
     * This method is used to listen a stream of objects (for Santorini, the object received will be either an {@link it.polimi.ingsw.model.updates.Update},
     * a {@link it.polimi.ingsw.controller.commands.Command}, or a String for ping messages). {@link ObjectListener#forwardNotify(Object)}
     * is called every time a new Object has been received. Other callbacks are {@link ObjectListener#handleConnectionTimeoutExpired()} (which is called when the
     * SocketTimeout expires) and {@link ObjectListener#handleConnectionReset()} (called when there's an error reading from the Socket).
     *
     * @param objectListener the Object listener which delegates listening to this class.
     */
    public void listen(ObjectListener objectListener) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (objectListener.isActive()) {

                try {
                    Object inputObject = objectInputStream.readObject();
                    objectListener.forwardNotify(inputObject);

                } catch (SocketTimeoutException e) {
                    objectListener.handleConnectionTimeoutExpired();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | ClassNotFoundException e) {

            if (objectListener.isActive()) {
                objectListener.handleConnectionReset();
            } else {
                objectListener.setIsActive(false);
            }

        }
    }
}
