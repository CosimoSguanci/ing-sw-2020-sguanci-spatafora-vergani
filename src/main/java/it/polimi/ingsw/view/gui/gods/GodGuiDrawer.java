package it.polimi.ingsw.view.gui.gods;

import it.polimi.ingsw.view.gui.components.RealGame;

import javax.swing.*;

/**
 * This interface defines a draw method through which classes
 * that implement GodGuiInterface override to define a particular component
 * for a specific God.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public interface GodGuiDrawer {
    JComponent draw(RealGame realGameComponent);
}
