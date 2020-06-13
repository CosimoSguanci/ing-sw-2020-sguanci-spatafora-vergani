package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.Manual;
import it.polimi.ingsw.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GameManual extends JPanel implements ActionListener {
    //private Font font = new Font(Font.SERIF, Font.BOLD, 13);
    private String standardImgPath = "/images/GameManual/";
    private Image backgroundImage = new ImageIcon(getClass().getResource(standardImgPath + "green.jpg")).getImage();
    private int imgButtonWidth = 60;
    //private int imgButtonHeight = 78;
    private Dimension buttonDimension = new Dimension(250, 100);
    private Dimension rigidAreaDimension = new Dimension(0, 3);
    private JButton[] ruleButtonArray;
    private LinkedHashMap<String, String> rules = Manual.getRules();  //titles and descriptions of game's rules

    public GameManual() {
        //LinkedHashMap<String, String> rules = Manual.getRules();
        int ruleNumber = this.rules.size();
        this.ruleButtonArray = new JButton[ruleNumber];

        JPanel grid = new JPanel();
        grid.setLayout(new BoxLayout(grid, BoxLayout.Y_AXIS));
        grid.setOpaque(false);

        Object[] ruleTitle = this.rules.keySet().toArray();
        for(int i = 0; i < ruleNumber; i++) {
            String key = (String) ruleTitle[i];  //create title-strings for rules
            ImageIcon image = new ImageIcon(getClass().getResource(this.standardImgPath + key.toLowerCase().substring(0,3) + ".png"));
            image = new ImageIcon(image.getImage().getScaledInstance(this.imgButtonWidth, -1, Image.SCALE_SMOOTH));
            this.ruleButtonArray[i] = new JButton(key, image);
            this.ruleButtonArray[i].setHorizontalTextPosition(JButton.CENTER);
            this.ruleButtonArray[i].setVerticalTextPosition(JButton.BOTTOM);
            //this.ruleButtonArray[i].setFont(this.font);
            this.ruleButtonArray[i].setPreferredSize(this.buttonDimension);
            this.ruleButtonArray[i].setMaximumSize(this.buttonDimension);
            this.ruleButtonArray[i].setMinimumSize(this.buttonDimension);
            this.ruleButtonArray[i].addActionListener(this);
            grid.add(Box.createRigidArea(this.rigidAreaDimension));
            grid.add(this.ruleButtonArray[i]);
            grid.add(Box.createRigidArea(this.rigidAreaDimension));
        }

        this.add(grid);
        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton pressedButton = (JButton) e.getSource();
        String title = pressedButton.getText();
        Icon icon = pressedButton.getIcon();
        String description = rules.get(title);  //description associated with pressed-button
        JOptionPane.showMessageDialog(this, description, title, JOptionPane.INFORMATION_MESSAGE, icon);
    }
}
