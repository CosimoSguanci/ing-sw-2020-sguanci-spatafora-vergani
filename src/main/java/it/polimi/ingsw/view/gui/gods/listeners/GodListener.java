package it.polimi.ingsw.view.gui.gods.listeners;

import it.polimi.ingsw.view.gui.components.RealGame;

public abstract class GodListener {

    protected RealGame realGameComponent;

    protected GodListener(RealGame realGameComponent) {
        this.realGameComponent = realGameComponent;
    }
}
