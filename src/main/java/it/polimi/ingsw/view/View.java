package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.PlayerCommand;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

public abstract class View extends Observable<Object> implements Observer<Object> { // TODO Gerarchia Command

    private Player player;

    protected View(Player player){
        this.player = player;
    }

    protected Player getPlayer(){
        return player;
    }

    protected abstract void showMessage(Object message);

    void handleCommand(Object command) {
        notify(command);
    }

    public void reportError(String message){
        showMessage(message);
    }

}