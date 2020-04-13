package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.controller.PlayerCommand;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messages.PlayerUpdate;
import it.polimi.ingsw.model.messages.TurnUpdate;
import it.polimi.ingsw.observer.Observer;

public class Controller implements Observer<Object> {

    private Player clientPlayer;
    private TurnUpdate turnUpdate;
    private Client client;

    public Controller(Client client) {
        this.client = client;
    }

    private void handleCommand(PlayerCommand playerCommand) throws Exception {
        Player currentPlayer = turnUpdate.getCurrentPlayer();
        if (!clientPlayer.equals(currentPlayer)) {
            throw new Exception();
        } else {
            client.sendPlayerCommand(playerCommand);
        }
    }

    @Override
    public void update(Object message) {
        if (message instanceof PlayerUpdate) {
            this.clientPlayer = ((PlayerUpdate) message).getPlayer();
        } else if (message instanceof TurnUpdate) {
            this.turnUpdate = ((TurnUpdate) message);
        } else if (message instanceof PlayerCommand) {

            try {
                handleCommand((PlayerCommand) message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
