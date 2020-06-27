package it.polimi.ingsw.view.gui.gods.listeners;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.components.RealGame;
import it.polimi.ingsw.view.gui.ui.JCellButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is a listener used for Zeus. It manages the case in
 * which player using Zeus wants to build a level under himself
 * rather than in an adjacent cell.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class ZeusListener extends GodListener implements ActionListener {

    /**
     * This is the creator of the class. At the moment of the creation
     * the realComponent is associated to the class.
     * @param realGameComponent references the RealGameComponent which class refers to.
     */
    public ZeusListener(RealGame realGameComponent) {
        super(realGameComponent);
    }

    /**
     * This method manages the activities when the player with Zeus
     * decides to use Zeus' power to build a level under himself.
     * @param e contains a reference to the event of the listener.
     */
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
