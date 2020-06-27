package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.view.gui.components.GodChoice;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is the listener of a button which indicates a particular God during GodChoice game phase.
 * Once clicked it means that a player want to select/unselect (if it was already selected) the specified God
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class GodChoiceJButtonListener implements ActionListener {
    private final GodChoice godChoice;

    /**
     * This is the constructor of the class. At the moment of creation an association between
     * listener and GodChoice Gui component is made.
     * @param godChoice contains a reference to the GodChoice component
     */
    public GodChoiceJButtonListener(GodChoice godChoice) {
        this.godChoice = godChoice;
    }

    /**
     * This method is invoked when a God button is clicked.
     * In this method setGodChoiceSelected method is invoked giving btn as parameter.
     * GodChoice method manages cases of button selected or not yet selected, cases in which
     * a player has already chosen a God and handles differently for GodChooser.
     * @param e contains a reference to the button clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        godChoice.setGodChoiceSelected(btn);
    }
}
