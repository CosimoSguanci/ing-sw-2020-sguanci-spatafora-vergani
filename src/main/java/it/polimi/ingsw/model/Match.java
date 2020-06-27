package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.exceptions.AlreadyInsidePlayerException;
import it.polimi.ingsw.exceptions.InvalidPlayerNumberException;

import java.util.ArrayList;
import java.util.List;


/**
 * Match is the class that contains general information about current game, such as
 * reference to players in it, the board (made up of cells where workers can move
 * and build), ...
 * This class sets up all data needed before starting the game, then controls some
 * aspects such as the rotation of turns, the total number of players involved in the
 * match and the possibility to do (or not) some moves. In this context, it is important
 * to specify canMove-parameter's role: it is a flag connected with Athena's power, so
 * its value depends on moves by the player owning this God (if there is one).
 *
 * @author Andrea Mario Vergani
 * @author Cosimo Sguanci
 */
public class Match {
    private final ArrayList<Player> players;
    private final Board matchBoard;
    private int turn;
    private final int playersNumber;
    private GamePhase currentGamePhase;

    /**
     * The constructor creates an instance of Match class starting from the number of
     * players who are going to join the match. So, it sets the right number of players
     * and some other general things about moves (such as attributes 'turn' and 'canMove'
     * <p>
     * -- @param number   number of total players who will join the match
     *
     * @throws it.polimi.ingsw.exceptions.InvalidPlayerNumberException when the number of players is not 2 or 3 (game option)
     */
    public Match(int playersNumber) throws InvalidPlayerNumberException {
        if (playersNumber != 2 && playersNumber != 3) throw new InvalidPlayerNumberException();
        this.players = new ArrayList<>();
        this.matchBoard = new Board();
        this.turn = 0;
        this.playersNumber = playersNumber;
        this.currentGamePhase = GamePhase.firstPhase();
    }


    /**
     * The method adds a player to the list of the ones joining the match. It is called
     * when a new player wants to enter a game room
     *
     * @param p the player who wants to join the match
     * @throws InvalidPlayerNumberException if there is no possibility for the player to join, because the selected total number has already been reached
     * @throws AlreadyInsidePlayerException if the Player passed as parameter has already been added to the match
     */
    public void addPlayer(Player p) throws InvalidPlayerNumberException, AlreadyInsidePlayerException {
        if (players.size() >= playersNumber) throw new InvalidPlayerNumberException();
        if (players.contains(p)) throw new AlreadyInsidePlayerException();
        players.add(p);
    }

    /**
     * The method removes the player from the match, if he/she is present. Otherwise,
     * nothing happens
     *
     * @param p the player to be removed from the match
     */
    public void removePlayer(Player p) {
        int position = players.indexOf(p);
        if (position >= 0) {
            players.remove(position);
            players.trimToSize();
            // when current player is removed, turn goes directly to the next player, without calling nextTurn()
            if (position == players.size()) {
                turn = 0;
            }
        }
    }

    /**
     * Match players list getter
     * @return the list of the currently playing users.
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * Players number getter
     * @return the specific Players number (2 or 3 in this version) chosen for this match.
     */
    public int getPlayersNumber() {
        return this.playersNumber;
    }


    /**
     * The method establishes who is the next player to play the turn. As seen, 'turn'
     * attribute decides who takes the current turn, so the the attribute's value is
     * the playing person's index in the ArrayList of players.
     * This method is an automatic setter of 'turn' parameter, based on the concept of
     * an established order that must be repeated until a player wins the match
     */
    public void nextTurn() {
        this.turn = (this.turn + 1) % players.size();
    }

    /**
     * Initial Turn setter
     * @param initialTurn the initial turn for this match
     */
    public void setInitialTurn(int initialTurn) {
        this.turn = initialTurn;
    }

    /**
     * The method returns the player who has the right to play his (current) turn
     *
     * @return player who is playing / going to play
     */
    public Player getCurrentPlayer() {
        return this.players.get(turn);
    }


    /**
     * The method returns corresponding game board of this specific match
     *
     * @return the board that is being used for this match
     */
    public Board getMatchBoard() {
        return this.matchBoard;
    }

    /**
     * The method is the getter for current GamePhase of the match.
     * @return current GamePhase
     */
    public GamePhase getCurrentGamePhase() {
        return this.currentGamePhase;
    }

    /**
     * This method performs a chronological switch of GamePhase. For example,
     * Santorini's manual says that, after choosing gods, every player must
     * place his/her workers on the board: these situations correspond
     * to different GamePhases, and after choosing gods the switch takes place.
     */
    public void nextGamePhase() {
        this.currentGamePhase = GamePhase.nextGamePhase(this.currentGamePhase);
    }
}
