package it.polimi.ingsw.view.gui.components;

import javax.swing.*;

public class NicknameAlreadyUsed {
    public static final String title = "Message";

    public static String getMessage() {
        return "You can't use this nickname!" + System.lineSeparator() +
                "Nicknames already used are: ";
    }

    /*public NicknameAlreadyUsed() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel message = new JLabel("You can't use this nickname!");
        message.setHorizontalAlignment(JLabel.CENTER);
        JLabel usedNicknames = new JLabel("Nicknames already used are: ");
        usedNicknames.setHorizontalAlignment(JLabel.CENTER);
        panel.add(message);
        panel.add(usedNicknames);

        this.add(panel);
        this.setSize(350, 200);
        this.setVisible(true);
    }*/
}
