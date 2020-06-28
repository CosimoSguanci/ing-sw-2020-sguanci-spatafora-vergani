package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.Manual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

/**
 * GameManual is a class in which the GUI-component of
 * the game manual is generated. Every time a user clicks the "helper" button
 * a new separate window opens and all the manual is visible.
 * It contains different information: general info, how to play
 * how to win, information about God.
 *
 * @author Andrea Vergani
 * @author Roberto Spatafora
 */
public class GameManual extends JDialog implements ActionListener {
    private final String standardImgPath = "/images/GameManual/";
    private final Image backgroundImage = new ImageIcon(getClass().getResource(standardImgPath + "green.jpg")).getImage();
    private final Dimension buttonDimension = new Dimension(250, 100);
    private final Dimension rigidAreaDimension = new Dimension(0, 3);
    private final LinkedHashMap<String, String> rules = Manual.getRules();  //titles and descriptions of game's rules

    /**
     * This is the constructor of the class. It creates a Panel in which
     * different button containing information about the game are put in.
     * In this method the private panelToShow method is invoked, this method
     * generates all the different rule buttons.
     */
    public GameManual() {
        super((Frame) null, "Game Manual", false);

        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel panelToShow = panelToShow();
        panelToShow.setOpaque(false);

        mainPanel.add(panelToShow);
        panelToShow.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelToShow.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.add(mainPanel);
        this.revalidate();
        this.repaint();
    }

    /**
     * This private method creates as many buttons as the number
     * of game manual rules.
     *
     * @return a JPanel which contains all the different button
     * which contain the information about the game.
     */
    private JPanel panelToShow() {
        int ruleNumber = this.rules.size();
        JButton[] ruleButtonArray = new JButton[ruleNumber];

        JPanel grid = new JPanel();
        grid.setLayout(new BoxLayout(grid, BoxLayout.Y_AXIS));
        grid.setOpaque(false);

        Object[] ruleTitle = this.rules.keySet().toArray();
        for (int i = 0; i < ruleNumber; i++) {
            String key = (String) ruleTitle[i];  //create title-strings for rules
            ImageIcon image = new ImageIcon(getClass().getResource(this.standardImgPath + key.toLowerCase().substring(0, 3) + ".png"));
            int imgButtonWidth = 60;
            image = new ImageIcon(image.getImage().getScaledInstance(imgButtonWidth, -1, Image.SCALE_SMOOTH));
            ruleButtonArray[i] = new JButton(key, image);
            ruleButtonArray[i].setHorizontalTextPosition(JButton.CENTER);
            ruleButtonArray[i].setVerticalTextPosition(JButton.BOTTOM);
            ruleButtonArray[i].setPreferredSize(this.buttonDimension);
            ruleButtonArray[i].setMaximumSize(this.buttonDimension);
            ruleButtonArray[i].setMinimumSize(this.buttonDimension);
            ruleButtonArray[i].addActionListener(this);
            grid.add(Box.createRigidArea(this.rigidAreaDimension));
            grid.add(ruleButtonArray[i]);
            grid.add(Box.createRigidArea(this.rigidAreaDimension));
        }

        return grid;
    }

    /**
     * This method manages all the activities linked to
     * the clicks on the buttons of the class
     *
     * @param e contains reference to the button clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton pressedButton = (JButton) e.getSource();
        String title = pressedButton.getText();
        Icon icon = pressedButton.getIcon();
        String description = rules.get(title);  //description associated with pressed-button
        JOptionPane.showMessageDialog(this, description, title, JOptionPane.INFORMATION_MESSAGE, icon);
    }
}
