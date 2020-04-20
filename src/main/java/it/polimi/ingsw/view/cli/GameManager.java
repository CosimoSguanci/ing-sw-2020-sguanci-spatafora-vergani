package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.controller.PlayerCommand;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observer.Observable;

import java.util.Scanner;

public class GameManager extends Observable<Object> {

    private Client client;
    private Scanner stdin;

    public GameManager(Client client) {
        this.client = client;
        this.stdin = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Waiting for a match...");

        String command;

        while(true) {
            command = stdin.nextLine();

            if (command.toLowerCase().equals("help")) {
                System.out.println(
                        "help -> print command format\n" +
                                "        build w1/w2 [lettera, numero] [optional: blockType {one, two, three, dome} \n" +
                                "        move  w1/w2 [lettera, numero]\n" +
                                "        end turn"
                );
            }

            else {
                try {
                    PlayerCommand playerCommand = PlayerCommand.parseInput(null, command);
                    notify(playerCommand);
                } catch(Exception e) {
                    e.printStackTrace();
                    System.out.println("Bad command");
                }
            }
        }
    }
}
