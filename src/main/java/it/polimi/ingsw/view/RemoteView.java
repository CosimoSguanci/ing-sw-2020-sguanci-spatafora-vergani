package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.PlayerCommand;
import it.polimi.ingsw.model.messages.ErrorUpdate;
import it.polimi.ingsw.model.messages.ModelUpdate;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.ClientHandler;


public class RemoteView extends View {

    private class MessageReceiver implements Observer<PlayerCommand> {

        @Override
        public void update(PlayerCommand playerCommand) {
           /* System.out.println("Received: " + message);
            try{
                String[] inputs = message.split(",");
                handleMove(Integer.parseInt(inputs[0]), Integer.parseInt(inputs[1]));
            }catch(IllegalArgumentException e){
                clientHandler.asyncSend("Error!");
            }*/

           handleMove(playerCommand);
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

                if (getPlayer().equals(errorUpdate.getPlayer())) {
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
