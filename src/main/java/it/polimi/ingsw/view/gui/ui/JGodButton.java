package it.polimi.ingsw.view.gui.ui;

import javax.swing.*;

/**
 * This class defines a button which contains information about
 * the God of a single player.
 *
 * @author Roberto Spatafora
 * @author Andrea Vergani
 * @author Cosimo Sguanci
 */
public class JGodButton extends JButton {
    private final String godName;

    /**
     * This is a constructor of the class.
     *
     * @param text    is a string which indicates the nickname of a player involved in the match
     * @param godName is a string which indicates the name of a God associated to the player who has the nickname specified in text
     */
    public JGodButton(String text, String godName) {
        super(text);
        this.godName = godName;
    }

    /**
     * This is another constructor of the class.
     *
     * @param text    generically contains a String
     * @param image   contains a reference to the image related to a specific God
     * @param godName is a String which contains the name of a related God.
     */
    public JGodButton(String text, ImageIcon image, String godName) {
        super(text, image);
        this.godName = godName;
    }

    /**
     * This method is simply a getter of the name of God associated to the button instance
     *
     * @return a String which indicates the name of a God.
     */
    public String getGodName() {
        return this.godName;
    }
}
