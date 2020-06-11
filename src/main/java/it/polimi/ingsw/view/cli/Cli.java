package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.UpdateHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.components.*;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;


/**
 * Cli is the class in which interactions with users are managed.
 * This class extends Observable interface and different types of
 * command observe it.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Vergani
 */
public class Cli extends View implements Observer<Update> {
    private final Client client;
    private int playersNum;
    private Scanner stdin;
    private PrintStream stdout;

    // TODO Put currentGamePhase in common superclass with GUI

    private GamePhase currentGamePhase;
    private List<String> selectedNicknames;
    private List<PrintableColor> selectableColors;
    private boolean isInitialGodChooser = false;
    private boolean continueToWatch = false;
    private List<String> selectableGods;
    private Map<String, String> playersGods;
    private Map<String, PrintableColor> playersColors;
    private final UpdateHandler cliUpdateHandler;

    private final Controller controller;

    /**
     *  JSON representation of current board of the match
     */
    private String currentBoard;

    private final GamePhaseCommandHandler gamePhaseCommandHandler = new GamePhaseCommandHandler(this);
    private final OtherInfoHandler otherInfoHandler = new OtherInfoHandler(this);

    /**
     * Cli is the builder of the class. At the moment of the Cli creation
     * a client and a controller of the client are associated to it.
     * A new instance of CliUpdateHandler is created which manages
     * all the updates notified by server.
     *
     * @param client indicates the client with whom a conversation with
     * the Cli instance created will take place.
     * @param controller indicates clientSideController implements client-side checks
     * in order to avoid repeated and unnecessary interactions with the server.
     */
    public Cli(Client client, Controller controller) {
        this.client = client;
        this.controller = controller;
        this.cliUpdateHandler = new CliUpdateHandler(this, controller);
    }

    /**
     * This is a method useful to avoid code repetitions
     * It is an abbreviation of stdout.println
     * @param string id the string printed by this method.
     */
    public void println(String string) {
        if (stdout == null)
            return;
        stdout.println(string);
    }

    /**
     * This is a method useful to avoid code repetitions.
     * It simply prints a blank line
     */
    public void newLine() {
        if (stdout == null)
            return;
        stdout.println();
    }

    /**
     * In this method, once a connection between client and server
     * is established, every game phase is managed.
     */
    @Override
    public void start() {
        stdin = new Scanner(System.in);
        stdout = System.out;
        try {
            do {
                println("How many players do you want in your match? ");
                String playersNumString = stdin.nextLine();
                if (playersNumString.equals("2")) {
                    playersNum = 2;
                } else if (playersNumString.equals("3")) {
                    playersNum = 3;
                } else {
                    println("Invalid Players number: 2 or 3 players matches are available.");
                }
            } while (playersNum != 2 && playersNum != 3);
            client.sendPlayersNumber(playersNum);
            String clientID = client.readPlayerID();
            controller.setClientPlayerID(clientID);
            client.setupUpdateListener();
            client.getUpdateListener().addObserver(this); // Register to UpdateListener
            this.gamePhaseCommandHandler.gameLoop();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("The Game couldn't start, maybe there was some network error or the server isn't available.");
            System.exit(0);
        }
    }

    /**
     * This method forwards an update received to the
     * client-side Controller
     * @param update is the update received from Cli which is
     *               forwarded to the Controller client-side.
     */
    void forwardNotify(Update update) {
        notify(update);
    }

    /**
     * This method is a setter used to set to the received value of
     * the parameter if this client is or not the GodChooser.
     * @param value is received as boolean parameter from the server,
     * after having it randomly from the players list.
     */
    void setInitialGodChooser(boolean value) {
        this.isInitialGodChooser = value;
    }

    /**
     * This method is a simple getter to have access to the private
     * instance of the boolean attribute isInitialGodChooser of this class.
     * @return true if isInitialGodChooser is true value set,
     *         otherwise it returns false.
     */
    public boolean getInitialGodChooser() {
        return this.isInitialGodChooser;
    }

    /**
     * This method make possible client who are not GodChooser to select
     * a God once the GodChooser selected Gods that will be involved in the match.
     * @param selectableGods is as list used to have references to all Gods
     * a player can choose in a specific match (once GodChooser selected them)
     */
    void setSelectableGods(List<String> selectableGods) {
        this.selectableGods = selectableGods;
    }

