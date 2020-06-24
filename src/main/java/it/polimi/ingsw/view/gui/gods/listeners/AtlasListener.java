package it.polimi.ingsw.view.gui.gods.listeners;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.components.RealGame;
import it.polimi.ingsw.view.gui.ui.JCellButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is a listener used for Atlas. It manages the case in
 * which player using Atlas wants to build a DOME instead of another level.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class AtlasListener extends GodListener implements ActionListener {

    /**
     * This is the creator of the class. At the moment of the creation
     * the realComponent is associated to the class.
     * @param realGameComponent references the RealGameComponent which class refers to.
     */
    public AtlasListener(RealGame realGameComponent) {
        super(realGameComponent);
    }

    /**
     * This method manages the activities when the player with Atlas
     * decides to use Atlas' power to build a dome.
     * @param e contains a reference to the event of the listener.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JCellButton selectedWorkerButton = realGameComponent.getSelectedWorker();
        JCellButton selectedCellButton = realGameComponent.getSelectedCellButton();

        if(selectedWorkerButton == null || selectedCellButton == null) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "You must select a Worker and a Board Cell!", "Error", JOptionPane.ERROR_MESSAGE));
        }
        else {

            PlayerCommand playerCommand = new PlayerCommand(CommandType.BUILD, selectedWorkerButton.getWorker().workerType, selectedCellButton.getRow(), selectedCellButton.getCol(), BlockType.DOME);
            Gui.getInstance().notify(playerCommand);

            this.realGameComponent.setLastCommand(CommandType.BUILD);

        }
    }
}
