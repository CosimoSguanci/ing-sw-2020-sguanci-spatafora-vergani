package it.polimi.ingsw.network;

import java.io.IOException;

public interface ObjectListener {
    void setIsActive(boolean active);
    boolean isActive();
    void forwardNotify(Object object);
    void handleConnectionTimeoutExpired() throws IOException;
    void handleConnectionReset();
}
