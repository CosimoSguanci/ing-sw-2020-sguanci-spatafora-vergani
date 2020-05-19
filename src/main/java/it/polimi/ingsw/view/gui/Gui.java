package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.components.GodChoice;
import it.polimi.ingsw.view.gui.components.InitialInfo;
import it.polimi.ingsw.view.gui.components.PlayerNumberChoice;
import it.polimi.ingsw.view.gui.components.WaitingForAMatch;
import it.polimi.ingsw.model.PrintableColor;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Gui {

    private static JFrame frame;

    public Gui(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    showGui();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void showGui() throws IOException {
        frame = new JFrame("Santorini");

        //frame.add(new PlayerNumberChoice());
        //frame.add(new WaitingForAMatch());
        //frame.add(new InitialInfo());
        frame.add(new GodChoice(3));
        //frame.add(new GodChoice(3, ArrayList));

        frame.pack();
        frame.setIconImage(ImageIO.read(Gui.class.getResource("/images/InitialInfo/santorini-logo.png")));
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
