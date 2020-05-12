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

    public void gamePhaseChangedUpdate(GamePhase newGamePhase) {
        GamePhaseChangedUpdate gamePhaseChangedUpdate = new GamePhaseChangedUpdate(newGamePhase);
        notify(gamePhaseChangedUpdate);
    }

    public void initialInfoUpdate(Player player, List<String> selectedNicknames, List<PrintableColor> selectableColors) {
        InitialInfoUpdate initialInfoUpdate = new InitialInfoUpdate(player.ID, selectedNicknames, selectableColors);
        notify(initialInfoUpdate);
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

    public void selectedInitialInfoUpdate(Map<String, PrintableColor> initialInfo) {
        SelectedInitialInfoUpdate selectedInitialInfoUpdate = new SelectedInitialInfoUpdate(initialInfo);
        notify(selectedInitialInfoUpdate);
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

    public void boardUpdate(PlayerCommand executedCommand) {
        BoardUpdate boardUpdate = new BoardUpdate(match.getMatchBoard().toString());
        executedCommand.setPlayerNickname(getCurrentPlayer().getNickname());
        boardUpdate.setExecutedCommand(executedCommand);
        notify(boardUpdate);
    }

    public void matchStartedUpdate() {
        TurnUpdate turnUpdate = new TurnUpdate(match.getCurrentPlayer().ID, match.getCurrentPlayer().getNickname());
        notify(turnUpdate);
        MatchStartedUpdate matchStartedUpdate = new MatchStartedUpdate(match.getMatchBoard().toString());
        notify(matchStartedUpdate);
    }

    public void turnUpdate(Player currentPlayer) {
        TurnUpdate turnUpdate = new TurnUpdate(currentPlayer.ID, currentPlayer.getNickname());
        notify(turnUpdate);
    }

    public void winUpdate(Player winnerPlayer) {
        WinUpdate winUpdate = new WinUpdate(winnerPlayer.ID, winnerPlayer.getNickname());
        notify(winUpdate);
    }

    public void loseUpdate(Player loserPlayer) {
        boolean onePlayerRemaining = getPlayers().size() == 1;
        LoseUpdate loseUpdate = new LoseUpdate(loserPlayer.ID, loserPlayer.getNickname(), onePlayerRemaining, match.getMatchBoard().toString());
        notify(loseUpdate);
    }

    public void disconnectedPlayerUpdate(Player disconnectedPlayer) {
        boolean onePlayerRemaining = getPlayers().size() == 1;
        DisconnectedPlayerUpdate disconnectedPlayerUpdate = new DisconnectedPlayerUpdate(disconnectedPlayer.ID, disconnectedPlayer.getNickname(), onePlayerRemaining, match.getMatchBoard().toString());
        notify(disconnectedPlayerUpdate);
    }

    public GamePhase getCurrentGamePhase() {
        return match.getCurrentGamePhase();
    }

    public void nextGamePhase() {
        match.nextGamePhase();
    }

    public void onPlayerLose(Player loserPlayer) {
        loserPlayer.getWorkerFirst().getPosition().setWorker(null);
        loserPlayer.getWorkerSecond().getPosition().setWorker(null);

        removePlayer(loserPlayer);
        loseUpdate(loserPlayer);

        turnUpdate(getCurrentPlayer());

    }
}
