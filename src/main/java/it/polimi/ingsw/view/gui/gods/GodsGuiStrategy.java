package it.polimi.ingsw.view.gui.gods;

import it.polimi.ingsw.view.gui.components.RealGame;

import javax.swing.*;

/**
 * This class is the Strategy-pattern application of the interface GodGuiDrawer for all Gods.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class GodsGuiStrategy implements GodGuiDrawer {

    /**
     * This method overrides the interface method draw
     * @param realGameComponent contains a reference to the realGameComponent of the gui
     * @return null generically for all Gods.
     */
    @Override
    public JComponent draw(RealGame realGameComponent) {
        return null;
    }
}
