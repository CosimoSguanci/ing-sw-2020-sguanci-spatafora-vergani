package it.polimi.ingsw.network.client.controller;

import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.model.updates.ErrorUpdate;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.exceptions.WrongPlayerException;
import it.polimi.ingsw.model.updates.PlayerUpdate;
import it.polimi.ingsw.model.updates.TurnUpdate;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

import java.io.IOException;

public class Controller extends Observable<Update> implements Observer<Object> {

    private String clientPlayerID;
    private String currentPlayerID;
    private final Client client;

    public Controller(Client client) {
        this.client = client;
    }

    public String getClientPlayerID() { /// WIP
        return this.clientPlayerID;
    }

    @Override
    public void update(Object message) { // todo Visitor?
        if (message instanceof PlayerUpdate) {
            this.clientPlayerID = ((PlayerUpdate) message).playerID;
        } else if (message instanceof TurnUpdate) {
            this.currentPlayerID = ((TurnUpdate) message).currentPlayerID;
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
                        client.sendPlayerCommand(playerCommand);

                    } catch (IOException e) {
                        e.printStackTrace(); // todo Handle this
                    }
                } else if (message instanceof GodChoiceCommand) {
                    try {
                        GodChoiceCommand godChoiceCommand = (GodChoiceCommand) message;
                        godChoiceCommand.setPlayerID(clientPlayerID);
                        client.sendGodChoiceCommand(godChoiceCommand);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (message instanceof GamePreparationCommand) {
                    try {
                        GamePreparationCommand gamePreparationCommand = (GamePreparationCommand) message;
                        gamePreparationCommand.setPlayerID(clientPlayerID);
                        client.sendGamePreparationCommand(gamePreparationCommand);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (message instanceof InitialInfoCommand) {
                    try {
                        InitialInfoCommand initialInfoCommand = (InitialInfoCommand) message;
                        initialInfoCommand.setPlayerID(clientPlayerID);
                        client.sendInitialInfoCommand(initialInfoCommand);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }

}
