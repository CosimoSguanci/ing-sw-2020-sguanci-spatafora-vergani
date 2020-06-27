package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.components.RealGame;
import it.polimi.ingsw.view.gui.ui.JCellButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is the listener of the BuildButton
 * which is constantly present in RealGame phase
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class BuildButtonListener implements ActionListener {

    private final RealGame realGameComponent;

    /**
     * This is the constructor of the class. At the moment of creation
     * an association between the listener and the RealGame component is made.
     * @param realGameComponent contains a reference to the RealGame component.
     */
    public BuildButtonListener(RealGame realGameComponent) {
        this.realGameComponent = realGameComponent;
    }

    /**
     * This method is invoked at the moment of clicking on the BuildButton.
     * When the BuildButton is clicked it generates a new build PlayerCommand.
     * @param e contains a reference to the BuildButton
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JCellButton selectedWorkerButton = this.realGameComponent.getSelectedWorker();
        JCellButton selectedCellButton = this.realGameComponent.getSelectedCellButton();

        if (selectedWorkerButton == null || selectedCellButton == null) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "You must select a Worker and a Board Cell!", "Error", JOptionPane.ERROR_MESSAGE));
        } else {

            PlayerCommand playerCommand = new PlayerCommand(CommandType.BUILD, selectedWorkerButton.getWorker().workerType, selectedCellButton.getRow(), selectedCellButton.getCol(), null);
            Gui.getInstance().notify(playerCommand);

            this.realGameComponent.setLastCommand(CommandType.BUILD);

        }
    }
}
