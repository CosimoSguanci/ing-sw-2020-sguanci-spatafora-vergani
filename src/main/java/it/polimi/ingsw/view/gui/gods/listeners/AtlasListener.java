package it.polimi.ingsw.view.gui.gods.listeners;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.components.RealGame;
import it.polimi.ingsw.view.gui.ui.JCellButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AtlasListener extends GodListener implements ActionListener {

    public AtlasListener(RealGame realGameComponent) {
        super(realGameComponent);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JCellButton selectedWorkerButton = realGameComponent.getSelectedWorker();
        JCellButton selectedCellButton = realGameComponent.getSelectedCellButton();

        if(selectedWorkerButton == null || selectedCellButton == null) {
            // todo add JDialog
        }
        else {

            PlayerCommand playerCommand = new PlayerCommand(CommandType.BUILD, selectedWorkerButton.getWorker().workerType, selectedCellButton.getRow(), selectedCellButton.getCol(), BlockType.DOME);
            Gui.getInstance().notify(playerCommand);

            this.realGameComponent.setLastCommand(CommandType.BUILD);

        }
    }
}
