package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.components.InitialInfo;
import it.polimi.ingsw.view.gui.components.PlayerNumberChoose;
import it.polimi.ingsw.view.gui.components.WaitingForAMatch;

import javax.swing.*;

public class Gui {

    private static JFrame frame;

    public Gui(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showGui();
            }
        });
    }

    private static void showGui(){
        frame = new JFrame("Santorini");

        frame.add(new InitialInfo());

        frame.pack();
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
