package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.MessageListener;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.cli.Cli;

public class ClientApp
{
    public static void main(String[] args) throws Exception{
        Client client = new Client();
        MessageListener messageListener = new MessageListener(client.getSocket());
        new Thread(messageListener).start();
        Controller controller = new Controller(client);
        Cli cli = new Cli(client);
        messageListener.addObserver(cli);
        cli.addObserver(controller);
        cli.start();
    }
}