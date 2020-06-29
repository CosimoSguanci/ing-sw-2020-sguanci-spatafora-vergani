package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.gods.GodStrategy;

import java.io.Serializable;

/**
 * This class contains information about Players who are playing a match, including
 * a unique ID, nickname, the color of the chosen workers, a reference to two different
 * workers the player can play with, a reference to the God associated to the player,
 * the match in which the player is involved in and a boolean that indicates it the player
 * is who chooses the Gods in play.
 *
 * @author Roberto Spatafora
 */
public class Player implements Serializable {
    private final String ID;
    private final transient Match match;
    private final transient Model model;
    private String nickname;
    private PrintableColor color;
    private final transient Worker workerFirst; // circular dependency if not transient
    private final transient Worker workerSecond;
    private boolean isGodChooser;
    private transient GodStrategy godStrategy;

    /**
     * This is the builder of the class. When a Player is created its id, nickname and
     * the match he is enrolled in are set.
     *
     * @param id    the String random identifier for this Player.
     * @param model the Model corresponding to this Player instance.
     * @param match the Match that this Player is about to play.
     */
    public Player(String id, Model model, Match match) {
        this.ID = id;
        this.model = model;
        this.match = match;

        this.workerFirst = new Worker(this, match.getMatchBoard(), Command.WORKER_FIRST);
        this.workerSecond = new Worker(this, match.getMatchBoard(), Command.WORKER_SECOND);
    }


    /**
     * This getter method gives information about the color the player has
     * chosen for its workers.
     *
     * @return the chosen color of worker, it returns null if the color has not been set yet
     */
    public PrintableColor getColor() {
        return this.color;
    }

    /**
     * This setter method allows players to set the color of their workers.
     *
     * @param color is the String that indicates the color chosen from a Player at the beginning of the match
     */
    public void setColor(PrintableColor color) {
        this.color = color;
    }

    /**
     * Determines if this Player is the GodChooser for its match, or not.
     *
     * @return true if the Player is the GodChooser, false otherwise.
     */
    public boolean isGodChooser() {
        return this.isGodChooser;
    }

    /**
     * GodChooser property setter
     */
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
     * This getter method returns a reference to the second worker the player has chosen.
     *
     * @return the second worker associated to the player, it returns null if the worker has not been set yet
     */
    public Worker getWorkerSecond() {
        return this.workerSecond;
    }

    /**
     * This getter method returns a reference to the GodStrategy the player has chosen.
     *
     * @return the GodStrategy associated to the player, it returns null if the God has not been set yet
     */
    public GodStrategy getGodStrategy() {
        return this.godStrategy;
    }

    /**
     * This setter method allows players to set the GodStrategy associated to them.
     *
     * @param godStrategy is the God chosen from a Player at the beginning of the match
     */
    public void setGodStrategy(GodStrategy godStrategy) {
        this.godStrategy = godStrategy;
    }

    /**
     * Player's nickname getter
     *
     * @return the Player's nickname
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * Player's nickname setter
     *
     * @param nickname the new Player's nickname to be set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Player's ID getter
     *
     * @return the Player ID, which identifies a player in a match
     */
    public String getPlayerID() {
        return this.ID;
    }

    /**
     * Player's Match getter
     *
     * @return the Match that this Player is playing
     */
    public Match getMatch() {
        return this.match;
    }

    /**
     * Player's Match Model getter
     *
     * @return the Model instance of the Match that this Player is playing
     */
    public Model getModel() {
        return this.model;
    }
}