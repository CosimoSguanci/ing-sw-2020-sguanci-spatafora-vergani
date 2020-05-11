package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.commands.Command;
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
    private String nickname;
    private PrintableColor color;
    private transient Worker workerFirst; // circular dependency
    private transient Worker workerSecond;
    private transient boolean isGodChooser;
    public final transient Match match;
    public final transient Model model;
    private transient GodStrategy godStrategy;

    /**
     * This is the builder of the class. When a Player is created its id, nickname and
     * the match he is enrolled in are set.
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
     * This getter method returns a reference to the second worker the player has chosen.
     *
     * @return the second worker associated to the player, it returns null if the worker has not been set yet
     */
    public Worker getWorkerSecond() {
        return this.workerSecond;
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
     * This getter method returns a reference to the GodStrategy the player has chosen.
     *
     * @return the GodStrategy associated to the player, it returns null if the God has not been set yet
     */
    public GodStrategy getGodStrategy() {
        return this.godStrategy;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }
}