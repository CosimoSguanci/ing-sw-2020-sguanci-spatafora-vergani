package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.components.InitialInfo;
import it.polimi.ingsw.view.gui.components.PlayerNumberChoice;

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

        frame.add(new PlayerNumberChoice());

        frame.pack();
        frame.setSize(450, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
