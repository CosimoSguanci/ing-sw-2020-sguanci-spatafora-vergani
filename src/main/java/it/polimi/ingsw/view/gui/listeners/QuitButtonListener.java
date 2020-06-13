package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuitButtonListener implements ActionListener {
    Gui gui = Gui.getInstance();
    private final JPanel panel;

    public QuitButtonListener(JPanel panel) {
        this.panel = panel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String title = "Quit";
        String message = "Are you sure you want to quit?";

        SwingUtilities.invokeLater(() -> {
            int res = JOptionPane.showOptionDialog(this.panel, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

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
