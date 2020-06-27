package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.observer.Observable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class View extends Observable<Object> {

    protected final Client client;
    protected final Controller controller;
    protected UpdateHandler updateHandler;

    protected GamePhase currentGamePhase;
    protected int playersNumber;

    protected Map<String, String> playersGods;
    protected Map<String, PrintableColor> playersColors;

    protected View(Client client, Controller controller) {
        this.client = client;
        this.controller = controller;
    }

    public static void playOnTurnSound() {
        try {
            URL defaultSound = View.class.getResource("/sounds/turn.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(defaultSound);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ignored) {
        }
    }

    public static StringBuilder listToStringBuilder(List<String> value) {
        if (value.size() == 0) {
            return null;
        }
        StringBuilder result = new StringBuilder(value.get(0));
        for (int i = 1; i < value.size(); i++) {
            result.append(", ").append(value.get(i));
        }
        return result;
    }

    public static List<String> getGodsNamesList() {
        return new ArrayList<>(GodsUtils.getGodsInfo().keySet());  //list of gods' names
    }

    /**
     * This is the getter of the Client
     *
     * @return the Client associated to a particular instance of Cli
     */
    public Client getClient() {
        return this.client;
    }

    /**
     * This method is a simple getter of the Controller client-side
     *
     * @return the instance of the Controller associated to Cli.
     */
    public Controller getController() {
        return this.controller;
    }

    /**
     * This method is a simple setter that set the number of the player involved in a match
     *
     * @param playersNumber is an integer number that indicates how many player are involved
     *                      in the match the particular Cli instance is involved in.
     */
    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    public void setCurrentGamePhase(GamePhase currentGamePhase) {
        this.currentGamePhase = currentGamePhase;
    }

    public Map<String, String> getPlayersGods() {
        return this.playersGods;
    }

    /**
     * This method makes a correspondence to the client and the God associated.
     *
     * @param playersGods is the corresponding God to the client.
     */
    public void setPlayersGods(Map<String, String> playersGods) {
        this.playersGods = playersGods;
    }

    /**
     * This method is a getter which gives a correspondence to the color chosen
     * from a single player.
     *
     * @return an association between a String which contains the nickname
     * of a player involved and a PrintableColor that indicates a color
     * a player can choose during INITIAL_INFO game phase.
     */
    public Map<String, PrintableColor> getPlayersColors() {
        return this.playersColors;
    }

    /**
     * This method makes a correspondence to the client and the color associated.
     *
     * @param playersColors is the corresponding color to the client.
     */
    public void setPlayersColors(Map<String, PrintableColor> playersColors) {
        this.playersColors = playersColors;
    }

    /**
     * This getter method gives information about the number of player involved in a match
     *
     * @return the number of player involved in the match
     */
    public int getPlayersNumber() {
        return this.playersNumber;
    }

    public abstract void start();

    public abstract void setSelectableGods(List<String> selectableGods);

    public abstract void setSelectedNicknames(List<String> selectedNicknames);

    public abstract void setSelectableColors(List<PrintableColor> selectableColors);

    /**
     * This method forwards an update received to the
     * client-side Controller
     *
     * @param update is the update received from Cli or Gui which is
     *               forwarded to the Controller client-side.
     */
    public void forwardNotify(Update update) {
        notify(update);
    }
}