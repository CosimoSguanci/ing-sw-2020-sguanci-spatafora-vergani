package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoadingComponent extends JPanel {
    private Font font = Gui.getFont(Gui.FONT_REGULAR, 20);

    public LoadingComponent(String loadingMessage, Color textColor) {
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        JPanel jPanel = new JPanel();
        LayoutManager panelLayoutManager = new BoxLayout(jPanel, BoxLayout.Y_AXIS);
        jPanel.setLayout(panelLayoutManager);

        JLabel label = new JLabel(loadingMessage);
        label.setForeground(textColor);
        label.setFont(this.font);
        label.setHorizontalAlignment(JLabel.CENTER);

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanel.add(label);

        ImageIcon imgIcon = new ImageIcon("src/main/resources/loading2.gif");

        JLabel label2 = new JLabel(imgIcon);
        label2.setBorder(new EmptyBorder(15, 0, 0, 0));

        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanel.add(label2);

        jPanel.setOpaque(false);


        this.add(jPanel, BorderLayout.NORTH);


        this.setBorder(BorderFactory.createEmptyBorder(35,20,20,20));

        this.setOpaque(false);
    }


}
