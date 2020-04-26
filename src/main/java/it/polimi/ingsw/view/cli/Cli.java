package it.polimi.ingsw.view.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.gods.*;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

import java.util.*;

public class Cli extends Observable<Object> implements Observer<Object> {
    private Client client;
    private String nickname;
    private int playersNum;
    private Scanner stdin;
    private boolean enableGodChoose = false;
    private boolean isInitialGodChooser = false;
    private boolean enableGamePreparation = false;
    private boolean enableGameCommands = false;

    private List<String> selectableGods;
    private Map<String, String> playersGods;

    public Cli(Client client) {
        this.client = client;
    }

    public void start() {
        stdin = new Scanner(System.in);

        try {
           // client.initConnection();

            System.out.println("Type a nickname: ");
            nickname = stdin.nextLine();
            client.sendString(nickname);

            System.out.println("How many players do you want to play with? ");
            playersNum = stdin.nextInt();
            client.sendInt(playersNum);


            //gameManager.run();

            gameLoop();



        } catch (Exception e) {
            System.out.println("Network error");
        }

    }

    private void gameLoop() {
        System.out.println("Waiting for a match...");

        String command;

        while(true) { // TODO handle bad command
            command = stdin.nextLine().toLowerCase();

            if (command.toLowerCase().equals("help")) {
                System.out.println(
                        "help -> print command format\n" +
                                "        build w1/w2 [lettera, numero] [optional: blockType {one, two, three, dome} \n" +
                                "        move  w1/w2 [lettera, numero]\n" +
                                "        end turn"
                );
            }

            else if(command.toLowerCase().contains("info")) {
                // provo a parsare info
                String[] s = command.split("\\s+");

                if(!s[0].toLowerCase().equals("info") || s.length > 2)
                    throw new BadCommandException();

                String god = s[1];

                printGodInfo(god);

            }
            else
                if(enableGodChoose && isInitialGodChooser) {
                    String[] s = command.split("\\s+");

                    if(!s[0].equals("select") || s.length > playersNum + 1) {
                        throw new BadCommandException();
                    }

                    ArrayList<String> chosenGods = new ArrayList<>();

                    for(int i = 0; i < playersNum; i++) {
                        String god = s[i+1].toLowerCase();

                        if(!isValidGod(god, chosenGods)) {
                            throw new BadCommandException();
                        }

                        chosenGods.add(god);
                    }

                    enableGodChoose =  false;

                    GodChoiceCommand godChoiceCommand = new GodChoiceCommand(chosenGods, true);
                    notify(godChoiceCommand);

                }
                else if(enableGodChoose) { // not initial god chooser
                    String[] s = command.split("\\s+");

                    if(!s[0].equals("select") || s.length > 2) {
                        throw new BadCommandException();
                    }

                    String god = s[1];

                    if(!selectableGods.contains(god)) {
                        throw new BadCommandException();
                    }

                    ArrayList<String> selected = new ArrayList<>();
                    selected.add(god);
                    GodChoiceCommand godChoiceCommand = new GodChoiceCommand(selected, false);
                    notify(godChoiceCommand);
                }
                else if(enableGamePreparation) {
                     // TODO toLowerCase EVERYWHERE

                    GamePreparationCommand gamePreparationCommand = GamePreparationCommand.parseInput(command);
                    notify(gamePreparationCommand);
                }



            else if(enableGameCommands){
                try {
                    PlayerCommand playerCommand = PlayerCommand.parseInput(command);

                    notify(playerCommand);


                } catch(BadCommandException e) {
                    System.out.println("Bad command");
                }
            }
            else if (!command.equals("")){ // TODO FIX
                System.out.println("Wrong Command");
            }
        }
    }




    @Override
    public void update(Object message) {

        if(message instanceof MatchStartedUpdate) {
            MatchStartedUpdate matchStartedUpdate = (MatchStartedUpdate) message;
            System.out.println("Match Started");
            printBoard(matchStartedUpdate.board);
            enableGamePreparation = false;
            enableGameCommands = true;
        }
        else if (message instanceof ChooseGodsUpdate) {
            ChooseGodsUpdate chooseGodsUpdate = (ChooseGodsUpdate) message;
            enableGodChoose = true;

            if(chooseGodsUpdate.isGodChooser) {
                isInitialGodChooser = true;
                System.out.println("Choose " + playersNum + " gods...");
            }
            else {
                isInitialGodChooser = false;
                selectableGods = chooseGodsUpdate.selectableGods;
                System.out.println("Choose your god. Available choices are: ");

                selectableGods.forEach(System.out::println);
            }

        }
        else if (message instanceof SelectedGodsUpdate) {
            SelectedGodsUpdate selectedGodsUpdate = (SelectedGodsUpdate) message;
            this.playersGods = selectedGodsUpdate.selectedGods;
            printPlayerGods();
        }
        else if (message instanceof GamePreparationUpdate) {
            //GamePreparationUpdate gamePreparationUpdate = (GamePreparationUpdate) message;
            enableGodChoose = false;
            enableGamePreparation = true;
            System.out.println("Game Preparation: place your workers ");

        }
        else if (message instanceof BoardUpdate) {
            BoardUpdate boardUpdate = (BoardUpdate) message;
            printBoard(boardUpdate.board);
        } else if (message instanceof ErrorUpdate) {
            ErrorUpdate errorUpdate = (ErrorUpdate) message;
            switch (errorUpdate.command) {
                case MOVE:
                    System.out.println("Move Error");
                    break;

                case BUILD:
                    System.out.println("Build Error");
                    break;
            }
        } else
            notify(message);
    }


