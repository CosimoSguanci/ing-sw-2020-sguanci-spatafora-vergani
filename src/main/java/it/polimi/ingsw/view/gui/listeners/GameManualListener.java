package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.components.GameManual;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * This class is the listener of the GameManual
 * GameManual button is in every game phase in order
 * to let a player know in eah moment game rules and other info.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class GameManualListener implements ActionListener {
    private final JPanel mainPanel;

    /**
     * This is the constructor of the class. At the moment of creation
     * an association between the listener and the mainPanel associated
     * to the component related to the current game phase is made.
     *
     * @param mainPanel contains a reference of the Panel related to the
     */
    public GameManualListener(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    /**
     * This method is invoked every time the infoButton is clicked.
     * It shows a Dialog in which GameManual is presented.
     *
     * @param e contains a reference to the button clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() ->
                {
                    try {
                        showDialog();
                    } catch (IOException exception) {
                        System.err.println("Error loading some Game Manual resource");
                    }
                }
        );
    }

    /**
     * This private method creates a GameManual instance
     *
     * @throws IOException case something went wrong.
     */
    private void showDialog() throws IOException {
        GameManual dialog = new GameManual();
        dialog.pack();
        dialog.setPreferredSize(new Dimension(605, 605));
        dialog.setMinimumSize(dialog.getPreferredSize());
        dialog.setMaximumSize(new Dimension(650, 650));
        dialog.setIconImage(ImageIO.read(Gui.class.getResource("/images/title_island.png")));
        dialog.setLocationRelativeTo(this.mainPanel);
        dialog.setVisible(true);
    }
}
