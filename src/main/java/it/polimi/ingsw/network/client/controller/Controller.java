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

public class Controller extends Observable<Update> implements Observer<Object> {

    private final Client client;
    private String clientPlayerID;
    private Player clientPlayer;
    private String currentPlayerID;
    private String currentPlayerNickname;

    public Controller(Client client) {
        this.client = client;
    }

    public String getClientPlayerID() { /// WIP
        return this.clientPlayerID;
    }

    public void setClientPlayerID(String clientPlayerID) {
        this.clientPlayerID = clientPlayerID;
    }

    public String getCurrentPlayerNickname() {
        return this.currentPlayerNickname;
    }

    public String getCurrentPlayerID() {
        return this.currentPlayerID;
    }

    public boolean isClientPlayerGodChooser() {
        return this.clientPlayer.isGodChooser();
    }

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
                return; // todo Or throw?
            }

            if (!clientPlayerID.equals(currentPlayerID)) {
                ErrorUpdate err = new ErrorUpdate(clientPlayer, ((Command) message).commandType, ErrorType.WRONG_TURN, null);
                notify(err);
            } else {
                try {
                    Command command = (Command) message;
                    command.setPlayerID(clientPlayerID);
                    client.sendCommand(command);
                } catch (IOException ignored) {
                } // todo handle ignored exceptions
            }
        }
    }
}
