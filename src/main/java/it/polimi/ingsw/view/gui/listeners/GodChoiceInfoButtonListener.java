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

public class GodChoiceInfoButtonListener implements ActionListener {

    private GodChoice godChoice;
    private GodInfo dialog;

    public GodChoiceInfoButtonListener(GodChoice godChoice) {
        this.godChoice = godChoice;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() ->
            {
                try {
                    showDialog();
                } catch(IOException exception) {
                    exception.printStackTrace();
                }
            }
        );
    }

    private void showDialog() throws IOException {
        dialog = new GodInfo(this.godChoice.getSelectableGods());
        dialog.pack();
        dialog.setPreferredSize(new Dimension(605, 605));
        dialog.setMinimumSize(dialog.getPreferredSize());
        dialog.setMaximumSize(new Dimension(650, 650));
        dialog.setIconImage(ImageIO.read(Gui.class.getResource("/images/title_island.png")));
        dialog.setLocationRelativeTo(this.godChoice);
        dialog.setVisible(true);
    }
}
