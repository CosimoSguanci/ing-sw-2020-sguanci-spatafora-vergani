package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.messages.ErrorUpdate;
import it.polimi.ingsw.model.messages.ModelUpdate;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messages.TurnUpdate;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.Request;

import java.util.Scanner;

public class Cli extends Observable<Object> implements Observer<Object> {
    private Client client;
    private String nickname;
    private Scanner stdin;

    private Player player;

    public Cli(Client client) {
        this.client = client;
    }

    public void start() {
        stdin = new Scanner(System.in);

        try {
            client.initConnection();

            System.out.println("Type a nickname: ");
            nickname = stdin.nextLine();
            client.sendString(nickname);

            String command;
            while(true) {
                command = stdin.nextLine();

                if(command.toLowerCase().equals("help")) {
                    System.out.println(
                            "help -> print command format\n" +
                                    "        build w1/w2 [lettera, numero] [optional: blockType {one, two, three, dome} \n" +
                                    "        move  w1/w2 [lettera, numero]\n" +
                                    "        end turn"
                    );
                }

            }

        } catch (Exception e) {
            System.out.println("Network error");
        }

    }


    @Override
    public void update(Object message) {
        /*if(message instanceof Request) {
            Request req = (Request) message;

            switch(req) {
                case ASK_PLAYER_NUM:
                    try {
                        System.out.println("Player Number: ");
                        String playerNum = stdin.nextLine();
                        client.sendString(playerNum);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }*/

        if(message instanceof ModelUpdate) {
            ModelUpdate modelUpdate = (ModelUpdate) message;
            printBoard(modelUpdate.getBoard());
        }

        else if(message instanceof ErrorUpdate) {
            ErrorUpdate errorUpdate = (ErrorUpdate) message;
            switch (errorUpdate.getCommand()) {
                case MOVE:
                    System.out.println("Move Error");
                    break;

                case BUILD:
                    System.out.println("Build Error");
                    break;
            }
        }

        else
            notify(message);
    }


    private void printBoard(Board board) {
        ///
    }






}
