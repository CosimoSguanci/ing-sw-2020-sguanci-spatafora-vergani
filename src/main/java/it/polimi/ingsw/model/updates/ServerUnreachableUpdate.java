package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

/**
 * This class represents an update sent generated locally by the client to notify the view that the Server is not reachable.
 * The possible causes for this are that the server is not online, or the internet connection isn't available on the client.
 *
 * @author Cosimo Sguanci
 */
public class ServerUnreachableUpdate extends Update {

    public ServerUnreachableUpdate() {
        super(null);
    }

    /**
     * Utility method used to implement Visitor Pattern for Updates handling.
     *
     * @param handler which handle the updates to update the View
     * @see UpdateHandler
     */
    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
