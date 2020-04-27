package it.polimi.ingsw.view.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.gods.*;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.UpdateHandler;

import java.io.PrintStream;
import java.util.*;

public class Cli extends Observable<Object> implements Observer<Update> {
    private Client client;
    private String nickname;
    private int playersNum;
    private Scanner stdin;
    private PrintStream stdout;
    private boolean enableGodChoose = false;
    private boolean isInitialGodChooser = false;
    private boolean enableGamePreparation = false;
    private boolean enableGameCommands = false;

    private List<String> selectableGods;
    private Map<String, String> playersGods;

    private UpdateHandler cliUpdateHandler; //CliUpdateHandler

    public Cli(Client client) {
        this.client = client;
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

            print("Type a nickname: ");
            nickname = stdin.nextLine();
            client.sendString(nickname);

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
            command = stdin.nextLine().toLowerCase();

            String[] splitCommand = command.split("\\s+");

            if ((splitCommand.length == 0) || (splitCommand[0].equals("") && splitCommand.length == 1)) {
                continue;
            }

            if (splitCommand[0].length() == 0) {  // command starting with space
                splitCommand = Arrays.copyOfRange(splitCommand, 1, splitCommand.length);
            }

            try {
                if (CommandType.parseCommandType(splitCommand[0]) == CommandType.HELP && (splitCommand.length == 1)) {
                    print("help -> print command list and tutorial");
                    print("info <god> -> get info about a god");
                    print("build w1/w2 [letter, number] [optional: blockType {one, two, three, dome}] -> tries to build with the chosen Worker in the specified position");
                    print("move  w1/w2 [letter, number] -> tries to move with the chosen Worker to the specified position\"");
                    print("end -> tries to end the current turn");
                    print("Info Example : info Apollo");
                    print("Move Example : move w1 a1");
                    print("Build Example: build w1 a2 dome");
                } else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.INFO) {
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
                } else if (enableGodChoose && isInitialGodChooser) {

                    if (CommandType.parseCommandType(splitCommand[0]) != CommandType.SELECT || splitCommand.length > playersNum + 1) {
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

                    enableGodChoose = false;

                    GodChoiceCommand godChoiceCommand = new GodChoiceCommand(chosenGods, true);
                    notify(godChoiceCommand);
                } else if (enableGodChoose) { // not initial god chooser

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
                } else if (enableGamePreparation) {

                    GamePreparationCommand gamePreparationCommand = GamePreparationCommand.parseInput(command);
                    notify(gamePreparationCommand);

                } else if (enableGameCommands) {

                    PlayerCommand playerCommand = PlayerCommand.parseInput(command);
                    notify(playerCommand);

                } else {
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

    void setEnableGamePreparation(boolean value) {
        this.enableGamePreparation = value;
    }

    void setEnableGameCommands(boolean value) {
        this.enableGameCommands = value;
    }

    void setEnableGodChoose(boolean value) {
        this.enableGodChoose = value;
    }

    void setInitialGodChooser(boolean value) {
        this.isInitialGodChooser = value;
    }

    void setSelectableGods(List<String> selectableGods) {
        this.selectableGods = selectableGods;
    }

    List<String> getSelectableGods() {
        return this.selectableGods;
    }

    int getPlayersNum() {
        return this.playersNum;
    }

    void setPlayersGods(Map<String, String> playersGods) {
        this.playersGods = playersGods;
    }


    @Override
    public void update(Update update) {
        update.handleUpdate(this.cliUpdateHandler);
    }

    void printBoard(String board) {
        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();
        Board gameBoard = gson.fromJson(board, Board.class); // TODO Board Drawing in CLI

        print(board);
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


}

