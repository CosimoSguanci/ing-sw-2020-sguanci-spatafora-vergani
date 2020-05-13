package it.polimi.ingsw.network.client.controller;

import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.model.updates.ErrorUpdate;
import it.polimi.ingsw.model.updates.PlayerUpdate;
import it.polimi.ingsw.model.updates.TurnUpdate;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

import java.io.IOException;

public class Controller extends Observable<Update> implements Observer<Object> {

    private String clientPlayerID;
    private String currentPlayerID;
    private String currentPlayerNickname;
    private final Client client;

    public Controller(Client client) {
        this.client = client;
    }

    public String getClientPlayerID() { /// WIP
        return this.clientPlayerID;
    }

    public String getCurrentPlayerNickname() {
        return this.currentPlayerNickname;
    }

    public String getCurrentPlayerID() {
        return this.currentPlayerID;
    }

    @Override
    public void update(Object message) { // todo Visitor?
        if (message instanceof PlayerUpdate) {
            this.clientPlayerID = ((PlayerUpdate) message).playerID;
        } else if (message instanceof TurnUpdate) {
            this.currentPlayerID = ((TurnUpdate) message).currentPlayerID;
            this.currentPlayerNickname = ((TurnUpdate) message).currentPlayerNickname;
        } else {

            if (!(message instanceof Command)) {
                return; // Or throw?
            }

            if (!clientPlayerID.equals(currentPlayerID)) {
                ErrorUpdate err = new ErrorUpdate(clientPlayerID, ((Command) message).commandType);
                notify(err);
            }
            else {
                if (message instanceof PlayerCommand) {
                    try {
                        PlayerCommand playerCommand = (PlayerCommand) message;
                        playerCommand.setPlayerID(clientPlayerID);
                        client.sendCommand(playerCommand);

                    } catch (IOException e) {
                        e.printStackTrace(); // todo Handle this
                    }
                } else if (message instanceof GodChoiceCommand) {
                    try {
                        GodChoiceCommand godChoiceCommand = (GodChoiceCommand) message;
                        godChoiceCommand.setPlayerID(clientPlayerID);
                        client.sendCommand(godChoiceCommand);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (message instanceof GamePreparationCommand) {
                    try {
                        GamePreparationCommand gamePreparationCommand = (GamePreparationCommand) message;
                        gamePreparationCommand.setPlayerID(clientPlayerID);
                        client.sendCommand(gamePreparationCommand);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (message instanceof InitialInfoCommand) {
                    try {
                        InitialInfoCommand initialInfoCommand = (InitialInfoCommand) message;
                        initialInfoCommand.setPlayerID(clientPlayerID);
                        client.sendCommand(initialInfoCommand);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
