package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

public abstract class View extends Observable<Command> implements Observer<Update> {

    private Player player;

    protected View(Player player){
        this.player = player;
    }

    protected Player getPlayer(){
        return player;
    }

    void handleCommand(Command command) {
        notify(command);
    }

}