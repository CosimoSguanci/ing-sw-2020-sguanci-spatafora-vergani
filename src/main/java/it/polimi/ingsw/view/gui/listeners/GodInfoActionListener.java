package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.view.gui.ui.JGodButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * This class is the listener of a button which indicates a player wants
 * information about a specific God.
 * Once clicked it will display a Dialog which contains info about the specified God.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class GodInfoActionListener implements ActionListener {
    private final Container container;

    /**
     * This is the creator of the class. At the moment of creation an association
     * between the listener and the component in which is contained is made.
     *
     * @param container contains a reference to the component which contains the lisener
     */
    public GodInfoActionListener(Container container) {
        this.container = container;
    }

    /**
     * This method is invoked when a particular God is clicked in GodInfo component.
     * When a button is clicked it displays a Dialog in which is specified the power of the selected God.
     *
     * @param e contains a reference to the button clicked.
     */
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
