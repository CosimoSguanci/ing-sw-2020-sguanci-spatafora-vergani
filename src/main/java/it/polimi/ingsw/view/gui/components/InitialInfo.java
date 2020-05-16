package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.model.PrintableColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InitialInfo extends JPanel {
    private JTextField nicknameTextField;
    private JComboBox<PrintableColor> color;

    public InitialInfo() {
        //LayoutManager layoutManager = new BoxLayout(this, BoxLayout.Y_AXIS);
        //this.setLayout(layoutManager);
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        JLabel labelNickname = new JLabel("Insert a nickname: ");
        JLabel labelColor = new JLabel("Insert a color: ");
        this.nicknameTextField = new JTextField();
        PrintableColor[] colors = {PrintableColor.GREEN, PrintableColor.BLUE};
        this.color = new JComboBox<>(colors);
        //this.playerNumberChooseRadioButtonListener = new PlayerNumberChooseRadioButtonListener(this);
        //button1.addActionListener(playerNumberChooseRadioButtonListener);
        //button2.addActionListener(playerNumberChooseRadioButtonListener);

        //JButton continueButton = new JButton("Continue");
        JButton continueButton = new JButton(new ImageIcon("resources/images/InitialInfo/button-play-normal.png"));
        JPanel innerPanel = new JPanel();
        JPanel innerPanel2 = new JPanel();
        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(labelNickname);
        buttonPanel.add(nicknameTextField);
        buttonPanel.add(labelColor);
        buttonPanel.add(color);
        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        labelNickname.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        labelColor.setBorder(BorderFactory.createEmptyBorder(20,0,10,0));
        this.add(buttonPanel, BorderLayout.NORTH);

        this.add(innerPanel, BorderLayout.SOUTH);
        innerPanel.setLayout(new BorderLayout());
        innerPanel2.setLayout(new BorderLayout());
        innerPanel2.add(continueButton, BorderLayout.EAST);
        innerPanel.add(innerPanel2);

    }



}
