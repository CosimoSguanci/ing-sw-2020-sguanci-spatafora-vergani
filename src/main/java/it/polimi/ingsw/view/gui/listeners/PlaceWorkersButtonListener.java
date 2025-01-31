package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.components.GamePreparation;
import it.polimi.ingsw.view.gui.ui.JCellButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * This class is the listener of the PlaceWorkersButton.
 * During GamePreparation phase a new button is available to continue
 * Players are asked to place their worker accordingly to their Gods Power
 * and once finished they can click on the button of which this class is the listener.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class PlaceWorkersButtonListener implements ActionListener {

    private static final String errorDialogTitle = "Error";
    private static final String errorDialogMessage = "You must place 2 Workers";

    private final GamePreparation gamePreparationComponent;

    /**
     * This is the creator of the class. At the moment of creation an association
     * between the listener and the gamePreparation component is made.
     *
     * @param gamePreparationComponent contains a reference to the component
     */
    public PlaceWorkersButtonListener(GamePreparation gamePreparationComponent) {
        this.gamePreparationComponent = gamePreparationComponent;
    }

    /**
     * This method is invoked when the continue button of the gamePreparation component is clicked.
     *
     * @param e contains a reference to the continue button.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        List<JCellButton> selectedButtons = gamePreparationComponent.getSelectedButtons();

        if (selectedButtons.size() == 2) {
            int workerFirstRow = selectedButtons.get(0).getRow();
            int workerFirstCol = selectedButtons.get(0).getCol();

            int workerSecondRow = selectedButtons.get(1).getRow();
            int workerSecondCol = selectedButtons.get(1).getCol();

            GamePreparationCommand gamePreparationCommand = new GamePreparationCommand(workerFirstRow, workerFirstCol, workerSecondRow, workerSecondCol);
            Gui.getInstance().notify(gamePreparationCommand);
        } else {
            JOptionPane.showMessageDialog(Gui.getInstance().getMainFrame(), errorDialogMessage, errorDialogTitle, JOptionPane.ERROR_MESSAGE);
        }
    }
}
