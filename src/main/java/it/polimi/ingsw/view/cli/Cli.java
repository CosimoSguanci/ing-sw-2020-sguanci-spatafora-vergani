package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.messages.ErrorUpdate;
import it.polimi.ingsw.model.messages.MatchStartedUpdate;
import it.polimi.ingsw.model.messages.ModelUpdate;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messages.TurnUpdate;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.Request;

import java.util.Scanner;

public class Cli extends Observable<Object> implements Observer<Object> {
    private Client client;
    private GameManager gameManager;
    private String nickname;
    private int playersNum;
    private Scanner stdin;

    public Cli(Client client, GameManager gameManager) {
        this.client = client;
        this.gameManager = gameManager;
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


            gameManager.run();


        } catch (Exception e) {
            System.out.println("Network error");
        }

    }




    @Override
    public void update(Object message) {

        if(message instanceof MatchStartedUpdate) {
            MatchStartedUpdate matchStartedUpdate = (MatchStartedUpdate) message;
            System.out.println("Match Started");
            printBoard(matchStartedUpdate.getBoard());
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


}
