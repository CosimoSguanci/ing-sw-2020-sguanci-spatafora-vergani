package it.polimi.ingsw.network.client.controller;

import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.exceptions.WrongPlayerException;
import it.polimi.ingsw.model.updates.PlayerUpdate;
import it.polimi.ingsw.model.updates.TurnUpdate;
import it.polimi.ingsw.observer.Observer;

import java.io.IOException;

public class Controller implements Observer<Object> {

    private String clientPlayerID;
    private TurnUpdate turnUpdate;
    private Client client;

    public Controller(Client client) {
        this.client = client;
    }

    private void handleCommand(PlayerCommand playerCommand) throws WrongPlayerException, IOException { // TODO remove Exception
        String currentPlayer = turnUpdate.playerID;
        if (clientPlayerID.equals(currentPlayer)) {
            playerCommand.setPlayerID(clientPlayerID);
            client.sendPlayerCommand(playerCommand);
        } else {
           throw new WrongPlayerException();
        }
    }

    @Override
    public void update(Object message) { // todo Visitor?
        if (message instanceof PlayerUpdate) {
            this.clientPlayerID = ((PlayerUpdate) message).playerID;
        } else if (message instanceof TurnUpdate) {
            this.turnUpdate = ((TurnUpdate) message);
        } else if (message instanceof PlayerCommand) {
            try {
                handleCommand((PlayerCommand) message);
            } catch (WrongPlayerException e) {
                e.printStackTrace(); // todo notify user
            } catch (IOException e) {
                e.printStackTrace();
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
