package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.network.client.controller.Controller;

import javax.swing.*;

public abstract class AbstractInitialChoice extends JPanel {
    private final Controller controller;

    AbstractInitialChoice(Controller controller) {
        this.controller = controller;
    }

    public final void onTurnChanged() {
        if(controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) {
            this.showGuiOnTurn();
        }
    }

    public abstract void showGuiOnTurn();

}
