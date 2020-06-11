package it.polimi.ingsw.view.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;

public class ConnectionError {

    public void show() {
        SwingUtilities.invokeLater(() -> {
            try {
                draw();
            } catch (IOException ignored) {}
        });
    }

    private void draw() throws IOException {
        JFrame frame = new JFrame("Santorini");
        ConnectionErrorPanel mainPanel = new ConnectionErrorPanel();
        frame.add(mainPanel);
        Gui.initFrame(frame);
    }

    private static class ConnectionErrorPanel extends JPanel {

        private final Image backgroundImage = new ImageIcon("src/main/resources/images/connection_error_bg.png").getImage();

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
            /*
            "<html>The Game couldn't start, maybe there was <br />some network error " +
                    "or the server isn't available.</html>"
             */
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

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(this.backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }
}
