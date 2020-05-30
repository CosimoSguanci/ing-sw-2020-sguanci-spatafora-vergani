package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.components.RealGame;
import it.polimi.ingsw.view.gui.ui.BackgroundButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuildButtonListener implements ActionListener {

    private RealGame realGameComponent;

    public BuildButtonListener(RealGame realGameComponent) {
        this.realGameComponent = realGameComponent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        BackgroundButton selectedWorkerButton = realGameComponent.getSelectedWorker();
        BackgroundButton selectedCellButton = realGameComponent.getSelectedCellButton();

        if(selectedWorkerButton == null || selectedCellButton == null) {
            // todo add JDialog
        }
        else {

            PlayerCommand playerCommand = new PlayerCommand(CommandType.BUILD, selectedWorkerButton.getWorker().workerType, selectedCellButton.getRow(), selectedCellButton.getCol(), null);
            Gui.getInstance().notify(playerCommand);

            this.realGameComponent.setLastCommand(CommandType.BUILD);

        }
    }
}
