package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.view.gui.components.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class GodInfoActionListener implements ActionListener {
    private String godChoiceImgPath = "src/main/resources/images/GodChoice/";
    private Game game;

    public GodInfoActionListener(Game game) {
        this.game = game;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JButton pressedButton = (JButton) e.getSource();
        String buttonText = pressedButton.getText().toLowerCase();
        String god = buttonText.substring(buttonText.lastIndexOf(" ") + 1);

        Map<String, String> godInfo = GodsUtils.parseGodName(god);  //all god's information

        int godIconWidth = 70;
        ImageIcon godIcon = new ImageIcon(this.godChoiceImgPath + god + ".png");  //god's image
        godIcon = new ImageIcon(godIcon.getImage().getScaledInstance(godIconWidth, -1, Image.SCALE_SMOOTH));

        JOptionPane.showMessageDialog(this.game, godInfo.get(GodsUtils.POWER_DESCRIPTION), godInfo.get(GodsUtils.GOD_NAME) + ", " + godInfo.get(GodsUtils.GOD_DESCRIPTION), JOptionPane.INFORMATION_MESSAGE, godIcon);
    }
}
