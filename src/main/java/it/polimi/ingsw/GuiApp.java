package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.gui.ConnectionError;
import it.polimi.ingsw.view.gui.Gui;

/**
 * GUI main class, the class which starts the game in the graphic environment.
 */
public class GuiApp {
    public static void main(String[] args) {
        try {


            Client client = new Client();
            Controller controller = new Controller(client);
            Gui gui = Gui.getInstance(client, controller);
            gui.addObserver(controller);
            controller.addObserver(gui);
            gui.start();

        } catch (Exception e) { // IOException

            new ConnectionError().show();
        }
    }
}