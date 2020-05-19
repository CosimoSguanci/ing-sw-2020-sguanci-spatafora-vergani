package it.polimi.ingsw.view.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.network.client.UpdateListener;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.Manual;
import it.polimi.ingsw.view.UpdateHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.component.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Cli is the class in which interactions with users are managed.
 * This class extends Observable interface and different types of
 * command observe it.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
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
    private final String currentPhaseString = "current_phase";
    private final UpdateHandler cliUpdateHandler;

    final Controller controller;

    private boolean soundPlayed = false;

    private String currentBoard;  //Gson representation of current board of the match

    private RealGame realGame = new RealGame(this);
    private GodChoice godChoice = new GodChoice(this);
    private InitialInfo initialInfo = new InitialInfo(this);
    private MatchEnded matchEnded = new MatchEnded(this);
    private GamePreparation gamePreparation = new GamePreparation(this);
    private CliPlayerHelper cliPlayerHelper = new CliPlayerHelper(this);

    /**
     * Cli is the builder of the class. At the moment of the Cli creation
     * a client and a controller of the client are associated to it
     *
     * @param client indicates the client with whom a conversation with
     * the Cli instance created will take place.
     * @param controller indicates clientSideController implements client-side checks
     * in order to avoid repeated and unnecessary interactions with the server.
     */
    public Cli(Client client, Controller controller) {
        this.client = client;
        this.controller = controller;
        this.cliUpdateHandler = new CliUpdateHandler(this);

        client.getUpdateListener().addObserver(this); // Register to UpdateListener
    }

    /**
     * This is a method useful to avoid code repetitions
     * It is an abbreviation of stdout.println
     * @param string id the string printed by this method.
     */
    public void print(String string) {
        if (stdout == null)
            return;
        stdout.println(string);
    }

    /**
     * This is a method useful to avoid code repetitions.
     * It simply prints a white line
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
    public void start() { // todo Command Pattern?

        stdin = new Scanner(System.in);
        stdout = System.out;

        try {

            do {
                print("How many players do you want in your match? ");
                String playersNumString = stdin.nextLine();

                if (playersNumString.equals("2")) {
                    playersNum = 2;
                } else if (playersNumString.equals("3")) {
                    playersNum = 3;
                } else {
                    print("Invalid Players number: 2 or 3 players matches are available.");
                }

            } while (playersNum != 2 && playersNum != 3);

            client.sendInt(playersNum);

            gameLoop();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("The Game couldn't start, maybe there was some network error or the server isn't available.");
            System.exit(0);
        }

    }

    private boolean computeCommand(String[] splitCommand) {
        return (splitCommand.length == 0) || (splitCommand[0].equals("") && splitCommand.length == 1);
    }

    /**
     * This method manages every possible interaction with the client.
     * All the commands received by the client are parsed in this method
     * grouped by game phases.
     */
    private void gameLoop() {
        print("Waiting for a match...");
        newLine();
        print("HIGHLY RECOMMENDED: type '" + Cli.toBold("help") + "' in any moment to have information about game-phases, commands, ...");
        print("RECOMMENDED: if it's the first time you play or have any doubt during the game, type '" + Cli.toBold("rules") + "' to read a short version of Santorini's manual");
        newLine();

        String command;

        while (true) {

            command = stdin.nextLine();

            String[] splitCommand = command.toLowerCase().split("\\s+");

            if (computeCommand(splitCommand)) { continue; }

            if (splitCommand[0].length() == 0) {  // command starting with space
                splitCommand = Arrays.copyOfRange(splitCommand, 1, splitCommand.length);
            }

            try {

                if (currentGamePhase == GamePhase.MATCH_ENDED) {
                    command = command.toLowerCase();

                    if(command.equals("yes")) {
                        matchEnded.handle();
                        break;
                    }
                    else if(command.equals("no")) {
                        print("Quitting...");
                        System.exit(0);
                    }
                    else throw new BadCommandException();

                }
                else if (currentGamePhase == GamePhase.MATCH_LOST) {

                    command = command.toLowerCase();

                    if(command.equals("yes")) {
                        this.continueToWatch = true;
                        newLine();
                        continue;// todo test
                    }
                    else if(command.equals("no")) {
                        this.currentGamePhase = GamePhase.MATCH_ENDED;
                        newLine();
                        print("Do you want to play another match?");
                    }
                    else throw new BadCommandException(); // todo add multiple exception
                }
                else if (CommandType.isHelperCommandType(splitCommand[0])){
                    cliPlayerHelper.helperHandle(splitCommand);
                }
                else if (currentGamePhase == GamePhase.INITIAL_INFO) {
                    this.initialInfo.handleInitialInfoCommand(splitCommand);
                }
                else if (currentGamePhase == GamePhase.CHOOSE_GODS && isInitialGodChooser) {
                    this.godChoice.handleIsGodChooserGodsChoice(splitCommand);
                }
                else if (currentGamePhase == GamePhase.CHOOSE_GODS) { // but not initial god chooser
                    this.godChoice.handleGodChoice(splitCommand);
                }
                else if (currentGamePhase == GamePhase.GAME_PREPARATION) {
                    this.gamePreparation.handle(command);
                }
                else if (currentGamePhase == GamePhase.REAL_GAME) {
                    this.realGame.handleRealGame(command);
                }
                else {
                    print("Unknown Command");
                }
            } catch (BadCommandException e) {
                print("Bad command generated, please repeat it.");
            } catch (NicknameAlreadyTakenException e) {
                print("Nickname already taken for this match, please select another nickname.");
            } catch (InvalidColorException e) {
                print ("Invalid color requested: another player already chose it or this color is not available in this game.");
            } catch (WrongPlayerException e) {
                print ("Invalid command: please check if it's your turn!");
            }

            newLine();
        }
    }



    void forwardNotify(Update update) { // forwards update to client-side Controller
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
     * This method make possible client who are not GodChooser to select
     * a God once the GodChooser selected Gods that will be involved in the match.
     * @param selectableGods is as list used to have references to all Gods
     * a player can choose in a specific match (once GodChooser selected them)
     */
    void setSelectableGods(List<String> selectableGods) {
        this.selectableGods = selectableGods;
    }

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
     * This setter method is used to set a specific game phase.
     * It is necessary to have this method to change different phase during the match.
     * @param newGamePhase is the new phase that it is set with the invocation of this method.
     */
    void setCurrentGamePhase(GamePhase newGamePhase) {
        this.currentGamePhase = newGamePhase;

        switch(this.currentGamePhase) {
            case INITIAL_INFO:
                print("Players are choosing nickname and color... Wait for your turn.");
                newLine();
                break;
            case CHOOSE_GODS:
                print("Players are choosing their gods... Wait for your turn.");
                newLine();
                break;
            case GAME_PREPARATION:
                print("Players are placing their Workers... Wait for your turn.");
                newLine();
                break;
            case REAL_GAME:
                // real game
                break;
            case MATCH_ENDED:
                print("Do you want to play another match?");
                newLine();
                break;
        }
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
     * This method contains an algorithm to print the board game Cli version.
     * Every cell is printed as a 5x5; there are boarders which delimit each cell.
     * @param board indicates the board which is used in the relative match.
     *              It contains references to each cell (including their level
     *              and workers if there are on that cell).
     */
    public void printBoard(String board) {
        this.currentBoard = board;  //new Gson version of board is saved

        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();
        Board gameBoard = gson.fromJson(board, Board.class);


        char rowIdentifier = 'A';

        print("");
        print("");
        print("");

        for (int i = 0; i < 5; i++) {    //Single cell printed as 5x5: +---+ boarders; " "/"1"/"2" if worker is inside; BlockType specified.
            System.out.println("\t+  -  -  -  +  +  -  -  -  +  +  -  -  -  +  +  -  -  -  +  +  -  -  -  +");
            System.out.println("\t|         " + convertBlockTypeToUnicode(gameBoard.getCell(i, 0).getLevel()) + " | " +
                    " |         " + convertBlockTypeToUnicode(gameBoard.getCell(i, 1).getLevel()) + " | " +
                    " |         " + convertBlockTypeToUnicode(gameBoard.getCell(i, 2).getLevel()) + " | " +
                    " |         " + convertBlockTypeToUnicode(gameBoard.getCell(i, 3).getLevel()) + " | " +
                    " |         " + convertBlockTypeToUnicode(gameBoard.getCell(i, 4).getLevel()) + " | ");

            System.out.print(rowIdentifier + "\t");
            rowIdentifier++;

            for (int j = 0; j < 5; j++) {
                System.out.print("|    ");
                if (!gameBoard.getCell(i, j).isEmpty()) {

                    Worker printableWorker = gameBoard.getCell(i, j).getWorker();
                    if (printableWorker.workerType.equals(Command.WORKER_FIRST)) {
                        System.out.print(convertColorToAnsi(printableWorker.player.getColor()) + " W1" + PrintableColor.RESET);
                    } else {
                        System.out.print(convertColorToAnsi(printableWorker.player.getColor()) + " W2" + PrintableColor.RESET);
                    }
                } else {
                    System.out.print("   ");
                }
                System.out.print("    |  ");

            }

            System.out.println();
            System.out.println("\t|           |  |           |  |           |  |           |  |           |");
            System.out.println("\t+  -  -  -  +  +  -  -  -  +  +  -  -  -  +  +  -  -  -  +  +  -  -  -  +");

        }


        System.out.println("\t      1              2              3              4              5    ");


        print("");
        print("");
        print("");
    }

    //TODO eliminate: getLevelNumber() is a BlockType method.
    static String convertBlockTypeToUnicode(BlockType level) { // todo move to BlockType
        switch (level) {
            case GROUND:
                return "0";
            case LEVEL_ONE:
                return "1";
            case LEVEL_TWO:
                return "2";
            case LEVEL_THREE:
                return "3";
            case DOME:
                return "D";
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * This method converts a color given as parameter according to enum
     * defined, and it is used to have the correct correspondence Ansi color.
     * @param color is the PrintableColor you want to calculate the Ansi code
     * @return the string of the respective Ansi color.
     */
    static String convertColorToAnsi (PrintableColor color) { // todo move to PrintableColor
        switch (color) {
            case RED:
                return "\u001B[31m";
            case BLUE:
                return "\u001B[34m";
            case GREEN:
                return "\u001B[32m";
            case YELLOW:
                return "\u001B[33m";
            case PURPLE:
                return "\u001B[35m";
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * This method is used to print Gods chosen by users.
     * Its format is [nickname] has [God Name] and it is
     * printed for each player involved in the match
     */
    public void printPlayerGods() {
        this.playersGods.keySet().forEach((key) -> {
            print(playerWithColor(key) + " has " + playersGods.get(key));
        });
    }

    /**
     * This method is used to print colors chosen by users.
     * Its format is [nickname] is [color] and it is
     * printed for each player involved in the match
     */
    void printPlayersColors() {
        this.playersColors.keySet().forEach((key) -> {
            print(key + " is " + convertColorToAnsi(playersColors.get(key)) + playersColors.get(key) + PrintableColor.RESET);
        });
    }

    String playerWithColor(String nickname) {
        if(playersColors != null) {

            if(!playersColors.containsKey(nickname) && nickname.equals("Player")) {
                return nickname;
            }

            return convertColorToAnsi(playersColors.get(nickname)) + nickname + PrintableColor.RESET;
        }
        return null;
    }

    public void printCurrentTurn() {
        String currentPlayerNickname = controller.getCurrentPlayerNickname();
        if(currentPlayerNickname != null && playerWithColor(currentPlayerNickname) != null) {

            if (!controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) {  //not player's turn
                soundPlayed = false;
                print("It's " + playerWithColor(currentPlayerNickname) + "'s turn!");
            } else {  //client's turn
                print("It's" + convertColorToAnsi(playersColors.get(currentPlayerNickname)) + " your " + PrintableColor.RESET + "turn!");

                if(!soundPlayed) {
                    try {
                        URL defaultSound = getClass().getResource("/turn.wav");
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(defaultSound);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start( );

                        soundPlayed = true;
                    } catch (Exception ignored) {}
                }

            }
            newLine();
        }
    }

    public GamePhase getCurrentPhase() {
        return this.currentGamePhase;
    }

    boolean wantsToContinueToWatch() {
        return this.continueToWatch;
    }

    public void setContinueToWatch(boolean continueToWatch) {
        this.continueToWatch = continueToWatch;
    }

    public static String toBold(String s) {
        return PrintableColor.BOLD + s + PrintableColor.RESET;
    }

    public Client getClient(){
        return this.client;
    }

    public String getCurrentBoard() {
        return this.currentBoard;
    }

    public String getCurrentPhaseString() {
        return this.currentPhaseString;
    }
}