    private void printBoard(String board) {
        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();
        Board gameBoard = gson.fromJson(board, Board.class);

        System.out.println(board);
    }

    private void printGodInfo(String god) {
        switch(god) {
            case "apollo":
                System.out.println("Name: " + Apollo.NAME);
                System.out.println("Description: " + Apollo.DESCRIPTION);
                System.out.println("Power: " + Apollo.POWER_DESCRIPTION);
                break;

            case "artemis":
                System.out.println("Name: " + Artemis.NAME);
                System.out.println("Description: " + Artemis.DESCRIPTION);
                System.out.println("Power: " + Artemis.POWER_DESCRIPTION);
                break;

            case "athena":
                System.out.println("Name: " + Athena.NAME);
                System.out.println("Description: " + Athena.DESCRIPTION);
                System.out.println("Power: " + Athena.POWER_DESCRIPTION);
                break;

            case "atlas":
                System.out.println("Name: " + Atlas.NAME);
                System.out.println("Description: " + Atlas.DESCRIPTION);
                System.out.println("Power: " + Atlas.POWER_DESCRIPTION);
                break;

            case "demeter":
                System.out.println("Name: " + Demeter.NAME);
                System.out.println("Description: " + Demeter.DESCRIPTION);
                System.out.println("Power: " + Demeter.POWER_DESCRIPTION);
                break;

            case "eros":
                System.out.println("Name: " + Eros.NAME);
                System.out.println("Description: " + Eros.DESCRIPTION);
                System.out.println("Power: " + Eros.POWER_DESCRIPTION);
                break;

            case "hephaestus":
                System.out.println("Name: " + Hephaestus.NAME);
                System.out.println("Description: " + Hephaestus.DESCRIPTION);
                System.out.println("Power: " + Hephaestus.POWER_DESCRIPTION);
                break;

            case "hera":
                System.out.println("Name: " + Hera.NAME);
                System.out.println("Description: " + Hera.DESCRIPTION);
                System.out.println("Power: " + Hera.POWER_DESCRIPTION);
                break;

            case "hestia":
                System.out.println("Name: " + Hestia.NAME);
                System.out.println("Description: " + Hestia.DESCRIPTION);
                System.out.println("Power: " + Hestia.POWER_DESCRIPTION);
                break;

            case "minotaur":
                System.out.println("Name: " + Minotaur.NAME);
                System.out.println("Description: " + Minotaur.DESCRIPTION);
                System.out.println("Power: " + Minotaur.POWER_DESCRIPTION);
                break;

            case "pan":
                System.out.println("Name: " + Pan.NAME);
                System.out.println("Description: " + Pan.DESCRIPTION);
                System.out.println("Power: " + Pan.POWER_DESCRIPTION);
                break;

            case "poseidon":
                System.out.println("Name: " + Poseidon.NAME);
                System.out.println("Description: " + Poseidon.DESCRIPTION);
                System.out.println("Power: " + Poseidon.POWER_DESCRIPTION);
                break;

            case "prometheus":
                System.out.println("Name: " + Prometheus.NAME);
                System.out.println("Description: " + Prometheus.DESCRIPTION);
                System.out.println("Power: " + Prometheus.POWER_DESCRIPTION);
                break;

            case "zeus":
                System.out.println("Name: " + Zeus.NAME);
                System.out.println("Description: " + Zeus.DESCRIPTION);
                System.out.println("Power: " + Zeus.POWER_DESCRIPTION);
                break;

            default: throw new BadCommandException();
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

    private void printPlayerGods() {
        this.playersGods.keySet().forEach((key) -> {
            System.out.println(key + " has " + playersGods.get(key));
        });
    }


}


/*
{"board":[[{"level":"GROUND","rowIdentifier":0,"colIdentifier":0},{"level":"GROUND","rowIdentifier":0,"colIdentifier":1},{"level":"GROUND","rowIdentifier":0,"colIdentifier":2},{"level":"GROUND","rowIdentifier":0,"colIdentifier":3},{"level":"GROUND","rowIdentifier":0,"colIdentifier":4}],[{"level":"GROUND","rowIdentifier":1,"colIdentifier":0},{"level":"GROUND","rowIdentifier":1,"colIdentifier":1},{"level":"GROUND","rowIdentifier":1,"colIdentifier":2},{"level":"GROUND","rowIdentifier":1,"colIdentifier":3},{"level":"GROUND","rowIdentifier":1,"colIdentifier":4}],[{"level":"GROUND","rowIdentifier":2,"colIdentifier":0},{"level":"GROUND","rowIdentifier":2,"colIdentifier":1},{"level":"GROUND","rowIdentifier":2,"colIdentifier":2},{"level":"GROUND","rowIdentifier":2,"colIdentifier":3},{"level":"GROUND","rowIdentifier":2,"colIdentifier":4}],[{"level":"GROUND","rowIdentifier":3,"colIdentifier":0},{"level":"GROUND","rowIdentifier":3,"colIdentifier":1},{"level":"GROUND","rowIdentifier":3,"colIdentifier":2},{"level":"GROUND","rowIdentifier":3,"colIdentifier":3},{"level":"GROUND","rowIdentifier":3,"colIdentifier":4}],[{"level":"GROUND","rowIdentifier":4,"colIdentifier":0},{"level":"GROUND","rowIdentifier":4,"colIdentifier":1},{"level":"GROUND","rowIdentifier":4,"colIdentifier":2},{"level":"GROUND","rowIdentifier":4,"colIdentifier":3},{"level":"GROUND","rowIdentifier":4,"colIdentifier":4}]],"id":"15"}
 */
