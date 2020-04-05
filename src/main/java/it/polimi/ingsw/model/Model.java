package it.polimi.ingsw.model;

import it.polimi.ingsw.observer.Observable;

public class Model extends Observable<ModelUpdate> {
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
    }
}
