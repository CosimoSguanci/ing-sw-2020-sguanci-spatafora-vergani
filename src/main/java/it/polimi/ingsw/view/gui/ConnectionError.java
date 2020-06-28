package it.polimi.ingsw.view.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;

/**
 * This class manages errors during connection to server phase.
 * When a Client cannot connect to the server a new Panel, in which
 * he is notified that something went wrong in connection, appears in his screen
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class ConnectionError {

    /**
     * This method handles the notification of an error when trying to connect to server
     */
    public void show() {
        SwingUtilities.invokeLater(() -> {
            try {
                draw();
            } catch (IOException e) {
                System.err.println("Unexpected Error: " + e.getMessage());
                System.exit(-1);
            }
        });
    }

    /**
     * This private method creates a new ConnectionErrorPanel through which a player
     * is notified about the error in the connection with the server.
     */
    private void draw() throws IOException {
        JFrame frame = new JFrame("Santorini");
        ConnectionErrorPanel mainPanel = new ConnectionErrorPanel();
        frame.add(mainPanel);
        Gui.initFrame(frame);
    }

    /**
     * This is a private class used to create instances of a particular panel
     * used to notify users that something went wrong during connection with server
     */
    private static class ConnectionErrorPanel extends JPanel {
        private final Image backgroundImage = new ImageIcon(getClass().getResource("/images/connection_error_bg.png")).getImage();

        /**
         * This is the constructor of the private Class. It set the background,
         * the label which explains the problem to the user, color and font text is set.
         */
        private ConnectionErrorPanel() {

            LayoutManager layoutManager = new BorderLayout();
            this.setLayout(layoutManager);
            JPanel buttonAllInfoPanel = new JPanel();
            buttonAllInfoPanel.setLayout(new BorderLayout());
            buttonAllInfoPanel.setOpaque(false);
            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(false);
            JPanel resumeNumberPanel = new JPanel();
            resumeNumberPanel.setOpaque(false);
            JLabel errorLabel = new JLabel("The Game couldn't start, maybe there was some network error " +
                    "or the server isn't available.");
            errorLabel.setBackground(Color.RED);
            errorLabel.setOpaque(true);
            Border border = BorderFactory.createLineBorder(Color.WHITE, 2, true);
            errorLabel.setBorder(BorderFactory.createCompoundBorder(border,
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            Color resumeNumberTextColor = Color.WHITE;
            errorLabel.setForeground(resumeNumberTextColor);
            Font resumeNumberFont = Gui.getFont(Gui.FONT_BOLD, 19);
            errorLabel.setFont(resumeNumberFont);
            resumeNumberPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
            resumeNumberPanel.add(errorLabel);
            buttonAllInfoPanel.add(resumeNumberPanel, BorderLayout.CENTER);
            this.add(buttonAllInfoPanel, BorderLayout.CENTER);

        }

        /**
         * This method set the background image of the panel
         * @param g contains a reference to the component which we want to set the background image.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(this.backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }
}
