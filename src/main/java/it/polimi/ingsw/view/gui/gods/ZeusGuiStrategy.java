package it.polimi.ingsw.view.gui.gods;

import it.polimi.ingsw.view.gui.components.RealGame;
import it.polimi.ingsw.view.gui.gods.listeners.ZeusListener;

import javax.swing.*;
import java.awt.*;

/**
 * This class is the Strategy-pattern application of the interface GodGuiDrawer for Zeus.
 * This class allows to create, only for Zeus, a button with his special power.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class ZeusGuiStrategy implements GodGuiDrawer {

    /**
     * This method manages the creation of a new button, which is absent in all the
     * other Gods display through which is viewable the possibility to play a special
     * command using Zeus' power.
     *
     * @param realGameComponent contains a reference to the realComponent of the gui
     * @return a new JComponent that allow to perform a command with Atlas' special power
     */
    @Override
    public JComponent draw(RealGame realGameComponent) {
        ImageIcon buildIcon = new ImageIcon( getClass().getResource("/images/RealGame/build_under_yourself.png"));
        buildIcon = new ImageIcon(buildIcon.getImage().getScaledInstance(RealGame.dimension, -1, Image.SCALE_SMOOTH));

        JButton buildBtn = new JButton("<html>Build<br />Under<br />Yourself</html", buildIcon);

        buildBtn.addActionListener(new ZeusListener(realGameComponent));

        buildBtn.setMinimumSize(new Dimension(RealGame.buttonWidth, RealGame.buttonHeight));
        buildBtn.setPreferredSize(new Dimension(RealGame.buttonWidth, RealGame.buttonHeight));
        buildBtn.setMaximumSize(new Dimension(RealGame.buttonWidth, RealGame.buttonHeight));

        return buildBtn;
    }
}
