package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.view.gui.components.GodChoice;
import it.polimi.ingsw.view.gui.components.GodInfo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GodChoiceInfoButtonListener implements ActionListener {

    private GodChoice godChoice;
    private JLabel titleLabel = new JLabel("Click on a god to have info");
    private JPanel possibleGodsPanel = new JPanel();
    private JPanel innerPanel = new JPanel();
    private JPanel innerPanel2 = new JPanel();

    public GodChoiceInfoButtonListener(GodChoice godChoice) {
        this.godChoice = godChoice;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JButton pressedButton = (JButton) e.getSource();
        System.out.println("Info pressed");

        //JDialog dialog = GodInfo.showAllGodsForInfo(this.godChoice);
    }
}
