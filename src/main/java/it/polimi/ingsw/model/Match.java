package it.polimi.ingsw.model;

import java.util.ArrayList;


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
 */
public class Match {  //tested with 100% coverage
    private ArrayList<Player> players;
    private Board matchBoard;
    private int turn;
    private boolean canMove;
    private int playersNumber;

    /**
     * The constructor creates an instance of Match class starting from the number of
     * players who are going to join the match. So, it sets the right number of players
     * and some other general things about moves (such as attributes 'turn' and 'canMove'
     *
     * @param number   number of total players who will join the match
     * @throws Exception    when the number of players is not 2 or 3 (game option)
     *
     */
    public Match(int number) throws Exception {
        if(number != 2 && number != 3)  throw new Exception();
        this.players = new ArrayList<>();
        this.matchBoard = new Board();
        this.turn = 0;
        this.canMove = true;
        this.playersNumber = number;
    }

    private boolean nicknameAlreadyInside(Player p) {
        String nick = p.nickname;
        for (Player player : players) {  //explore all players
            if (player.nickname.equals(nick)) {  //nickname already present
                return true;
            }
        }  //end cycle
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
    public void addPlayer(Player p) throws Exception {
        if(players.size() >= playersNumber)  throw new Exception();
        if(players.contains(p))  throw new Exception();  //rather than exception, notify through View
        if(nicknameAlreadyInside(p))  throw new Exception();  //rather than exception, notify through View
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
                turn=0;
            }
        }
    }

    /**
     * The method is a getter for 'canMove' attribute of the class
     *
     * @return value of canMove
     *
     */
    public boolean getCanMove() {
        return canMove;
    }


    /**
     * The method sets the value of 'canMove' attribute, so that the new value is
     * the one passed as parameter
     *
     * @param canMove   new value of canMove
     *
     */
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
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


    /**
     * The method returns the player who has the right to play his (current) turn
     *
     * @return player who is playing / going to play
     *
     */
    public Player getCurrentPlayer() {
        return players.get(turn);
    }
}