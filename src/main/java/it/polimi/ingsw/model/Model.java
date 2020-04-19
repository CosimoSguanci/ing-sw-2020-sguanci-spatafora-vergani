package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.observer.Observable;

import java.util.List;

// TODO Javadoc Model
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


    public Board getBoard() {
        return match.getMatchBoard();
    }

    public List<Player> getPlayers() {
        return match.getPlayers();
    }


    /**
     * The method calls for the end of turn of current player.
     *
     */
    public void endTurn() {
        match.nextTurn();
        TurnUpdate turnUpdate = new TurnUpdate(match.getCurrentPlayer().ID);
        notify(turnUpdate);
    }

    public void reportError(Player player, CommandType commandType) {
        ErrorUpdate errorUpdate = new ErrorUpdate(player, commandType);
        notify(errorUpdate);
    }

    public void playerUpdate(Player player) {
        PlayerUpdate playerUpdate = new PlayerUpdate(player.ID);
        notify(playerUpdate);
    }

    public void startMatch() {
        MatchStartedUpdate matchStartedUpdate = new MatchStartedUpdate(match.getMatchBoard());
        notify(matchStartedUpdate);
    }
}
