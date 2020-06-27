package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.CommandHandler;

import java.util.List;


/**
 * GodChoiceCommand is the class that represents a Command specific for GodChoice
 * phase. In particular, in this phase a god-chooser selects a number of gods equal
 * to the number of players involved in the match; then, every player chooses the god
 * he/she likes most.
 *
 * @author Andrea Mario Vergani
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 */
public class GodChoiceCommand extends Command {
    private final List<String> chosenGods;


    /**
     * The constructor creates a Command specific for GodChoice phase: CommandType is SELECT
     * because this is what players must do in this phase; then, the god/gods are selected.
     *
     * @param chosenGods the list of gods chosen by the player: this list contains more than one
     *                   element only if the player is god-chooser; otherwise, only one god can
     *                   be selected
     */
    public GodChoiceCommand(List<String> chosenGods) {
        super(CommandType.SELECT);
        this.chosenGods = chosenGods;
    }


    /**
     * This method is the getter for the list of chosen gods.
     *
     * @return the list of gods chosen by the player (only one god if he/she is not god-chooser)
     */
    public List<String> getChosenGods() {
        return chosenGods;
    }


    /**
     * This method handles the GodChoiceCommand received by Server in the proper way.
     *
     * @param handler the object that is going to handle the command
     */
    @Override
    public void handleCommand(CommandHandler handler) {
        handler.handle(this);
    }
}
