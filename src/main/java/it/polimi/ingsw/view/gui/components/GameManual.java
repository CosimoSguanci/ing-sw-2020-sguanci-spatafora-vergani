package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.Manual;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GameManual extends JPanel {
    private Font font = new Font(Font.SERIF, Font.BOLD, 13);
    private String standardImgPath = "src/main/resources/images/GameManual/";
    private Image backgroundImage = new ImageIcon(standardImgPath + "background.jpg").getImage();
    private int imgButtonWidth = 95;
    private int imgButtonHeight = 78;
    private Dimension buttonDimension = new Dimension(250, 100);
    private Dimension rigidAreaDimension = new Dimension(0, 3);
    private JButton[] ruleButtonArray;

    public GameManual() {
        LinkedHashMap<String, String> rules = Manual.getRules();
        int ruleNumber = rules.size();
        this.ruleButtonArray = new JButton[ruleNumber];

        JPanel grid = new JPanel();
        grid.setLayout(new BoxLayout(grid, BoxLayout.Y_AXIS));
        grid.setOpaque(false);

        Object[] ruleTitle = rules.keySet().toArray();
        for(int i = 0; i < ruleNumber; i++) {
            String key = (String) ruleTitle[i];  //create strings for rules: title and description
            //String description = rules.get(key);
            ImageIcon image = new ImageIcon(this.standardImgPath + key.toLowerCase().substring(0,3) + ".png");
            image = new ImageIcon(image.getImage().getScaledInstance(this.imgButtonWidth, this.imgButtonHeight, Image.SCALE_SMOOTH));
            this.ruleButtonArray[i] = new JButton(key.toUpperCase(), image);
            this.ruleButtonArray[i].setHorizontalTextPosition(JButton.CENTER);
            this.ruleButtonArray[i].setVerticalTextPosition(JButton.BOTTOM);
            this.ruleButtonArray[i].setFont(this.font);
            this.ruleButtonArray[i].setPreferredSize(this.buttonDimension);
            this.ruleButtonArray[i].setMaximumSize(this.buttonDimension);
            this.ruleButtonArray[i].setMinimumSize(this.buttonDimension);
            grid.add(Box.createRigidArea(this.rigidAreaDimension));
            grid.add(this.ruleButtonArray[i]);
            grid.add(Box.createRigidArea(this.rigidAreaDimension));
        }

        this.add(grid);
        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }

    /*public GameManual() {
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        JPanel mainPanel = new JPanel();  //centred
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        LinkedHashMap<String, String> rules = Manual.getRules();

        Object[] ruleTitle = rules.keySet().toArray();
        for(int i = 0; i < rules.size(); i++) {  //create JLabels for rules: title and description
            String key = (String) ruleTitle[i];
            String description = rules.get(key);

            JPanel titlePanel = new JPanel();
            JLabel title = new JLabel(key);
            titlePanel.setBackground(this.color);
            title.setFont(this.titleFont);
            JTextPane rule = new JTextPane();
            rule.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
            rule.setText(description);
            rule.setFont(this.font);
            rule.setEditable(false);

            titlePanel.add(title);
            mainPanel.add(titlePanel);
            mainPanel.add(rule);
            mainPanel.add(Box.createVerticalGlue());
        }

        //mainPanel.setPreferredSize(new Dimension(300, 600));


        //OK button must be bottom-right
        ImageIcon continueImg = new ImageIcon(this.externalImgPath + "done.png");
        continueImg = new ImageIcon(continueImg.getImage().getScaledInstance(this.buttonWidth, -1, Image.SCALE_SMOOTH));
        JButton continueButton = new JButton(continueImg);
        JPanel innerPanel = new JPanel();
        JPanel innerPanel2 = new JPanel();
        innerPanel.setLayout(new BorderLayout());
        innerPanel.setOpaque(false);
        innerPanel2.setLayout(new BorderLayout());
        innerPanel2.setOpaque(false);
        innerPanel2.add(continueButton, BorderLayout.EAST);
        innerPanel.add(innerPanel2);

        JScrollPane scrollableMain = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        /*scrollableMain.setMinimumSize(new Dimension(200, 200));
        scrollableMain.setPreferredSize(new Dimension(300, 300));
        scrollableMain.setMaximumSize(new Dimension(800, 1500));*/
        //this.add(scrollableMain, BorderLayout.NORTH);
        //this.add(innerPanel, BorderLayout.SOUTH);

        //this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    //}


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
