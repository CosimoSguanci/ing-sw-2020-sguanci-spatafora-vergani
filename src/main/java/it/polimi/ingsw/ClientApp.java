package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.controller.PlayerCommand;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.cli.Cli;

public class ClientApp
{
    public static void main(String[] args) throws Exception{
        /*Client client = new Client();
        Controller controller = new Controller(client);
        Cli cli = new Cli(client);
        cli.addObserver(controller);
        cli.start();*/

        PlayerCommand c = PlayerCommand.parseInput(new Player("", "", null), "move       w1 c5 dome");

        System.out.println("");
    }
}