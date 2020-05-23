package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.view.gui.components.PlayerNumberChoice;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerNumberStartButtonListener implements ActionListener {

    private PlayerNumberChoice playerNumberChoice;

    public PlayerNumberStartButtonListener(PlayerNumberChoice playerNumberChoice) {
        this.playerNumberChoice = playerNumberChoice;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       // playerNumberChoice.startPressed
    }
}
