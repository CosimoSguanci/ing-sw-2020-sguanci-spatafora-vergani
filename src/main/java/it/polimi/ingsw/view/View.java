package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.controller.PlayerCommand;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelUpdate;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

public abstract class View extends Observable<PlayerCommand> implements Observer<ModelUpdate> {

    private Player player;

    protected View(Player player){
        this.player = player;
    }

    protected Player getPlayer(){
        return player;
    }

    protected abstract void showMessage(Object message);

    void handleMove(PlayerCommand playerCommand) {
        //System.out.println(row + " " + column);
        notify(playerCommand);
    }

    public void reportError(String message){
        showMessage(message);
    }

}