    /**
     * This is a method used to have a List that contains all the Gods a player can
     * choose after GodChooser player selected them.
     * @return a List with Gods references which are selectable from a
     *         single player, during CHOOSE_GODS game phase.
     */
    public List<String> getSelectableGods() {
        return this.selectableGods;
    }

    /**
     * This method let clients choose a nickname unique in the match.
     * @param selectedNicknames is a list of all nickname already chosen from players.
     */
    void setSelectedNicknames(List<String> selectedNicknames) {
        this.selectedNicknames = selectedNicknames;
    }

    /**
     * This method is used to have a list that contains all the nicknames already
     * chosen from different Players in a match. It is used in the first phase
     * of a match, during INITIAL_INFO game phase, to avoid the possibility that
     * different players involved in a match choose the same nicknames.
     * @return a List with all the names already chosen from
     *         other players for a single match.
     */
    public List<String> getSelectedNicknames() {
        return this.selectedNicknames;
    }

    /**
     * This method let clients choose a color unique in the match.
     * @param selectableColors is a list of all color not yet chosen from players.
     */
    void setSelectableColors(List<PrintableColor> selectableColors) {
        this.selectableColors = selectableColors;
    }

    /**
     * This method is used to have a list that contains all the colors already
     * chosen from different Players in a match. It is used in the first phase
     * of a match, during INITIAL_INFO game phase, to avoid the possibility that
     * different players involved in a match choose the same color.
     * @return a List with all the colors already chosen from
     *         other players for a single match.
     */
    public List<PrintableColor> getSelectableColors() {
        return this.selectableColors;
    }

    /**
     * This getter method gives information about the number of player involved in a match
     * @return the number of player involved in the match
     */
    public int getPlayersNum() {
        return this.playersNum;
    }

    /**
     * This method is a simple getter that set the number of the player involved in a match
     * @param playersNum is an integer number that indicates how many player are involved
     *                   in the match the particular Cli instance is involved in.
     */
    public void setPlayersNum(int playersNum) {
        this.playersNum = playersNum;
    }

    /**
     * This method makes a correspondence to the client and the God associated.
     * @param playersGods is the corresponding God to the client.
     */
    void setPlayersGods(Map<String, String> playersGods) {
        this.playersGods = playersGods;
    }

    /**
     * This method makes a correspondence to the client and the color associated.
     * @param playersColors is the corresponding color to the client.
     */
    void setPlayersColors(Map<String, PrintableColor> playersColors) {
        this.playersColors = playersColors;
    }

    /**
     * This method is a getter which gives a correspondence to the color chosen
     * from a single player.
     * @return an association between a String which contains the nickname
     *         of a player involved and a PrintableColor that indicates a color
     *         a player can choose during INITIAL_INFO game phase.
     */
    public Map<String, PrintableColor> getPlayersColors() {
        return this.playersColors;
    }

    /**
     * This setter method is used to set a specific game phase.
     * It is necessary to have this method to change different phase during the match.
     * @param newGamePhase is the new phase that it is set with the invocation of this method.
     */
    public void setCurrentGamePhase(GamePhase newGamePhase) {
        this.currentGamePhase = newGamePhase;

        this.otherInfoHandler.printGamePhase(this.currentGamePhase);
    }

    /**
     * This method manages all the message received from the server.
     * Visitor pattern used to invoke the correct method for each different
     * instance of update from server to client.
     * @param update contains references to what changes server-side
     *               and it is notified to the client.
     */
    @Override
    public void update(Update update) {
        update.handleUpdate(this.cliUpdateHandler);
    }

    /**
     * This method converts a color given as parameter according to enum
     * defined, and it is used to have the correct correspondence Ansi color.
     * @param color is the PrintableColor you want to calculate the Ansi code
     * @return the string of the respective Ansi color.
     */
    public static String convertColorToAnsi (PrintableColor color) {
        return PrintableColor.convertColorToAnsi(color);
    }

    /**
     * This method is used to print Gods chosen by users.
     * Its format is [nickname] has [God Name] and it is
     * printed for each player involved in the match
     */
    public void printPlayerGods() {
        this.playersGods.keySet().forEach((key) -> println(playerWithColor(key) + " has " + playersGods.get(key)));
    }

