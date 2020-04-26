package it.polimi.ingsw.view;

import it.polimi.ingsw.model.updates.ChooseGodsUpdate;
import it.polimi.ingsw.model.updates.ErrorUpdate;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.updates.GamePreparationUpdate;
import it.polimi.ingsw.model.updates.PlayerUpdate;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.network.server.ClientHandler;


public class RemoteView extends View {

    private class MessageReceiver implements Observer<Object> {

        @Override
        public void update(Object command) {
           handleCommand(command);
        }

    }

    private ClientHandler clientHandler;

    public RemoteView(Player player, ClientHandler c) {
        super(player);
        this.clientHandler = c;
        c.addObserver(new MessageReceiver());
     //   c.asyncSend("Your opponent is: " + opponent);

    }

    @Override
    protected void showMessage(Object message) {
      //  clientConnection.asyncSend(message);
    }

    @Override
    public void update(Object message)
    {
        try {

            if(message instanceof ErrorUpdate) {
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
                clientHandler.sendObject(message);
        } catch(Exception e) {
            e.printStackTrace();
        }

       /* showMessage(message.getBoard());
        String resultMsg = "";
        boolean gameOver = message.getBoard().isGameOver(message.getPlayer().getMarker());
        boolean draw = message.getBoard().isFull();
        if (gameOver) {
            if (message.getPlayer() == getPlayer()) {
                resultMsg = gameMessage.winMessage + "\n";
            } else {
                resultMsg = gameMessage.loseMessage + "\n";
            }
        }
        else {
            if (draw) {
                resultMsg = gameMessage.drawMessage + "\n";
            }
        }
        if(message.getPlayer() == getPlayer()){
            resultMsg += gameMessage.waitMessage;
        }
        else{
            resultMsg += gameMessage.moveMessage;
        }

        showMessage(resultMsg);*/
    }

}
