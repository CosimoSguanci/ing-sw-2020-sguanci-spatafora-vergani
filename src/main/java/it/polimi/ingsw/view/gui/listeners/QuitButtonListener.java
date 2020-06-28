package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is the listener of the QuitButton available in several components in gui.
 * QuitButton is clicked by a player who doesn't want to continue play the match.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class QuitButtonListener implements ActionListener {
    private final JPanel panel;
    Gui gui = Gui.getInstance();

    /**
     * This is the creator of the class. At the moment of creation an association
     * between listener and the panel of the relative current phase of the match is made.
     *
     * @param panel contains a reference to the Panel
     */
    public QuitButtonListener(JPanel panel) {
        this.panel = panel;
    }

    /**
     * This method manages cases in which a player clicks on QuitButton.
     * Once clicked it is asked to the player if is he sure to quit through
     * an OptionDialog. If player select to quit it is asked to him if he
     * wants to play a new match or not.
     *
     * @param e contains a reference to the button clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String title = "Quit";
        String message = "Are you sure you want to quit?";

        SwingUtilities.invokeLater(() -> {

            String imagePath = "/images/RealGame/exit.png";
            int iconWidth = 70;
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            icon = new ImageIcon(icon.getImage().getScaledInstance(iconWidth, -1, Image.SCALE_SMOOTH));

            int res = JOptionPane.showOptionDialog(this.panel, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon, null, null);

            switch (res) {
                case JOptionPane.YES_OPTION:
                    this.gui.askPlayAgainDialog();
                    break;
                case JOptionPane.NO_OPTION:
                case JOptionPane.CLOSED_OPTION:
                    break;
            }
        });
    }
}
