package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WaitingForAMatch extends JPanel {
    private final String standardImgPath = "/images/WaitingForAMatch/";
    private final Image backgroundImage = new ImageIcon(getClass().getResource(standardImgPath + "title_FG_grass.png")).getImage();

    public WaitingForAMatch() {
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        JPanel jPanel = new JPanel();
        LayoutManager panelLayoutManager = new BoxLayout(jPanel, BoxLayout.Y_AXIS);
        jPanel.setLayout(panelLayoutManager);

        JLabel label = new JLabel("Waiting for a match...");
        Font font = Gui.getFont(Gui.FONT_BOLD, 16);
        label.setFont(font);
        label.setHorizontalAlignment(JLabel.CENTER);

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanel.add(label);

        Icon imgIcon = new ImageIcon(getClass().getResource("/images/loadings/loading.gif"));
        JLabel label2 = new JLabel(imgIcon);
        label2.setBorder(new EmptyBorder(15, 0, 0, 0));

        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanel.add(label2);

        jPanel.setOpaque(false);

        this.add(jPanel, BorderLayout.NORTH);

        this.setBorder(BorderFactory.createEmptyBorder(35, 20, 20, 20));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
