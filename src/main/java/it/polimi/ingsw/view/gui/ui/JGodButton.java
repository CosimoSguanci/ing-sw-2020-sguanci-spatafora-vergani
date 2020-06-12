package it.polimi.ingsw.view.gui.ui;

import javax.swing.*;

public class JGodButton extends JButton {
    private String godName;

    public JGodButton(String text, String godName) {
        super(text);
        this.godName = godName;
    }

    public JGodButton(String text, ImageIcon image, String godName) {
        super(text, image);
        this.godName = godName;
    }

    public String getGodName() {
        return this.godName;
    }
}
