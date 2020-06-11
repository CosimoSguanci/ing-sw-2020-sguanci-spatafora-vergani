package it.polimi.ingsw.view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ConnectionError {

    public void show() {
        SwingUtilities.invokeLater(() -> {
            try {
                draw();
            } catch (IOException ignored) {
            }
        });
    }

    private void draw() throws IOException {
        JFrame frame = new JFrame("Santorini");

        JPanel mainPanel = new JPanel();

        frame.add(mainPanel);

        frame.pack();
        frame.setPreferredSize(new Dimension(800, 700));
        frame.setMinimumSize(frame.getPreferredSize());
        frame.setIconImage(ImageIO.read(Gui.class.getResource("/images/title_island.png")));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
