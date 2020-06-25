package it.polimi.ingsw.network.client.controller;

import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.model.ErrorType;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.updates.ErrorUpdate;
import it.polimi.ingsw.model.updates.TurnUpdate;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

import java.io.IOException;


/**
 * This class represents Client's Controller in MVC design pattern. In particular, this controller
 * has the task of doing some "internal" control of objects, in order to perform a pre-process of
 * commands that are going to be sent to server; the main control is verifying that client has the
 * "right" to send a command, so if it is its turn or not. Obviously, according to MVC design pattern,
 * "real" and complete Controller is server-side; however, this one performs some simple controls to
 * avoid sending clearly wrong commands through socket (so, its main task is to enlighten network
 * communication when possible).
 * Remember that all controls done here must be repeated server-side too.
 *
 * @author Cosimo Sguanci
 */
public class Controller extends Observable<Update> implements Observer<Object> {

    private final Client client;
    private String clientPlayerID;
    private Player clientPlayer;
    private String currentPlayerID;
    private String currentPlayerNickname;

    /**
     * The constructor creates a client-side Controller associated to the parameter client object.
     *
     * @param client client object associated to Controller
     */
    public Controller(Client client) {
        this.client = client;
    }

    /**
     * The method returns the unique client ID given by server to a player.
     *
     * @return client ID given by server
     */
    public String getClientPlayerID() { /// WIP
        return this.clientPlayerID;
    }

    /**
     * The method is the getter for the unique client ID given by server to a player.
     *
     * @param clientPlayerID client ID given by server
     */
    public void setClientPlayerID(String clientPlayerID) {
        this.clientPlayerID = clientPlayerID;
    }

    /**
     * The method is the getter for current player's nickname; current player is the player who
     * is playing his/her turn.
     *
     * @return current player's nickname
     */
    public String getCurrentPlayerNickname() {
        return this.currentPlayerNickname;
    }

    /**
     * The method is the getter for current player's unique ID; current player is the player who
     * is playing his/her turn.
     *
     * @return current player's unique ID (given by server)
     */
    public String getCurrentPlayerID() {
        return this.currentPlayerID;
    }

    /**
     * This method is the getter for Player-object representing the "human" player who is playing
     * from this client.
     *
     * @return Player-object of player associated with client
     */
    public Player getClientPlayer() {
        return this.clientPlayer;
    }

    /**
     * This method tells if player associated with this client is match god-chooser or not.
     *
     * @return true if client-player is god-chooser for the match; false if he/she is not god-chooser
     */
    public boolean isClientPlayerGodChooser() {
        return this.clientPlayer.isGodChooser();
    }


    /**
     * The method is the setter for player nickname.
     *
     * @param nickname nickname to set
     */
    public void setClientPlayerNickname(String nickname) {
        if(this.clientPlayer != null) {
            this.clientPlayer.setNickname(nickname);
        }
    }


    /**
     * This method is an overriding method of "update" in Observer interface.
     * Its task is to handle specific Commands, verifying that they are sent by current-player.
     * The method also updates current-player and all Strings related to it (nickname, ID) on the
     * basis of a specific Update received by Server (TurnUpdate).
     *
     * @param message object to be controlled client-side
     */
    @Override
    public void update(Object message) {

        if (message instanceof TurnUpdate) {
            Player currentPlayer = ((TurnUpdate) message).getCurrentPlayer();
            this.currentPlayerID = currentPlayer.getPlayerID();
            this.currentPlayerNickname = currentPlayer.getNickname();

            if(currentPlayerID.equals(clientPlayerID)) {
                this.clientPlayer = currentPlayer;
            }

        } else {

            if (!(message instanceof Command)) {
                return;
            }

            if (!clientPlayerID.equals(currentPlayerID)) {
                ErrorUpdate err = new ErrorUpdate(clientPlayer, ((Command) message).commandType, ErrorType.WRONG_TURN, null);
                notify(err);
            } else {
                Command command = (Command) message;
                command.setPlayerID(clientPlayerID);
                client.sendCommand(command);
            }
        }
    }
}
