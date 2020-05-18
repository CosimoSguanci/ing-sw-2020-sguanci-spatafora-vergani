package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.components.GodChoice;
import it.polimi.ingsw.view.gui.components.InitialInfo;
import it.polimi.ingsw.view.gui.components.PlayerNumberChoice;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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

        frame.add(new GodChoice(3));
        frame.setIconImage(ImageIO.read(Gui.class.getResource("/images/InitialInfo/button-play-normal.png")));
        frame.pack();
        frame.setSize(700, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
