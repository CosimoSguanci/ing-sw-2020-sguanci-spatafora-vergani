package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.observer.Observable;

import java.util.List;
import java.util.Map;

// TODO Javadoc Model
public class Model extends Observable<Update> {
    private final Match match;

    public Model(Match match) {
        this.match = match;
    }


    /**
     * The method returns current player of the match.
     *
     * @return current player
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

    public void removePlayer(Player player) {
        match.removePlayer(player);
    }

    /**
     * The method calls for the end of turn of current player.
     */
    public void endTurn() {
        match.nextTurn();
        turnUpdate(match.getCurrentPlayer());
    }

    public void setInitialTurn(int initialTurn) {
        match.setInitialTurn(initialTurn);
    }

    public void gamePhaseUpdate(GamePhase newGamePhase) {
        GamePhaseUpdate gamePhaseUpdate = new GamePhaseUpdate(newGamePhase);
        notify(gamePhaseUpdate);
    }

    public void reportError(Player player, CommandType commandType, ErrorType errorType, Map<String, String> inhibitorGod ) {
        ErrorUpdate errorUpdate = new ErrorUpdate(player, commandType, errorType, inhibitorGod);
        notify(errorUpdate);
    }


    public void initialInfoUpdate(Map<String, PrintableColor> initialInfo) {
        InitialInfoUpdate initialInfoUpdate = new InitialInfoUpdate(initialInfo);
        notify(initialInfoUpdate);
    }

    public void godsUpdate(List<String> selectableGods, Map<String, String> selectedGods) {
        GodsUpdate godsUpdate = new GodsUpdate(selectableGods, selectedGods);
        notify(godsUpdate);
    }

    public void boardUpdate() {
        BoardUpdate boardUpdate = new BoardUpdate(match.getMatchBoard().toString());
        notify(boardUpdate);
    }

    public void boardUpdate(PlayerCommand executedCommand) {
        BoardUpdate boardUpdate = new BoardUpdate(match.getMatchBoard().toString());
        executedCommand.setPlayerNickname(executedCommand.getPlayer().getNickname());
        boardUpdate.setExecutedCommand(executedCommand);
        notify(boardUpdate);
    }

    public void matchStartedUpdate() {
        TurnUpdate turnUpdate = new TurnUpdate(match.getCurrentPlayer());
        notify(turnUpdate);
        MatchStartedUpdate matchStartedUpdate = new MatchStartedUpdate(match.getMatchBoard().toString());
        notify(matchStartedUpdate);
    }

    public void turnUpdate(Player currentPlayer) {
        TurnUpdate turnUpdate = new TurnUpdate(currentPlayer);
        notify(turnUpdate);
    }

    public void winUpdate(Player winnerPlayer) {
        WinUpdate winUpdate = new WinUpdate(winnerPlayer);
        notify(winUpdate);
    }

    public void loseUpdate(Player loserPlayer, LoseUpdate.LoseCause loseCause) {
        boolean onePlayerRemaining = getPlayers().size() == 1;
        LoseUpdate loseUpdate = new LoseUpdate(loserPlayer, loseCause, onePlayerRemaining, match.getMatchBoard().toString());
        notify(loseUpdate);
    }

    public void disconnectedPlayerUpdate(Player disconnectedPlayer) {
        DisconnectedPlayerUpdate disconnectedPlayerUpdate = new DisconnectedPlayerUpdate(disconnectedPlayer, match.getMatchBoard().toString());
        notify(disconnectedPlayerUpdate);
    }

    public GamePhase getCurrentGamePhase() {
        return match.getCurrentGamePhase();
    }

    public void nextGamePhase() {
        match.nextGamePhase();
    }
    
    public void onPlayerLose(Player loserPlayer, LoseUpdate.LoseCause loseCause) {
        loserPlayer.getWorkerFirst().getPosition().setWorker(null);
        loserPlayer.getWorkerSecond().getPosition().setWorker(null);
        removePlayer(loserPlayer);
        loseUpdate(loserPlayer, loseCause);
        turnUpdate(getCurrentPlayer());
    }

    public int getPlayersNumber() {
        return match.getPlayersNumber();
    }
}
