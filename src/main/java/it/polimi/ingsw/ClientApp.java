package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.MessageListener;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.view.cli.Cli;

public class ClientApp
{
    public static void main(String[] args) throws Exception{
        Client client = new Client();
        MessageListener messageListener = new MessageListener(client.getSocket());
        new Thread(messageListener).start();
        Controller controller = new Controller(client);
        //GameManager gameManager = new GameManager(client);
        Cli cli = new Cli(client);
        messageListener.addObserver(cli);
        cli.addObserver(controller);
        //gameManager.addObserver(controller);
        cli.start();
    }
}