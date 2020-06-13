package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoadingComponent extends JPanel {

    private String loadingMessage;
    private JLabel waitingLabel;

    public LoadingComponent(String loadingMessage, Color textColor) {
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        JPanel jPanel = new JPanel();
        LayoutManager panelLayoutManager = new BoxLayout(jPanel, BoxLayout.Y_AXIS);
        jPanel.setLayout(panelLayoutManager);

        this.loadingMessage = loadingMessage;

        this.waitingLabel = new JLabel(this.loadingMessage);

        this.waitingLabel.setForeground(textColor);
        Font font = Gui.getFont(Gui.FONT_REGULAR, 20);
        this.waitingLabel.setFont(font);
        this.waitingLabel.setHorizontalAlignment(JLabel.CENTER);

        this.waitingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanel.add(this.waitingLabel);

        ImageIcon imgIcon;

        if (textColor.equals(Color.BLACK)) {
            imgIcon = new ImageIcon(getClass().getResource("/images/loadings/loading_infinity_black.gif"));
        }
        else {
            imgIcon = new ImageIcon(getClass().getResource("/images/loadings/loading_infinity_white.gif"));
        }

        JLabel label2 = new JLabel(imgIcon);
        label2.setBorder(new EmptyBorder(15, 0, 0, 0));

        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanel.add(label2);

        jPanel.setOpaque(false);


        this.add(jPanel, BorderLayout.NORTH);


        this.setBorder(BorderFactory.createEmptyBorder(35,20,20,20));

        this.setOpaque(false);
    }

    void setLoadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
        this.waitingLabel.setText(loadingMessage);
    }


}
