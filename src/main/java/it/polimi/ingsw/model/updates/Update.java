package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

import java.io.Serializable;

/**
 * This abstract class represents a message exchanged between Client and Server.
 * Specifically, unlike {@link it.polimi.ingsw.controller.commands.Command} are sent from Server to Client.
 * They encapsulate any changes made to the Game State.
 *
 * @author Cosimo Sguanci
 */
public abstract class Update implements Serializable {
    private static final long serialVersionUID = 8108733601179952266L;

    /**
     * JSON Serialized Board representation
     */
    private final String board;

    public Update(String board) {
        this.board = board;
    }

    /**
     * Serialized Board getter
     *
     * @return JSON String Serialized version of the Match Board
     */
    public String getBoard() {
        return this.board;
    }

    /**
     * Utility method used to implement Visitor Pattern for Updates handling.
     *
     * @param handler which handle the updates to update the View
     * @see UpdateHandler
     */
    public abstract void handleUpdate(UpdateHandler handler);
}
