package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.cli.Cli;

public class CliApp {
    public static void main(String[] args) {
        try {

            Client client = new Client();
            Controller controller = new Controller(client);
            Cli cli = new Cli(client, controller);
            cli.addObserver(controller);
            controller.addObserver(cli);
            cli.start();

        } catch (Exception e) { // IOException
            System.err.println("The Game couldn't start, maybe there was some network error " +
                    "or the server isn't available.");
        }
    }
}