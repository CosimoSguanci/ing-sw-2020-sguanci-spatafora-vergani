package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.components.GodChoice;
import it.polimi.ingsw.view.gui.components.GodInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * This class is the listener of the GodChoiceInfoButton.
 * This particular roundButton is used during GOD_CHOICE game phase.
 * Once clicked it will display a Dialog in which info about Gods and their
 * power are available to the players.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class GodChoiceInfoButtonListener implements ActionListener {

    private final GodChoice godChoice;

    /**
     * This is the constructor of the class. At the moment of creation
     * an association between the listener and GodChoice component is made.
     *
     * @param godChoice contains a reference to the GodChoice component
     */
    public GodChoiceInfoButtonListener(GodChoice godChoice) {
        this.godChoice = godChoice;
    }

    /**
     * This method is invoked at the moment of clicking on the info round button in God choice game phase
     * If invoked it will display a Dialog with information about Gods available.
     *
     * @param e contains a reference to the GodChoiceInfoButton clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() ->
                {
                    try {
                        showDialog();
                    } catch (IOException exception) {
                        System.err.println("Error loading some God Choice Info Button resource");
                    }
                }
        );
    }

    /**
     * This private method creates a GodInfo instance
     *
     * @throws IOException case something went wrong.
     */
    private void showDialog() throws IOException {
        GodInfo dialog = new GodInfo(this.godChoice.getSelectableGods());
        dialog.pack();
        dialog.setPreferredSize(new Dimension(605, 605));
        dialog.setMinimumSize(dialog.getPreferredSize());
        dialog.setMaximumSize(new Dimension(650, 650));
        dialog.setIconImage(ImageIO.read(Gui.class.getResource("/images/title_island.png")));
        dialog.setLocationRelativeTo(this.godChoice);
        dialog.setVisible(true);
    }
}
