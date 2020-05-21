package it.polimi.ingsw.network.client.controller;

import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.model.updates.ErrorUpdate;
import it.polimi.ingsw.model.updates.TurnUpdate;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.utils.NetworkUtils;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

import java.io.IOException;
import java.util.Map;

public class Controller extends Observable<Update> implements Observer<Object> {

    private final Client client;
    private String clientPlayerID;
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

    @Override
    public void update(Object message) { // todo Visitor?

        if (message instanceof TurnUpdate) {
            this.currentPlayerID = ((TurnUpdate) message).getCurrentPlayerID();
            this.currentPlayerNickname = ((TurnUpdate) message).currentPlayerNickname;
        } else {

            if (!(message instanceof Command)) {
                return; // todo Or throw?
            }

            if (!clientPlayerID.equals(currentPlayerID)) {
                ErrorUpdate err = new ErrorUpdate(clientPlayerID, ((Command) message).commandType);
                notify(err);
            } else {

                try {
                    if (message instanceof PlayerCommand) {
                        PlayerCommand playerCommand = (PlayerCommand) message;
                        playerCommand.setPlayerID(clientPlayerID);
                        client.sendCommand(playerCommand);

                    } else if (message instanceof GodChoiceCommand) {
                        GodChoiceCommand godChoiceCommand = (GodChoiceCommand) message;
                        godChoiceCommand.setPlayerID(clientPlayerID);
                        client.sendCommand(godChoiceCommand);

                    } else if (message instanceof GamePreparationCommand) {
                        GamePreparationCommand gamePreparationCommand = (GamePreparationCommand) message;
                        gamePreparationCommand.setPlayerID(clientPlayerID);
                        client.sendCommand(gamePreparationCommand);

                    } else if (message instanceof InitialInfoCommand) {

                        InitialInfoCommand initialInfoCommand = (InitialInfoCommand) message;
                        initialInfoCommand.setPlayerID(clientPlayerID);
                        client.sendCommand(initialInfoCommand);
                    }
                } catch (IOException ignored) {
                } // todo handle ignored exceptions
            }
        }
    }
}
