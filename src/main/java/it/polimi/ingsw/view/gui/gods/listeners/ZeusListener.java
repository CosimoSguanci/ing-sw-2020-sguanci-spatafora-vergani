package it.polimi.ingsw.view.gui.gods.listeners;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.components.RealGame;
import it.polimi.ingsw.view.gui.ui.JCellButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ZeusListener extends GodListener implements ActionListener {

    public ZeusListener(RealGame realGameComponent) {
        super(realGameComponent);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JCellButton selectedWorkerButton = realGameComponent.getSelectedWorker();

        if(selectedWorkerButton == null) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "You must select a Worker!", "Error", JOptionPane.ERROR_MESSAGE));
        }
        else {

            PlayerCommand playerCommand = new PlayerCommand(CommandType.BUILD, selectedWorkerButton.getWorker().workerType, selectedWorkerButton.getRow(), selectedWorkerButton.getCol(), null);
            Gui.getInstance().notify(playerCommand);

            this.realGameComponent.setLastCommand(CommandType.BUILD);

        }
    }
}
