package it.polimi.ingsw.view.gui.gods.listeners;

import it.polimi.ingsw.view.gui.components.RealGame;

/**
 * This abstract class is a generic GodListener.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public abstract class GodListener {

    protected RealGame realGameComponent;

    /**
     * This is the creator of the class. At the moment of a concrete class creation
     * it is necessary to have a reference of the realGameComponent.
     * @param realGameComponent contains a reference to the realGameComponent associated to gui
     */
    protected GodListener(RealGame realGameComponent) {
        this.realGameComponent = realGameComponent;
    }
}
