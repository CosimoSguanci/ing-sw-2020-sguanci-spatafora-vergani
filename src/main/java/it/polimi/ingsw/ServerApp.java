package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;

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
