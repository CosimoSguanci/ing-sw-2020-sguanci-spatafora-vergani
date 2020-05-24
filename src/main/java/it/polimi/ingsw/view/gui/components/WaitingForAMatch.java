package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WaitingForAMatch extends JPanel {
    private String standardImgPath = "src/main/resources/images/WaitingForAMatch/";
    private Image backgroundImage = new ImageIcon(standardImgPath + "title_FG_grass.png").getImage();
    private Font font = Gui.getFont(Gui.FONT_BOLD, 16);

    public WaitingForAMatch() {
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        JPanel jPanel = new JPanel();
        LayoutManager panelLayoutManager = new BoxLayout(jPanel, BoxLayout.Y_AXIS);
        jPanel.setLayout(panelLayoutManager);

        JLabel label = new JLabel("Waiting for a match...");
        label.setFont(this.font);
        label.setHorizontalAlignment(JLabel.CENTER);

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanel.add(label);

        Icon imgIcon = new ImageIcon("src/main/resources/loading.gif");
        JLabel label2 = new JLabel(imgIcon);
        label2.setBorder(new EmptyBorder(15, 0, 0, 0));

        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanel.add(label2);

        jPanel.setOpaque(false);



        this.add(jPanel, BorderLayout.NORTH);




        this.setBorder(BorderFactory.createEmptyBorder(35,20,20,20));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
