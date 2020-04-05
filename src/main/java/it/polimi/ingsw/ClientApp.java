package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.view.cli.Cli;

public class ClientApp
{
    public static void main(String[] args){
        Client client = new Client();
        Cli cli = new Cli(client);
        cli.start();
    }
}