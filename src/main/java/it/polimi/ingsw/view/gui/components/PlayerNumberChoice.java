package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.gui.listeners.PlayerNumberChoiceRadioButtonListener;

import javax.swing.*;
import java.awt.*;

public class PlayerNumberChoice extends JPanel {
    private JRadioButton button1;
    private JRadioButton button2;
    private PlayerNumberChoiceRadioButtonListener playerNumberChooseRadioButtonListener;

    public PlayerNumberChoice() {
        //LayoutManager layoutManager = new BoxLayout(this, BoxLayout.Y_AXIS);
        //this.setLayout(layoutManager);
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        JLabel label = new JLabel("How many players do you want in your match? ");
        this.button1 = new JRadioButton("2 players", true);
        this.button2 = new JRadioButton("3 players");
        this.playerNumberChooseRadioButtonListener = new PlayerNumberChoiceRadioButtonListener(this);
        button1.addActionListener(playerNumberChooseRadioButtonListener);
        button2.addActionListener(playerNumberChooseRadioButtonListener);

        JButton continueButton = new JButton("Continue");
        JPanel innerPanel = new JPanel();
        JPanel innerPanel2 = new JPanel();
        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(label);
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        label.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        this.add(buttonPanel, BorderLayout.NORTH);

        this.add(innerPanel, BorderLayout.SOUTH);
        innerPanel.setLayout(new BorderLayout());
        innerPanel2.setLayout(new BorderLayout());
        innerPanel2.add(continueButton, BorderLayout.EAST);
        innerPanel.add(innerPanel2);

    }

    public void setPlayerNumberSelected(JRadioButton button) {
        if(button.equals(button1)) {
            button1.setSelected(true);
            button2.setSelected(false);
        }
        else{
            button1.setSelected(false);
            button2.setSelected(true);
        }
    }
}
