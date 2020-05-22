package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;

public class BackgroundButton extends JButton {
    private Image backgroundImage = new ImageIcon(Gui.class.getResource("/images/InitialInfo/backgroundTemple.png")).getImage();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
