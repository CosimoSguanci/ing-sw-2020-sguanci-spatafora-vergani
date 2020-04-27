package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.ObjectListenerDelegate;
import it.polimi.ingsw.network.client.UpdateListener;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.cli.Cli;

public class ClientApp
{
    public static void main(String[] args) throws Exception{
        Client client = new Client();
        UpdateListener updateListener = new UpdateListener(client.getSocket());
        new Thread(updateListener).start();
        Controller controller = new Controller(client);
        Cli cli = new Cli(client);
        updateListener.addObserver(cli);
        cli.addObserver(controller);
        cli.start();
    }
}