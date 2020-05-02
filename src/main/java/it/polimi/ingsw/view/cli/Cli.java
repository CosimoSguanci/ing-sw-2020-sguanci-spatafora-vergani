package it.polimi.ingsw.view.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.PrintableColour;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.gods.*;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.network.client.UpdateListener;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.UpdateHandler;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class Cli extends Observable<Object> implements Observer<Update> {
    private Client client;
    private int playersNum;
    private Scanner stdin;
    private PrintStream stdout;
    private Map<String, String> playerSymbol;

    // TODO Put currentGamePhase in common superclass with GUI


    private GamePhase currentGamePhase;

    private List<String> selectedNicknames;
    private List<PrintableColour> selectableColors;

    private boolean isInitialGodChooser = false;

    private List<String> selectableGods;
    private Map<String, String> playersGods;
    private Map<String, PrintableColour> playersColors;

    private final UpdateHandler cliUpdateHandler;

    final Controller controller; /// WIP

    public Cli(Client client, Controller controller) {
        this.client = client;
        this.controller = controller; // WIP
        this.cliUpdateHandler = new CliUpdateHandler(this);
    }

    void print(String string) {
        if (stdout == null)
            return;
        stdout.println(string);
    }


    public void start() { // todo Command Pattern?

        stdin = new Scanner(System.in);
        stdout = System.out;

        try {

            do {

                print("How many players do you want in you match? ");
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

        } catch (Exception e) {
            print("Unknown Error: " + e.getMessage());
            System.exit(0);
        }

    }

    private void gameLoop() {
        print("Waiting for a match...");

        String command;

        while (true) {

            command = stdin.nextLine();

            String[] splitCommand = command.split("\\s+");

            if ((splitCommand.length == 0) || (splitCommand[0].equals("") && splitCommand.length == 1)) {
                continue;
            }

            if (splitCommand[0].length() == 0) {  // command starting with space
                splitCommand = Arrays.copyOfRange(splitCommand, 1, splitCommand.length);
            }

            try {

                if (currentGamePhase == GamePhase.MATCH_ENDED) {

                    command = command.toLowerCase();

                    if(command.equals("yes")) {

                        try {
                            client.closeConnection();
                            client.reinitializeConnection();

                            UpdateListener updateListener = new UpdateListener(client.getSocket());
                            new Thread(updateListener).start();
                            updateListener.addObserver(this);

                            this.playerSymbol = null;

                        } catch(IOException e) {
                            e.printStackTrace();
                            System.err.println("The Game couldn't start, maybe there was some network error or the server isn't available.");
                            System.exit(0);
                        }

                        start();
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
                        //continue;
                        break;
                    }
                    else if(command.equals("no")) {
                        this.currentGamePhase = GamePhase.MATCH_ENDED;
                        print("Do you want to play another match?");

                    }
                    else throw new BadCommandException(); // todo add multiple exception

                }
                else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.HELP && (splitCommand.length == 1)) { // todo extend help
                    print("If you want information about a specific game-phase: help <game-phase>");
                    print("Game phases are: " + GamePhase.toStringBuilder());
                    print("help -> print command list and tutorial");
                    print("info <god> -> get info about a god");
                    print("build w1/w2 [letter, number] [optional: blockType {one, two, three, dome}] -> tries to build with the chosen Worker in the specified position");
                    print("move  w1/w2 [letter, number] -> tries to move with the chosen Worker to the specified position\"");
                    print("end -> tries to end the current turn");
                    print("Info Example : info Apollo");
                    print("Move Example : move w1 a1");
                    print("Build Example: build w1 a2 dome");
                }
                else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.HELP && (splitCommand.length == 2)) {
                    if(GamePhase.parseGamePhase(splitCommand[1]) == GamePhase.INITIAL_INFO) {
                        print("In this phase, you decide your nickname and your workers' colour");
                        print("Command format: pick <nickname> <colour>");
                        print("Command example: pick Mike green");
                    }
                    else if(GamePhase.parseGamePhase(splitCommand[1]) == GamePhase.CHOOSE_GODS) {
                        print("In this phase, you select your god");
                        print("Command format: select <god>");
                        print("Command example: select Apollo");
                    }
                    else if(GamePhase.parseGamePhase(splitCommand[1]) == GamePhase.GAME_PREPARATION) {
                        print("In this phase, you place your workers over the board");
                        print("Command format: place w1 [letter, number] w2 [letter, number]");
                        print("Command example: place w1 A1 w2 B2");
                    }
                    else if(GamePhase.parseGamePhase(splitCommand[1]) == GamePhase.REAL_GAME) {
                        print("In this phase, you play!");
                        print("Move format: move w1/w2 [letter, number] -> tries to move with the chosen Worker to the specified position");
                        print("Move example: move w1 A1");
                        print("Build format: build w1/w2 [letter, number] [optional: blockType {one, two, three, dome}] -> tries to build with the chosen Worker in the specified position");
                        print("Build example: build w1 A2");
                        print("Build example: build w1 A2 dome");
                        print("End format: end -> tries to end the current turn");
                        print("End example: end");
                    }
                    else{
                        throw new BadCommandException();
                    }
                }


                else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.INFO) {
                    if (splitCommand.length != 2) {
                        throw new BadCommandException();
                    }
                    String god = splitCommand[1];

                    printGodInfo(god);
                } else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.QUIT) {
                    if (splitCommand.length > 1) {
                        throw new BadCommandException();
                    }
                    print("Quitting...");
                    System.exit(0);
                }
                else if (currentGamePhase == GamePhase.INITIAL_INFO) {
                    if (CommandType.parseCommandType(splitCommand[0]) != CommandType.PICK || splitCommand.length != 3) {
                        throw new BadCommandException();
                    }

                    String nickname = splitCommand[1];

                    if (selectedNicknames.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(nickname)) {
                        print("This nickname was already taken for this match");
                        throw new BadCommandException();
                    }

                    String color = splitCommand[2];

                    if(!PrintableColour.isValidColor(color)) {
                        print("Not a valid color");
                        throw new BadCommandException();
                    }

                    PrintableColour actualColor = Enum.valueOf(PrintableColour.class, color.toUpperCase());

                    if(!selectableColors.contains(actualColor)) {
                        print("Not a selectable color");
                        throw new BadCommandException();
                    }

                    InitialInfoCommand initialInfoCommand = new InitialInfoCommand(nickname, actualColor);
                    notify(initialInfoCommand);

                    print("Wait for other players to choose their nicknames and colors...");


                } else if (currentGamePhase == GamePhase.CHOOSE_GODS && isInitialGodChooser) {

                    if (CommandType.parseCommandType(splitCommand[0]) != CommandType.SELECT || splitCommand.length != playersNum + 1) {
                        print("You have to use the SELECT command, and type " + playersNum + " names of gods separated by spaces");
                        throw new BadCommandException();
                    }

                    ArrayList<String> chosenGods = new ArrayList<>();

                    for (int i = 0; i < playersNum; i++) {
                        String god = splitCommand[i + 1];

                        if (!isValidGod(god, chosenGods)) {
                            throw new BadCommandException();
                        }

                        chosenGods.add(god);
                    }

                    //enableGodChoose = false;

                    GodChoiceCommand godChoiceCommand = new GodChoiceCommand(chosenGods, true);
                    notify(godChoiceCommand);

                    print("Wait for other players to choose their gods...");

                } else if (currentGamePhase == GamePhase.CHOOSE_GODS) { // but not initial god chooser

                    if (CommandType.parseCommandType(splitCommand[0]) != CommandType.SELECT || splitCommand.length > 2) {
                        throw new BadCommandException();
                    }

                    String god = splitCommand[1];

                    if (!selectableGods.contains(god)) {
                        throw new BadCommandException();
                    }

                    ArrayList<String> selected = new ArrayList<>();
                    selected.add(god);
                    GodChoiceCommand godChoiceCommand = new GodChoiceCommand(selected, false);
                    notify(godChoiceCommand);

                    print("Wait for other players to choose their gods...");
                } else if (currentGamePhase == GamePhase.GAME_PREPARATION) {

                    GamePreparationCommand gamePreparationCommand = GamePreparationCommand.parseInput(command);
                    notify(gamePreparationCommand);

                    print("Wait for other players to place their Workers...");


                } else if (currentGamePhase == GamePhase.REAL_GAME) {

                    PlayerCommand playerCommand = PlayerCommand.parseInput(command);
                    notify(playerCommand);

                   // print("Wait for other players to choose their gods...");


                }
                else {
                    print("Unknown Command");
                }
            } catch (BadCommandException e) {
                print("Bad command generated, please repeat it.");
            }
        }
    }


    void forwardNotify(Update update) { // forwards update to client-side Controller
        notify(update);
    }

    void setInitialGodChooser(boolean value) {
        this.isInitialGodChooser = value;
    }

    void setSelectableGods(List<String> selectableGods) {
        this.selectableGods = selectableGods;
    }

    void setSelectedNicknames(List<String> selectedNicknames) {
        this.selectedNicknames = selectedNicknames;
    }

    void setSelectableColors(List<PrintableColour> selectableColors) {
        this.selectableColors = selectableColors;
    }

    int getPlayersNum() {
        return this.playersNum;
    }

    void setPlayersGods(Map<String, String> playersGods) {
        this.playersGods = playersGods;
    }

    void setPlayersColors(Map<String, PrintableColour> playersColors) {
        this.playersColors = playersColors;
    }


    void setCurrentGamePhase(GamePhase newGamePhase) {
        this.currentGamePhase = newGamePhase;

        switch(this.currentGamePhase) {
            case INITIAL_INFO:
                print("Players are choosing nickname and color... Wait for your turn.");
                break;
            case CHOOSE_GODS:
                print("Players are choosing their gods... Wait for your turn.");
                break;
            case GAME_PREPARATION:
                print("Players are placing their Workers... Wait for your turn.");
                break;
            case REAL_GAME:
                // real game
                break;
        }
    }


    @Override
    public void update(Update update) {
        update.handleUpdate(this.cliUpdateHandler);
    }

    void printBoard(String board) {
        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();
        Board gameBoard = gson.fromJson(board, Board.class);


        char rowIdentifier = 'A';

        if(playerSymbol == null || playerSymbol.size() < playersNum) {
            playerSymbol = mapPlayerIdToSymbol(gameBoard);
        }

        print("");
        print("");
        print("");

        for (int i = 0; i < 5; i++) {    //Single cell printed as 5x5: +---+ board; " "/"1"/"2" if worker is inside; BlockType specified.
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
                        System.out.print(convertColorToAnsi(printableWorker.player.getColor()) + playerSymbol.get(printableWorker.player.ID) + " 1" + PrintableColour.RESET);
                    } else {
                        System.out.print(convertColorToAnsi(printableWorker.player.getColor()) + playerSymbol.get(printableWorker.player.ID) + " 2" + PrintableColour.RESET);
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

    private static Map<String, String> mapPlayerIdToSymbol(Board board) {
        Map<String, String> symbolMap = new HashMap<>();
        String symbol = "";


        for(int i = 0; i < Board.HEIGHT_SIZE; i++) {
            for(int j = 0; j < Board.WIDTH_SIZE; j++) {
                if(!board.getCell(i, j).isEmpty()) {
                    if(!symbolMap.containsKey(board.getCell(i, j).getWorker().player.ID)) {
                        symbol = nextSymbol(symbol);
                        symbolMap.put(board.getCell(i, j).getWorker().player.ID, symbol);
                    }
                }
            }
        }

        return symbolMap;
    }

    private static String nextSymbol(String symbol) {
        switch(symbol) {
            case "":
                return "\u265C";
            case "\u265C":
                return "\u265E";
            case "\u265E":
                return("\u265F");
            default:
                return "";
        }
    }

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

    static String convertColorToAnsi (PrintableColour color) { // todo move to PrintableColor
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

    private void printGodInfo(String god) {
        switch (god) {
            case "apollo":
                print("Name: " + Apollo.NAME);
                print("Description: " + Apollo.DESCRIPTION);
                print("Power: " + Apollo.POWER_DESCRIPTION);
                break;

            case "artemis":
                print("Name: " + Artemis.NAME);
                print("Description: " + Artemis.DESCRIPTION);
                print("Power: " + Artemis.POWER_DESCRIPTION);
                break;

            case "athena":
                print("Name: " + Athena.NAME);
                print("Description: " + Athena.DESCRIPTION);
                print("Power: " + Athena.POWER_DESCRIPTION);
                break;

            case "atlas":
                print("Name: " + Atlas.NAME);
                print("Description: " + Atlas.DESCRIPTION);
                print("Power: " + Atlas.POWER_DESCRIPTION);
                break;

            case "demeter":
                print("Name: " + Demeter.NAME);
                print("Description: " + Demeter.DESCRIPTION);
                print("Power: " + Demeter.POWER_DESCRIPTION);
                break;

            case "eros":
                print("Name: " + Eros.NAME);
                print("Description: " + Eros.DESCRIPTION);
                print("Power: " + Eros.POWER_DESCRIPTION);
                break;

            case "hephaestus":
                print("Name: " + Hephaestus.NAME);
                print("Description: " + Hephaestus.DESCRIPTION);
                print("Power: " + Hephaestus.POWER_DESCRIPTION);
                break;

            case "hera":
                print("Name: " + Hera.NAME);
                print("Description: " + Hera.DESCRIPTION);
                print("Power: " + Hera.POWER_DESCRIPTION);
                break;

            case "hestia":
                print("Name: " + Hestia.NAME);
                print("Description: " + Hestia.DESCRIPTION);
                print("Power: " + Hestia.POWER_DESCRIPTION);
                break;

            case "minotaur":
                print("Name: " + Minotaur.NAME);
                print("Description: " + Minotaur.DESCRIPTION);
                print("Power: " + Minotaur.POWER_DESCRIPTION);
                break;

            case "pan":
                print("Name: " + Pan.NAME);
                print("Description: " + Pan.DESCRIPTION);
                print("Power: " + Pan.POWER_DESCRIPTION);
                break;

            case "poseidon":
                print("Name: " + Poseidon.NAME);
                print("Description: " + Poseidon.DESCRIPTION);
                print("Power: " + Poseidon.POWER_DESCRIPTION);
                break;

            case "prometheus":
                print("Name: " + Prometheus.NAME);
                print("Description: " + Prometheus.DESCRIPTION);
                print("Power: " + Prometheus.POWER_DESCRIPTION);
                break;

            case "zeus":
                print("Name: " + Zeus.NAME);
                print("Description: " + Zeus.DESCRIPTION);
                print("Power: " + Zeus.POWER_DESCRIPTION);
                break;

            default:
                throw new BadCommandException();
        }
    }

    private boolean isValidGod(String god, ArrayList<String> chosenGods) {


        ArrayList<String> validGods = new ArrayList<>(
                Arrays.asList(
                        "apollo",
                        "artemis",
                        "athena",
                        "atlas",
                        "demeter",
                        "hephaestus",
                        "eros",
                        "hera",
                        "hestia",
                        "minotaur",
                        "pan",
                        "poseidon",
                        "prometheus",
                        "zeus"
                ));


        return validGods.contains(god) && (chosenGods == null || !chosenGods.contains(god));
    }

    void printPlayerGods() {
        this.playersGods.keySet().forEach((key) -> {
            print(key + " has " + playersGods.get(key));
        });
    }

    void printPlayersColors() {
        this.playersColors.keySet().forEach((key) -> {
            print(key + " is " + convertColorToAnsi(playersColors.get(key)) + playersColors.get(key) + PrintableColour.RESET);
        });
    }

    /*void handleMatchEnded() {

        String response;
        Byte res;


        do {

            print("Do you want to play another match?");
            res = stdin.nextByte();

            response = res.;

            if(response.equals("yes")) {

                try {
                    client.closeConnection();
                    client.reinitializeConnection();

                    UpdateListener updateListener = new UpdateListener(client.getSocket());
                    new Thread(updateListener).start();
                    updateListener.addObserver(this);

                } catch(IOException e) {
                    e.printStackTrace();
                    System.err.println("The Game couldn't start, maybe there was some network error or the server isn't available.");
                    System.exit(0);
                }

                start();
            }
            else if(response.equals("no")) {
                print("Quitting...");
                System.exit(0);
            }

        } while (!response.equals("yes"));



    }*/


}
