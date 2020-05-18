package it.polimi.ingsw.view.gui.components;

import javax.swing.*;
import java.awt.*;

public class WaitingForAMatch extends JPanel {
    private String standardImgPath = "src/main/resources/images/WaitingForAMatch/";
    private Image backgroundImage = new ImageIcon(standardImgPath + "title_FG_grass.png").getImage();
    private Font font = new Font(Font.SERIF, Font.BOLD, 14);

    public WaitingForAMatch() {
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);
        JLabel label = new JLabel("Waiting for a match...");
        label.setFont(this.font);
        label.setHorizontalAlignment(JLabel.CENTER);
        this.add(label, BorderLayout.NORTH);

        this.setBorder(BorderFactory.createEmptyBorder(35,20,20,20));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
