package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.view.gui.ui.JGodButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class GodInfoActionListener implements ActionListener {
    private final Container container;

    public GodInfoActionListener(Container container) {
        this.container = container;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JGodButton pressedButton = (JGodButton) e.getSource();
        String god = pressedButton.getGodName();

        Map<String, String> godInfo = GodsUtils.parseGodName(god);  //all god's information

        int godIconWidth = 70;
        String godChoiceImgPath = "/images/GodChoice/";
        ImageIcon godIcon = new ImageIcon(getClass().getResource(godChoiceImgPath + god + ".png"));  //god's image
        godIcon = new ImageIcon(godIcon.getImage().getScaledInstance(godIconWidth, -1, Image.SCALE_SMOOTH));

        JOptionPane.showMessageDialog(this.container, godInfo.get(GodsUtils.POWER_DESCRIPTION), godInfo.get(GodsUtils.GOD_NAME) + ", " + godInfo.get(GodsUtils.GOD_DESCRIPTION), JOptionPane.INFORMATION_MESSAGE, godIcon);
    }
}
