package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.components.GamePreparation;
import it.polimi.ingsw.view.gui.ui.BackgroundButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PlaceWorkersButtonListener implements ActionListener {

    private static final String errorDialogTitle = "Error";
    private static final String errorDialogMessage = "You must place 2 Workers";

    private GamePreparation gamePreparationComponent;

    public PlaceWorkersButtonListener(GamePreparation gamePreparationComponent) {
        this.gamePreparationComponent = gamePreparationComponent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        List<BackgroundButton> selectedButtons = gamePreparationComponent.getSelectedButtons();

        if(selectedButtons.size() == 2) {
            int workerFirstRow = selectedButtons.get(0).getRow();
            int workerFirstCol = selectedButtons.get(0).getCol();

            int workerSecondRow = selectedButtons.get(1).getRow();
            int workerSecondCol = selectedButtons.get(1).getCol();

            GamePreparationCommand gamePreparationCommand = new GamePreparationCommand(workerFirstRow, workerFirstCol, workerSecondRow, workerSecondCol);
            Gui.getInstance().notify(gamePreparationCommand);
        }
        else {
            JOptionPane.showMessageDialog(Gui.getInstance().getMainFrame(), errorDialogMessage, errorDialogTitle, JOptionPane.ERROR_MESSAGE);
        }
    }
}
