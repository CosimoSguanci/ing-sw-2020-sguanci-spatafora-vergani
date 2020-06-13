package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.view.gui.components.GodChoice;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GodChoiceJButtonListener implements ActionListener {
    private final GodChoice godChoice;

    public GodChoiceJButtonListener(GodChoice godChoice) {
        this.godChoice = godChoice;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        godChoice.setGodChoiceSelected(btn);
    }
}
