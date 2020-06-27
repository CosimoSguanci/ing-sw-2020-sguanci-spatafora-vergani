package it.polimi.ingsw.view.gui.ui;

import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * This class defines a component which indicates a waiting for
 * opponents to terminate their turns.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Vergani
 */
public class LoadingComponent extends JPanel {

    private String loadingMessage;
    private final JLabel waitingLabel;

    /**
     * This is the constructor of the class. At the moment of creation a new
     * layout is created which contains a loading gif
     * @param loadingMessage contains a message which would be displayed in order to let player know what they are waiting for
     * @param textColor indicates the color in which message would be printed
     */
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
        } else {
            imgIcon = new ImageIcon(getClass().getResource("/images/loadings/loading_infinity_white.gif"));
        }

        JLabel label2 = new JLabel(imgIcon);
        label2.setBorder(new EmptyBorder(15, 0, 0, 0));
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanel.add(label2);
        jPanel.setOpaque(false);
        this.add(jPanel, BorderLayout.NORTH);
        this.setBorder(BorderFactory.createEmptyBorder(35, 20, 20, 20));
        this.setOpaque(false);
    }

    /**
     * This is a simple setter of a String. It set the string that would be displayed as loadingMessage
     * @param loadingMessage contains a String which would be displayed
     */
    public void setLoadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
        this.waitingLabel.setText(loadingMessage);
    }
}
