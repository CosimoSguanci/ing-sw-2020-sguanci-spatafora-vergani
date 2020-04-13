package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.model.messages.ErrorUpdate;
import it.polimi.ingsw.model.messages.ModelUpdate;
import it.polimi.ingsw.model.messages.TurnUpdate;
import it.polimi.ingsw.observer.Observable;

public class Model extends Observable<Object> {
    private Match match;

    public Model(Match match) {
        this.match = match;
    }


    /**
     * The method returns current player of the match.
     *
     * @return   current player
     *
     */
    public Player getCurrentPlayer() {
        return match.getCurrentPlayer();
    }


    /**
     * The method calls for the end of turn of current player.
     *
     */
    public void endTurn() {
        match.nextTurn();
        TurnUpdate turnUpdate = new TurnUpdate(match.getCurrentPlayer());
        notify(turnUpdate);
    }

    public void reportError(Player player, CommandType commandType) {
        ErrorUpdate errorUpdate = new ErrorUpdate(player, commandType);
        notify(errorUpdate);
    }
}
