package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.exceptions.AlreadyInsidePlayerException;
import it.polimi.ingsw.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.exceptions.NicknameAlreadyTakenException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


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
public class Match {  //tested with 100% coverage
    private ArrayList<Player> players;
    private Board matchBoard;
    private int turn;
    private int playersNumber;
    private final String id;
    private GamePhase currentGamePhase;

    private static final ConcurrentMap<String, Match> matchInstances = new ConcurrentHashMap<>();

    /**
     * The constructor creates an instance of Match class starting from the number of
     * players who are going to join the match. So, it sets the right number of players
     * and some other general things about moves (such as attributes 'turn' and 'canMove'
     *
     *-- @param number   number of total players who will join the match
     * @throws it.polimi.ingsw.exceptions.InvalidPlayerNumberException when the number of players is not 2 or 3 (game option)
     *
     */
    private Match(String id, int playersNumber) throws InvalidPlayerNumberException {
        this.id = id;

        if(playersNumber != 2 && playersNumber != 3)  throw new InvalidPlayerNumberException();
        this.players = new ArrayList<>();
        this.matchBoard = Board.getInstance(UUID.randomUUID().toString());
        this.turn = 0;
        this.playersNumber = playersNumber;
        this.currentGamePhase = GamePhase.firstPhase();
    }

    /**
     * Implements Multiton Pattern: one instance for each thread representing a match that's being played.
     *
     * @param key Thread id
     * @return a new instance of Match if the key was not already contained in matchInstances, otherwise the previous created instance.
     */
    public static Match getInstance(final String key, Integer playersNumber) {
        Match match = matchInstances.get(key);

        if (match == null)
        {
            match = new Match(key, playersNumber);
            matchInstances.putIfAbsent(key, new Match(key, playersNumber));
        }

        return match;
    }

    public static void clearInstances() {
        matchInstances.clear();
    }


    private boolean nicknameAlreadyInside(Player p) {
        String nick = p.getNickname();
        for (Player player : players) {  //explore all players
            if (player.getNickname().equals(nick)) {  //nickname already present
                return true;
            }
        }
        return false;  //case nickname not found in players list
    }


    /**
     * The method adds a player to the list of the ones joining the match. It is called
     * when a new player wants to enter a game room
     *
     * @param p     the player who wants to join the match
     * @throws Exception    if there is no possibility for the player to join, because the selected total number has already been reached
     *
     */
    public void addPlayer(Player p) throws InvalidPlayerNumberException, AlreadyInsidePlayerException, NicknameAlreadyTakenException {
        if(players.size() >= playersNumber)  throw new InvalidPlayerNumberException();
        if(players.contains(p))  throw new AlreadyInsidePlayerException();  //rather than exception, notify through View
        players.add(p);
    }

    /**
     * The method removes the player from the match, if he/she is present. Otherwise,
     * nothing happens
     *
     * @param p     the player to be removed from the match
     *
     */
    //when current player is removed, turn goes directly to the next player, without calling nextTurn()
    public void removePlayer(Player p) {
        int position = players.indexOf(p);  //index of p in the array-list; -1 if not present
        if(position >= 0) {  //p is in the array-list
            players.remove(position);
            players.trimToSize();
            //when current player is removed, turn goes directly to the next player, without calling nextTurn()
            if(position == players.size()) {
                turn = 0;
            }
        }
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public int getPlayersNumber() {
        return this.playersNumber;
    }


    /**
     * The method establishes who is the next player to play the turn. As seen, 'turn'
     * attribute decides who takes the current turn, so the the attribute's value is
     * the playing person's index in the ArrayList of players.
     * This method is an automatic setter of 'turn' parameter, based on the concept of
     * an established order that must be repeated until a player wins the match
     *
     */
    public void nextTurn() {
        this.turn = (this.turn + 1) % players.size();
    }

    public void setInitialTurn(int initialTurn) {
        this.turn = initialTurn;
    }


    /**
     * The method returns the player who has the right to play his (current) turn
     *
     * @return player who is playing / going to play
     *
     */
    public Player getCurrentPlayer() {
        return this.players.get(turn);
    }


    /**
     * The method returns corresponding game board of this specific match
     *
     * @return the board that is being used for this match
     *
     */
    public Board getMatchBoard() {
        return this.matchBoard;
    }

    public GamePhase getCurrentGamePhase() {
        return this.currentGamePhase;
    }

    public void nextGamePhase() {
        this.currentGamePhase = GamePhase.nextGamePhase(this.currentGamePhase);
    }
}
