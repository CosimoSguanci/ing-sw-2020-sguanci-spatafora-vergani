package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;

/**
 * Server main class, the class which starts the backend of the game in the command line.
 */
public class ServerApp {
    public static void main(String[] args) {
        try {

            Server server = new Server();
            Server.setIsActive(true);
            server.runServer();

        } catch (Exception e) {
            System.err.println("Uncaught Exception: " + e.getMessage());
            Server.setIsActive(false);
            System.exit(0);
        }
    }
}