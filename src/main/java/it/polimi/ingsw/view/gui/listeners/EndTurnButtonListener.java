package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.components.RealGame;
import it.polimi.ingsw.view.gui.ui.JCellButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndTurnButtonListener implements ActionListener {

    private RealGame realGameComponent;

    public EndTurnButtonListener(RealGame realGameComponent) {
        this.realGameComponent = realGameComponent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JCellButton selectedWorkerButton = realGameComponent.getSelectedWorker();
        JCellButton selectedCellButton = realGameComponent.getSelectedCellButton();

        if(selectedWorkerButton == null || selectedCellButton == null) {
            // todo add JDialog
        }
        else {
            PlayerCommand playerCommand = new PlayerCommand(CommandType.END_TURN, null, -1, -1, null);
            Gui.getInstance().notify(playerCommand);
            this.realGameComponent.setLastCommand(CommandType.END_TURN);
        }
    }
}
