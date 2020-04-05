package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.client.Client;

import java.util.Scanner;

public class Cli {
    private Client client;
    private String nickname;

    public Cli(Client client) {
        this.client = client;
    }

    public void start() {
        Scanner stdin = new Scanner(System.in);

        try {
            client.initConnection();

            System.out.println("Type a nickname: ");
            nickname = stdin.nextLine();
            client.sendString(nickname);

        } catch (Exception e) {
            System.out.println("Network error");
        }

    }
}
