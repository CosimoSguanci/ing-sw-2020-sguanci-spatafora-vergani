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

    private ClientHandler clientHandler;

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

            /*if(message instanceof ErrorUpdate) {
                ErrorUpdate errorUpdate = (ErrorUpdate) message;

                if (getPlayer().ID.equals(errorUpdate.playerID)) {
                    clientHandler.sendObject(message);
                }
            }
            else if(message instanceof PlayerUpdate) {
                PlayerUpdate playerUpdate = (PlayerUpdate) message;

                if (getPlayer().ID.equals(playerUpdate.playerID)) {
                    clientHandler.sendObject(message);
                }
            }
            else if(message instanceof ChooseGodsUpdate) {
                ChooseGodsUpdate chooseGodsUpdate = (ChooseGodsUpdate) message;

                if (getPlayer().ID.equals(chooseGodsUpdate.playerID)) {
                    clientHandler.sendObject(message);
                }
            }
            else if(message instanceof GamePreparationUpdate) {
                GamePreparationUpdate gamePreparationUpdate = (GamePreparationUpdate) message;

                if (getPlayer().ID.equals(gamePreparationUpdate.playerID)) {
                    clientHandler.sendObject(message);
                }
            }
            else
                clientHandler.sendObject(message);*/
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
