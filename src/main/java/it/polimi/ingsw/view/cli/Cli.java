package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.controller.GodChoiceCommand;
import it.polimi.ingsw.controller.PlayerCommand;
import it.polimi.ingsw.exceptions.BadPlayerCommandException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.gods.Apollo;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

import java.util.*;

public class Cli extends Observable<Object> implements Observer<Object> {
    private Client client;
    //private GameManager gameManager;
    private String nickname;
    private int playersNum;
    private Scanner stdin;
    private boolean enableGodChoose = false;
    private boolean isInitialGodChooser = false;
    private boolean enableGameCommands = false;
    private List<String> selectableGods;
    private Map<String, String> playersGods;

    public Cli(Client client) {
        this.client = client;
        //this.gameManager = gameManager;
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

        while(true) {
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
                    throw new BadPlayerCommandException();

                String god = s[1];

                printGodInfo(god);

            }
            else
                if(enableGodChoose && isInitialGodChooser) {
                    String[] s = command.split("\\s+");

                    if(!s[0].equals("select") || s.length > playersNum + 1) {
                        throw new BadPlayerCommandException();
                    }

                    ArrayList<String> chosenGods = new ArrayList<>();

                    for(int i = 0; i < playersNum; i++) {
                        String god = s[i+1].toLowerCase();

                        if(!isValidGod(god, chosenGods)) {
                            throw new BadPlayerCommandException();
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
                        throw new BadPlayerCommandException();
                    }

                    String god = s[1];

                    if(!selectableGods.contains(god)) {
                        throw new BadPlayerCommandException();
                    }

                    ArrayList<String> selected = new ArrayList<>();
                    selected.add(god);
                    GodChoiceCommand godChoiceCommand = new GodChoiceCommand(selected, false);
                    notify(godChoiceCommand);
                }



            else {
                try {
                    PlayerCommand playerCommand = PlayerCommand.parseInput(null, command);

                    notify(playerCommand);


                } catch(BadPlayerCommandException e) {
                    System.out.println("Bad command");
                }
            }
        }
    }




    @Override
    public void update(Object message) {

        if(message instanceof MatchStartedUpdate) {
            MatchStartedUpdate matchStartedUpdate = (MatchStartedUpdate) message;
            System.out.println("Match Started");
            printBoard(matchStartedUpdate.getBoard());
        }
        else if (message instanceof ChooseGodsUpdate) {
            ChooseGodsUpdate chooseGodsUpdate = (ChooseGodsUpdate) message;
            enableGodChoose = true;

            if(chooseGodsUpdate.isGodChooser()) {
                isInitialGodChooser = true;
                System.out.println("Choose " + playersNum + " gods...");
            }
            else {
                isInitialGodChooser = false;
                selectableGods = chooseGodsUpdate.getSelectableGods();
                System.out.println("Choose your god. Available choices are: ");

                selectableGods.forEach(System.out::println); // TOP!
            }

        }
        else if (message instanceof SelectedGodsUpdate) {
            SelectedGodsUpdate selectedGodsUpdate = (SelectedGodsUpdate) message;
            this.playersGods = selectedGodsUpdate.getSelectedGods();
            printPlayerGods();
        }
        else if (message instanceof ModelUpdate) {
            ModelUpdate modelUpdate = (ModelUpdate) message;
            printBoard(modelUpdate.getBoard());
        } else if (message instanceof ErrorUpdate) {
            ErrorUpdate errorUpdate = (ErrorUpdate) message;
            switch (errorUpdate.getCommand()) {
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


    private void printBoard(Board board) {
        ///
    }

    private void printGodInfo(String god) {
        switch(god) {
            case "apollo":
               System.out.println("Name: " + Apollo.NAME);
               System.out.println("Description: " + Apollo.DESCRIPTION);
               System.out.println("Power: " + Apollo.POWER_DESCRIPTION);



                break;
            // todo add other gods
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
