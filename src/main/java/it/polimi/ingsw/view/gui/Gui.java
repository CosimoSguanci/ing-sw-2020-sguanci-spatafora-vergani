package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.view.gui.components.*;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

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

        //frame.add(new PlayerNumberChoice());
        //frame.add(new WaitingForAMatch());
        //frame.add(new InitialInfo());
        JOptionPane.showMessageDialog(frame, NicknameAlreadyUsed.getMessage(), NicknameAlreadyUsed.title, JOptionPane.ERROR_MESSAGE);

        frame.pack();
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
