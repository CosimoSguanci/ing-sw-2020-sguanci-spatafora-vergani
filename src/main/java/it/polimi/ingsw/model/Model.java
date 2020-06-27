package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.observer.Observable;

import java.util.List;
import java.util.Map;

/**
 * This class is a "proxy" between Controller and other components, such as the {@link Match} current state, and the {@link it.polimi.ingsw.view.RemoteView}.
 * It's used by the Controller:
 * - to get information about the current game (model) state;
 * - to update the game state according to the command received by Players;
 * - to notify Updates to the connected Players.
 * <p>
 * This class is useful in particular to add a layer of abstraction and to decouple responsibilities between Model and Controller: in this way the
 * component responsible to notify state changes to the View is always the Model component.
 *
 * @author Cosimo Sguanci
 * @author Andrea Mario Vergani
 * @author Roberto Spatafora
 */
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

    /**
     * Game Board getter
     *
     * @return the Game Board
     */
    public Board getBoard() {
        return this.match.getMatchBoard();
    }

    /**
     * Match Players List getter
     *
     * @return the List of the the Players playing this Match
     */
    public List<Player> getPlayers() {
        return match.getPlayers();
    }

    /**
     * Removes a Player from the Players list (used for example when a Player loses).
     *
     * @param player the player to remove
     */
    public void removePlayer(Player player) {
        match.removePlayer(player);
    }

    /**
     * This method is called to end the turn of the current Player.
     */
    public void endTurn() {
        match.nextTurn();
        turnUpdate(match.getCurrentPlayer());
    }

    /**
     * This method is used at startup to set the initial turn
     *
     * @param initialTurn the index corresponding to the Player which will be the first to play
     */
    public void setInitialTurn(int initialTurn) {
        match.setInitialTurn(initialTurn);
    }

    /**
     * Current Game Phase getter used by Controller
     *
     * @return the current Game Phase
     */
    public GamePhase getCurrentGamePhase() {
        return match.getCurrentGamePhase();
    }

    /**
     * Game Phase updater method used by Controller to change the state when a new Game Phase needs to be started
     */
    public void nextGamePhase() {
        match.nextGamePhase();
    }

    /**
     * Callback method called when a Player lost. If a Player lost, its Workers have to be removed from the game Board, and its reference
     * has to be removed from the Match Players List.
     *
     * @param loserPlayer the Player who just lost
     * @param loseCause   the reason why the Player lost
     */
    public void onPlayerLose(Player loserPlayer, LoseUpdate.LoseCause loseCause) {
        loserPlayer.getWorkerFirst().getPosition().setWorker(null);
        loserPlayer.getWorkerSecond().getPosition().setWorker(null);
        removePlayer(loserPlayer);
        loseUpdate(loserPlayer, loseCause);
        turnUpdate(getCurrentPlayer());
    }

    /**
     * Players Number getter
     *
     * @return the number of Players chosen for this match
     */
    public int getPlayersNumber() {
        return match.getPlayersNumber();
    }

    /**
     * Notifies the {@link it.polimi.ingsw.view.View} (using {@link it.polimi.ingsw.view.RemoteView} abstraction) that the current GamePhase has changed.
     *
     * @param newGamePhase the new current GamePhase for this match
     */
    public void gamePhaseUpdate(GamePhase newGamePhase) {
        GamePhaseUpdate gamePhaseUpdate = new GamePhaseUpdate(newGamePhase);
        notify(gamePhaseUpdate);
    }

    /**
     * Notifies the {@link it.polimi.ingsw.view.View} (using {@link it.polimi.ingsw.view.RemoteView} abstraction) that there was an error executing a Command.
     *
     * @param player       the Player who made the Command which caused the error
     * @param commandType  the commandType corresponding to the attempted Command
     * @param errorType    the specific ErrorType of the error raised
     * @param inhibitorGod [optional] the God that made the Command not executable
     */
    public void reportError(Player player, CommandType commandType, ErrorType errorType, Map<String, String> inhibitorGod) {
        ErrorUpdate errorUpdate = new ErrorUpdate(player, commandType, errorType, inhibitorGod);
        notify(errorUpdate);
    }

    /**
     * Notifies the {@link it.polimi.ingsw.view.View} (using {@link it.polimi.ingsw.view.RemoteView} abstraction) that some new Initial Info has been set for a Player.
     *
     * @param initialInfo the map which associates Players nicknames and Players colors.
     */
    public void initialInfoUpdate(Map<String, PrintableColor> initialInfo) {
        InitialInfoUpdate initialInfoUpdate = new InitialInfoUpdate(initialInfo);
        notify(initialInfoUpdate);
    }

    /**
     * Notifies the {@link it.polimi.ingsw.view.View} (using {@link it.polimi.ingsw.view.RemoteView} abstraction) that a new God has been set for a Player.
     *
     * @param selectableGods the Gods that have been selected by the GodChooser, but have not been chosen by any Player.
     * @param selectedGods   the map which associates Players nicknames and Players Gods.
     */
    public void godsUpdate(List<String> selectableGods, Map<String, String> selectedGods) {
        GodsUpdate godsUpdate = new GodsUpdate(selectableGods, selectedGods);
        notify(godsUpdate);
    }

    /**
     * Notifies the {@link it.polimi.ingsw.view.View} (using {@link it.polimi.ingsw.view.RemoteView} abstraction) that a there was some changes in the Game Board
     * (a Worker moved or built a new level)
     */
    public void boardUpdate() {
        BoardUpdate boardUpdate = new BoardUpdate(match.getMatchBoard().toString());
        notify(boardUpdate);
    }

    /**
     * Notifies the {@link it.polimi.ingsw.view.View} (using {@link it.polimi.ingsw.view.RemoteView} abstraction) that a there was some changes in the Game Board
     * (a Worker moved or built a new level)
     *
     * @param executedCommand the command which caused the changes to the Game Board
     */
    public void boardUpdate(PlayerCommand executedCommand) {
        BoardUpdate boardUpdate = new BoardUpdate(match.getMatchBoard().toString());
        executedCommand.setPlayerNickname(executedCommand.getPlayer().getNickname());
        boardUpdate.setExecutedCommand(executedCommand);
        notify(boardUpdate);
    }

    /**
     * Notifies the {@link it.polimi.ingsw.view.View} (using {@link it.polimi.ingsw.view.RemoteView} abstraction) that the Match has actually started.
     */
    public void matchStartedUpdate() {
        TurnUpdate turnUpdate = new TurnUpdate(match.getCurrentPlayer());
        notify(turnUpdate);
        MatchStartedUpdate matchStartedUpdate = new MatchStartedUpdate(match.getMatchBoard().toString());
        notify(matchStartedUpdate);
    }

    /**
     * Notifies the {@link it.polimi.ingsw.view.View} (using {@link it.polimi.ingsw.view.RemoteView} abstraction) that the current turn has changed.
     *
     * @param currentPlayer the new Player in turn now
     */
    public void turnUpdate(Player currentPlayer) {
        TurnUpdate turnUpdate = new TurnUpdate(currentPlayer);
        notify(turnUpdate);
    }

    /**
     * Notifies the {@link it.polimi.ingsw.view.View} (using {@link it.polimi.ingsw.view.RemoteView} abstraction) that a Player won.
     *
     * @param winnerPlayer the Player who has won the Match
     */
    public void winUpdate(Player winnerPlayer) {
        WinUpdate winUpdate = new WinUpdate(winnerPlayer);
        notify(winUpdate);
    }

    /**
     * Notifies the {@link it.polimi.ingsw.view.View} (using {@link it.polimi.ingsw.view.RemoteView} abstraction) that a Player lost.
     *
     * @param loserPlayer the Player who just lost
     * @param loseCause   the reason why the Player lost
     */
    public void loseUpdate(Player loserPlayer, LoseUpdate.LoseCause loseCause) {
        boolean onePlayerRemaining = getPlayers().size() == 1;
        LoseUpdate loseUpdate = new LoseUpdate(loserPlayer, loseCause, onePlayerRemaining, match.getMatchBoard().toString());
        notify(loseUpdate);
    }

    /**
     * Notifies the {@link it.polimi.ingsw.view.View} (using {@link it.polimi.ingsw.view.RemoteView} abstraction) that a Player disconnected.
     *
     * @param disconnectedPlayer the Player who disconnected from the match
     */
    public void disconnectedPlayerUpdate(Player disconnectedPlayer) {
        DisconnectedPlayerUpdate disconnectedPlayerUpdate = new DisconnectedPlayerUpdate(disconnectedPlayer, match.getMatchBoard().toString());
        notify(disconnectedPlayerUpdate);
    }
}
