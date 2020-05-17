package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.view.gui.Gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InitialInfo extends JPanel {
    private JTextField nicknameTextField;
    private JComboBox<PrintableColor> color;
    private String standardImgPath = "src/main/resources/images/InitialInfo/";
    private Image backgroundImage = new ImageIcon(standardImgPath + "backgroundTemple.png").getImage();
    Font font = new Font(Font.SERIF, Font.BOLD, 14);
    Color textColor = Color.WHITE;
    int buttonWidth = 60;


    public InitialInfo() {
        //LayoutManager layoutManager = new BoxLayout(this, BoxLayout.Y_AXIS);
        //this.setLayout(layoutManager);
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        //borderLayout for nickname and color, in the north
        JPanel nicknameColorPanel = new JPanel();
        nicknameColorPanel.setLayout(new BorderLayout());
        nicknameColorPanel.setOpaque(false);

        //nickname
        JPanel panelNickname = new JPanel();
        panelNickname.setLayout(new BoxLayout(panelNickname, BoxLayout.Y_AXIS));
        panelNickname.setOpaque(false);
        JPanel centredLabelNickname = new JPanel();
        centredLabelNickname.setOpaque(false);
        JLabel labelNickname = new JLabel("Insert a nickname:   (1 word)");
        labelNickname.setForeground(textColor);
        labelNickname.setFont(font);
        centredLabelNickname.add(labelNickname);
        this.nicknameTextField = new JTextField();
        panelNickname.add(centredLabelNickname);
        panelNickname.add(Box.createVerticalGlue());
        panelNickname.add(this.nicknameTextField);

        //color
        JPanel panelColor = new JPanel();
        panelColor.setLayout(new BoxLayout(panelColor, BoxLayout.Y_AXIS));
        panelColor.setOpaque(false);
        JPanel centredLabelColor = new JPanel();
        centredLabelColor.setOpaque(false);
        JLabel labelColor = new JLabel("Choose a color: ");
        labelColor.setForeground(textColor);
        labelColor.setFont(font);
        centredLabelColor.add(labelColor);
        PrintableColor[] colors = {PrintableColor.GREEN, PrintableColor.BLUE};
        this.color = new JComboBox<>(colors);
        panelColor.add(centredLabelColor);
        panelNickname.add(Box.createVerticalGlue());
        panelColor.add(this.color);

        nicknameColorPanel.add(panelNickname, BorderLayout.NORTH);
        nicknameColorPanel.add(panelColor, BorderLayout.SOUTH);
        this.add(nicknameColorPanel, BorderLayout.NORTH);



        //button to continue must be south-east
        ImageIcon continueImg = new ImageIcon(standardImgPath + "button-play-normal.png");
        continueImg = new ImageIcon(continueImg.getImage().getScaledInstance(this.buttonWidth, -1, Image.SCALE_DEFAULT));
        JButton continueButton = new JButton(continueImg);
        JPanel innerPanel = new JPanel();
        JPanel innerPanel2 = new JPanel();
        innerPanel.setLayout(new BorderLayout());
        innerPanel.setOpaque(false);
        innerPanel2.setLayout(new BorderLayout());
        innerPanel2.setOpaque(false);
        innerPanel2.add(continueButton, BorderLayout.EAST);
        innerPanel.add(innerPanel2);
        this.add(innerPanel, BorderLayout.SOUTH);

        //labelNickname.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        //labelColor.setBorder(BorderFactory.createEmptyBorder(20,0,10,0));

        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }



}
