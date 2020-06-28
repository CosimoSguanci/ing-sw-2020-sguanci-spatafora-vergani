package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.view.gui.components.PlayerNumberChoice;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is the listener of the PlayerNumberChoice.
 * When a player connects to the server, he is asked to select how many
 * player does he wants in his match and there are two radio button available.
 * This class is a listener of this two possible options.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class PlayerNumberChoiceRadioButtonListener implements ActionListener {
    private final PlayerNumberChoice playerNumberChoice;

    /**
     * This is the constructor of the class. At the moment of creation an association
     * between listener and PlayerNumberChoice component is made
     *
     * @param playerNumberChoice the number of players that are playing the match
     */
    public PlayerNumberChoiceRadioButtonListener(PlayerNumberChoice playerNumberChoice) {
        this.playerNumberChoice = playerNumberChoice;
    }

    /**
     * This method is invoked when a player click on the RadioButton which
     * gives him the possibility to choose how many player he wants in his match.
     * In this method a PlayerNumberChoice method is invoked giving button
     * clicked as parameter and it will manages all the possibilities.
     *
     * @param e contains a reference to the button clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JRadioButton btn = (JRadioButton) e.getSource();
        playerNumberChoice.setPlayerNumberSelected(btn);
        playerNumberChoice.refresh();
    }
}