    /**
     * This method is used to print colors chosen by users.
     * Its format is [nickname] is [color] and it is
     * printed for each player involved in the match
     */
    void printPlayersColors() {
        this.playersColors.keySet().forEach((key) -> println(key + " is " + convertColorToAnsi(playersColors.get(key)) + playersColors.get(key) + PrintableColor.RESET));
    }

    /**
     * This method is used to print a specific nickname of a player
     * involved in a match with the same color the player choose
     * during the INITIAL_INFO game phase.
     * @param nickname is a String which contains the nickname of a player
     *                 involved in the match.
     * @return a colored String. Colored with color chosen from the player with
     *         the nickname given as parameter.
     */
    public String playerWithColor(String nickname) {
        if(this.playersColors != null) {

            if(!this.playersColors.containsKey(nickname) && nickname.equals("Player")) {
                return nickname;
            }

            return convertColorToAnsi(this.playersColors.get(nickname)) + nickname + PrintableColor.RESET;
        }
        return null;
    }

    /**
     * This method is simply used as delegator. It is invoked from CliUpdateHandler
     * when a match starts, a player's turn ends or when a player asks whose turn is it.
     * It invokes otherInfoHandler.printCurrentTurn in which all the prints are defined.
     */
    public void printCurrentTurn() {
        this.otherInfoHandler.printCurrentTurn();
    }

    /**
     * This method is used to print during GAME_PREPARATION game phase
     * a particular string which invites Players to place them workers
     * on the board.
     */
    public void printGamePreparationInfo() {
        println("Game Preparation: place your " + Cli.toBold("workers") + ".    Command " + Cli.toBold("format") + " expected: place W1 [row1][col1]  W2 [row2][col2]");
        newLine();
    }

    /**
     * This method gives information about the current phase of the match
     * @return the current phase of the match
     */
    public GamePhase getCurrentPhase() {
        return this.currentGamePhase;
    }

    /**
     * This method is a simply getter. Once the player associated to the Cli
     * lose, this method gives information if the wants to continue watch the match
     * in which he was involved.
     * @return true if the player response to the question
     * "Do you want to continue to watch this match?" was positive, false otherwise.
     */
    boolean wantsToContinueToWatch() {
        return this.continueToWatch;
    }

    /**
     * This method set continueToWatch attribute to the boolean value given
     * as parameter. It indicates if a player which is involved in a match, after
     * losing the match wants to watch other players play
     * @param continueToWatch is a boolean value that indicates if the
     *                        player wants continue to watch
     */
    public void setContinueToWatch(boolean continueToWatch) {
        this.continueToWatch = continueToWatch;
    }

    /**
     * This method is used to make a string given as parameter in bold
     * @param s is the string that you want in bold
     * @return the bold String
     */
    public static String toBold(String s) {
        return PrintableColor.BOLD + s + PrintableColor.RESET;
    }

    /**
     * This is the getter of the Client
     * @return the Client associated to a particular instance of Cli
     */
    public Client getClient(){
        return this.client;
    }

    /**
     * This method is used to have the JSON representation of the current Board.
     * @return the String JSON-format of the board
     */
    public String getCurrentBoard() {
        return this.currentBoard;
    }

    /**
     * This method is a simple setter of the String attribute currentBoard to the
     * @param board given in JSON representation.
     */
    public void setCurrentBoard(String board) {
        this.currentBoard = board;
    }

    /**
     * This method is a String getter which
     * @return a particular string useful in many prints
     */
    public String getCurrentPhaseString() {
        return "current_phase";
    }

    /**
     * This method prints the board. It is used when a player moves one of
     * his workers, or build a level. It is also used when a player requests
     * "BOARD" command.
     * @param board is the JSON-format of the board to be printed.
     */
    public void printBoard(String board) {
        this.gamePhaseCommandHandler.printBoard(board);
    }

    /**
     * This method makes available what is received as input from console.
     * @return the Scanner stdin attribute in order to have access to monitor
     *         what Cli receives as input in console.
     */
    public Scanner getStdin() {
        return this.stdin;
    }

    /**
     * This method is a simple getter of the Controller client-side
     * @return the instance of the Controller associated to Cli.
     */
    public Controller getController() {
        return this.controller;
    }
}