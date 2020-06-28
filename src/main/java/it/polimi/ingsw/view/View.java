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

/**
 * This abstract class defines general methods which are used both in Cli and Gui
 * related to the experience of users in game. Both Cli and Gui extends this abstract
 * class in which general useful methods are defined.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Vergani
 */
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

    /**
     * This method allows players to hear a sound at the beginning of their turn.
     */
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

    /**
     * This static method is used to generate an only String by giving as parameter several, different Strings in a list
     *
     * @param value is a list that contains the strings to join according to the logic of the method
     * @return an only String, created by joining all the strings in value list given as parameter
     */
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

    /**
     * This method is a simple getter of the Gods' names
     *
     * @return a List containing Gods names.
     */
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
     * This method is a simple getter of the current game phase.
     * When the game phase changed this method, set the currentGamePhase attribute of
     * the class to the new current game phase of the match
     *
     * @param currentGamePhase contains a reference to the new current game phase of the match
     */
    public void setCurrentGamePhase(GamePhase currentGamePhase) {
        this.currentGamePhase = currentGamePhase;
    }

    /**
     * This method gives the possibility to know the association between nicknames and their God
     *
     * @return a Map containing an association between nickname of players involved in match and relative God.
     */
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

    /**
     * This method is a simple setter that set the number of the player involved in a match
     *
     * @param playersNumber is an integer number that indicates how many player are involved
     *                      in the match the particular Cli instance is involved in.
     */
    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    /**
     * This abstract method is declared in order to manages the start of the match for classes that extends View.
     */
    public abstract void start();

    /**
     * This is an abstract setter method. It is used to set in classes which extends View
     * a List of all the possible Gods a user related to the View can select
     *
     * @param selectableGods contains a list of all selectable Gods.
     */
    public abstract void setSelectableGods(List<String> selectableGods);

    /**
     * This is an abstract setter method. At the moment of registration to the match,
     * player are asked to set a nickname for the match. This method set a list which contains
     * all the already chosen nickname in order to have different nicknames in the same game
     *
     * @param selectedNicknames contains all the already chosen nicknames
     */
    public abstract void setSelectedNicknames(List<String> selectedNicknames);

    /**
     * This is an abstract setter method. It is used to set in classes which extends View
     * a List of all the possible colors a user related to the View can select
     *
     * @param selectableColors contains a list of all selectable colors.
     */
    public abstract void setSelectableColors(List<PrintableColor> selectableColors);

    /**
     * This method gives information about the current phase of the match
     *
     * @return the current phase of the match
     */
    public GamePhase getCurrentPhase() {
        return this.currentGamePhase;
    }

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