package it.polimi.ingsw.view.gui.components;

import javax.swing.*;
import java.awt.*;

public class WaitingForAMatch extends JPanel {

    public WaitingForAMatch() {
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);
        JLabel label = new JLabel("Waiting for a match...");
        label.setHorizontalAlignment(JLabel.CENTER);
        this.add(label, BorderLayout.CENTER);
    }
}
