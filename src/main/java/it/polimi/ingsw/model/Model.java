package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.observer.Observable;

import java.util.List;
import java.util.Map;

// TODO Javadoc Model
public class Model extends Observable<Update> {
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
        return this.match.getCurrentPlayer();
    }


    public Board getBoard() {
        return this.match.getMatchBoard();
    }

    public List<Player> getPlayers() {
        return match.getPlayers();
    }

    /**
     * The method calls for the end of turn of current player.
     *
     */
    public void endTurn() {
        nextTurn();
        TurnUpdate turnUpdate = new TurnUpdate(match.getCurrentPlayer().ID);
        notify(turnUpdate);
    }

    public void nextTurn() {
        match.nextTurn();
    }

    public void setInitialTurn(int initialTurn) {
        match.setInitialTurn(initialTurn);
    }

    public void reportError(Player player, CommandType commandType) {
        ErrorUpdate errorUpdate = new ErrorUpdate(player.ID, commandType);
        notify(errorUpdate);
    }

    public void playerUpdate(Player player) {
        PlayerUpdate playerUpdate = new PlayerUpdate(player.ID);
        notify(playerUpdate);
    }

    public void chooseGodsUpdate(Player player, List<String> selectableGods) {
        ChooseGodsUpdate chooseGodsUpdate = new ChooseGodsUpdate(player.ID, player.isGodChooser(), selectableGods);
        notify(chooseGodsUpdate);
    }

    public void selectedGodsUpdate(Map<String, String> selectedGods) {
        SelectedGodsUpdate selectedGodsUpdate = new SelectedGodsUpdate(selectedGods);
        notify(selectedGodsUpdate);
    }

    public void gamePreparationUpdate(Player player) {
        GamePreparationUpdate gamePreparationUpdate = new GamePreparationUpdate(player.ID, match.getMatchBoard().toString());
        notify(gamePreparationUpdate);
    }

    public void boardUpdate() {
        BoardUpdate boardUpdate = new BoardUpdate(match.getMatchBoard().toString());
        notify(boardUpdate);
    }

    public void matchStartedUpdate() {
        TurnUpdate turnUpdate = new TurnUpdate(match.getCurrentPlayer().ID);
        notify(turnUpdate);
        MatchStartedUpdate matchStartedUpdate = new MatchStartedUpdate(match.getMatchBoard().toString());
        notify(matchStartedUpdate);
    }
}
