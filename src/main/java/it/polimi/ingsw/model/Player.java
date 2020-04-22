package it.polimi.ingsw.model;

import it.polimi.ingsw.model.gods.GodStrategy;

/**
 * This class contains information about Players who are playing a match, including
 * a unique ID, nickname, the color of the chosen workers, a reference to two different
 * workers the player can play with, a reference to the God associated to the player,
 * the match in which the player is involved in and a boolean that indicates it the player
 * is who chooses the Gods in play.
 *
 * @author Roberto Spatafora
 */
public class Player {
    public final String ID;
    public final String nickname;
    private String color;
    private Worker workerFirst;
    private Worker workerSecond;
    private boolean isGodChooser;
    public final Match match;
    //private God god;
    private GodStrategy godStrategy;

    /**
     * This is the builder of the class. When a Player is created its id, nickname and
     * the match he is enrolled in are set.
     */
    public Player(String id, String nickname, Match match) {
        this.ID = id;
        this.nickname = nickname;
        this.match = match;
    }

    /**
     * This getter method gives information about the color the player has
     * chosen for its workers.
     *
     * @return the chosen color of worker, it returns null if the color has not been set yet
     */
    public String getColor() {
        return this.color;
    }

    /**
     * This setter method allows players to set the color of their workers.
     *
     * @param color is the String that indicates the color chosen from a Player at the beginning of the match
     */
    public void setColor(String color) {
        this.color = color;
    }

    public boolean isGodChooser() {
        return this.isGodChooser;
    }

    public void setAsGodChooser() {
        this.isGodChooser = true;
    }

    /**
     * This getter method returns a reference to the first worker the player has chosen.
     *
     * @return the first worker associated to the player, it returns null if the worker has not been set yet
     */
    public Worker getWorkerFirst() {
        return this.workerFirst;
    }

    /**
     * This setter method allows players to set the first of their workers.
     *
     * @param firstWorker is the first worker chosen from a Player at the beginning of the match
     */
    public void setWorkerFirst(Worker firstWorker) {
        this.workerFirst = firstWorker;
    }

    /**
     * This getter method returns a reference to the second worker the player has chosen.
     *
     * @return the second worker associated to the player, it returns null if the worker has not been set yet
     */
    public Worker getWorkerSecond() {
        return this.workerSecond;
    }

    /**
     * This setter method allows players to set the second of their workers.
     *
     * @param secondWorker is the second worker chosen from a Player at the beginning of the match
     */
    public void setWorkerSecond(Worker secondWorker) {
        this.workerSecond = secondWorker;
    }

    //TODO remove God
    /**
     * This getter method returns a reference to the God the player has chosen.
     *
     * @return the God associated to the player, it returns null if the God has not been set yet
     */
    /*
        public God getGod() {

        return this.god;
    }
    */

    //TODO remove God
    /**
     * This setter method allows players to set the God associated to them.
     *
     * @param //god is the God chosen from a Player at the beginning of the match
     */
    /*
    public void setGod(God god) {
        this.god = god;
    }
    */

    public void setGodStrategy(GodStrategy godStrategy) {
        this.godStrategy = godStrategy;
    }

    public GodStrategy getGodStrategy() {
        return this.godStrategy;
    }
}