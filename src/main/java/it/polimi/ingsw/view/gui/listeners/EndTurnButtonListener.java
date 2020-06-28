package it.polimi.ingsw.view.gui.listeners;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.components.RealGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is the listener of the EndTurnButton
 * which is constantly present in RealGame phase
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class EndTurnButtonListener implements ActionListener {

    private final RealGame realGameComponent;

    /**
     * This is the constructor of the class. At the moment of creation
     * an association between the listener and the RealGame component is made.
     *
     * @param realGameComponent contains a reference to the RealGame component.
     */
    public EndTurnButtonListener(RealGame realGameComponent) {
        this.realGameComponent = realGameComponent;
    }

    /**
     * This method is invoked at the moment of clicking on the EndTurnButton.
     * When the EndTurnButton is clicked it generates a new END_TURN PlayerCommand.
     *
     * @param e contains a reference to the EndTurnButton
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        PlayerCommand playerCommand = new PlayerCommand(CommandType.END_TURN, null, -1, -1, null);
        Gui.getInstance().notify(playerCommand);
        this.realGameComponent.setLastCommand(CommandType.END_TURN);

    }
}
