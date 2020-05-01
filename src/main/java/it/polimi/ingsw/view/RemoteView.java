package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.network.server.ClientHandler;


public class RemoteView extends View {

    private class MessageReceiver implements Observer<Command> {

        @Override
        public void update(Command command) {
           handleCommand(command);
        }

    }

    private final ClientHandler clientHandler;

    public RemoteView(Player player, ClientHandler clientHandler) {
        super(player);
        this.clientHandler = clientHandler;
        clientHandler.addObserver(new MessageReceiver());
    }


    @Override
    public void update(Update update)
    {
        try {

            if(update instanceof PlayerSpecificUpdate) {
                PlayerSpecificUpdate playerSpecificUpdate = (PlayerSpecificUpdate) update;

                if (getPlayer().ID.equals(playerSpecificUpdate.playerID)) {
                    clientHandler.sendObject(update);
                }
            }

            else if (update instanceof BroadcastUpdate) {
                clientHandler.sendObject(update);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
