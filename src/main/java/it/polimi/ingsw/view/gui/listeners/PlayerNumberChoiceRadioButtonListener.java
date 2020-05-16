package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.view.gui.components.PlayerNumberChoice;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerNumberChoiceRadioButtonListener implements ActionListener {
    private PlayerNumberChoice playerNumberChoose;

    public PlayerNumberChoiceRadioButtonListener(PlayerNumberChoice playerNumberChoose) {
        this.playerNumberChoose = playerNumberChoose;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JRadioButton btn = (JRadioButton) e.getSource();
        playerNumberChoose.setPlayerNumberSelected(btn);
        playerNumberChoose.refresh();
    }
}
