package it.polimi.ingsw.view.gui.gods;

import javax.swing.*;

public class ZeusGuiStrategy implements GodGuiDrawer {
    @Override
    public JComponent draw() {
        return new JLabel("Zeus GUI");
    }
}
