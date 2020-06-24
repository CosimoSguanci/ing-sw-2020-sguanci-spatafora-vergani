package it.polimi.ingsw.view.gui.gods;

import it.polimi.ingsw.view.gui.components.RealGame;
import it.polimi.ingsw.view.gui.gods.listeners.AtlasListener;

import javax.swing.*;
import java.awt.*;

/**
 * This class is the Strategy-pattern application of the interface GodGuiDrawer for Atlas.
 * This class allows to create, only for Atlas, a button with his special power.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class AtlasGuiStrategy implements GodGuiDrawer {

    /**
     * This method manages the creation of a new button, which is absent in all the
     * other Gods display through which is viewable the possibility to play a special
     * command using Atlas' power.
     *
     * @param realGameComponent contains a reference to the realComponent of the gui
     * @return a new JComponent that allow to perform a command with Atlas' special power
     */
    @Override
    public JComponent draw(RealGame realGameComponent) {

        ImageIcon domeIcon = new ImageIcon( getClass().getResource("/images/RealGame/build_dome.png"));
        domeIcon = new ImageIcon(domeIcon.getImage().getScaledInstance(RealGame.dimension, -1, Image.SCALE_SMOOTH));

        JButton buildDomeBtn = new JButton("<html>Build<br />Dome</html", domeIcon);

        buildDomeBtn.addActionListener(new AtlasListener(realGameComponent));

        buildDomeBtn.setMinimumSize(new Dimension(RealGame.buttonWidth, RealGame.buttonHeight));
        buildDomeBtn.setPreferredSize(new Dimension(RealGame.buttonWidth, RealGame.buttonHeight));
        buildDomeBtn.setMaximumSize(new Dimension(RealGame.buttonWidth, RealGame.buttonHeight));

        return buildDomeBtn;
    }
}
