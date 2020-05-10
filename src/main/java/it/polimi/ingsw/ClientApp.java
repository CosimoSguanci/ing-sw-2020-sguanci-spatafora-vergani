package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.UpdateListener;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.cli.Cli;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) {
        try {

            Client client = new Client();
            UpdateListener updateListener = new UpdateListener(client.getSocket());
            new Thread(updateListener).start();
            Controller controller = new Controller(client);
            Cli cli = new Cli(client, controller);
            updateListener.addObserver(cli);
            cli.addObserver(controller);
            controller.addObserver(cli);
            cli.start();

        } catch(IOException e) {
            System.err.println("The Game couldn't start, maybe there was some network error or the server isn't available.");
        }
    }
}