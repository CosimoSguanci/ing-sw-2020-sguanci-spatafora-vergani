package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.view.cli.Cli;

public class ServerApp {
    public static void main(String[] args){

        try {
            Server server = new Server();
            server.run();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
}
