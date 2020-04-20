package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.controller.PlayerCommand;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messages.PlayerUpdate;
import it.polimi.ingsw.model.messages.TurnUpdate;
import it.polimi.ingsw.observer.Observer;

public class Controller implements Observer<Object> {

    private String clientPlayerID;
    private TurnUpdate turnUpdate;
    private Client client;

    public Controller(Client client) {
        this.client = client;
    }

    private void handleCommand(PlayerCommand playerCommand) throws Exception {
        String currentPlayer = turnUpdate.getCurrentPlayer();
        if (clientPlayerID.equals(currentPlayer)) {
            client.sendPlayerCommand(playerCommand);
        } else {
           throw new Exception();
        }
    }

    @Override
    public void update(Object message) {
        if (message instanceof PlayerUpdate) {
            this.clientPlayerID = ((PlayerUpdate) message).getPlayerID();

        } else if (message instanceof TurnUpdate) {
            this.turnUpdate = ((TurnUpdate) message);
        } else if (message instanceof PlayerCommand) {
            //PlayerCommand playerCommand = (PlayerCommand) message;
            //playerCommand.setPlayer(clientPlayer);
            try {
                handleCommand((PlayerCommand) message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
