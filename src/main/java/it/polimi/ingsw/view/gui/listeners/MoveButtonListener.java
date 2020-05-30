package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.components.RealGame;
import it.polimi.ingsw.view.gui.ui.BackgroundButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MoveButtonListener implements ActionListener {

    private RealGame realGameComponent;

    public MoveButtonListener(RealGame realGameComponent) {
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

            PlayerCommand playerCommand = new PlayerCommand(CommandType.MOVE, selectedWorkerButton.getWorker().workerType, selectedCellButton.getRow(), selectedCellButton.getCol(), null);
            Gui.getInstance().notify(playerCommand);

            this.realGameComponent.setLastCommand(CommandType.MOVE);

        }
    }
}
