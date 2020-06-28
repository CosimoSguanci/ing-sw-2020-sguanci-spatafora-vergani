package it.polimi.ingsw.network;

import java.io.IOException;

/**
 * Interface which is implemented by any component which needs to listen from Objects from a remote endpoint (on a stream).
 * Specifically, this interface is implemented by {@link it.polimi.ingsw.network.client.UpdateListener} and {@link it.polimi.ingsw.network.server.CommandListener},
 * which are used both on client and server.
 *
 * @author Cosimo Sguanci
 */
public interface ObjectListener {
    void setIsActive(boolean active);

    boolean isActive();

    void forwardNotify(Object object);

    void handleConnectionTimeoutExpired() throws IOException;

    void handleConnectionReset();
}
