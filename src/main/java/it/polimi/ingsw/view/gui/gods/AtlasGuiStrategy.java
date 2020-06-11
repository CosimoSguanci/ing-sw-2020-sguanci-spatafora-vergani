package it.polimi.ingsw.view.gui.gods;

import javax.swing.*;

public class AtlasGuiStrategy implements GodGuiDrawer {
    @Override
    public JComponent draw() {
        return new JLabel("Atlas GUI");
    }
}
