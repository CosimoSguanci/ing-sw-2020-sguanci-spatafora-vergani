package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;

public class ServerApp {
    public static void main(String[] args) {
        try {
            Server server = new Server();
            Server.setIsActive(true);
            server.run();
        } catch (Exception e) {
            System.err.println("Uncaught Exception: " + e.getMessage());
            Server.setIsActive(false);
            System.exit(0);
        }
    }
}
