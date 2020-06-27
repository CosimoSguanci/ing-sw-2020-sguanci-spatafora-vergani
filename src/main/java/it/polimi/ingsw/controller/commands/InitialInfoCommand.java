package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.CommandHandler;
import it.polimi.ingsw.model.PrintableColor;


/**
 * InitialInfoCommand is the class that represents a Command specific for InitialInfo
 * phase. In particular, in this phase every player selects a nickname and a colour for
 * the match.
 *
 * @author Andrea Mario Vergani
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 */
public class InitialInfoCommand extends Command {
    public final String nickname;
    public final PrintableColor color;


    /**
     * The constructor creates a Command specific for InitialInfo phase: CommandType is PICK
     * because this is what players must do in this phase; then, a nickname and a colour are
     * selected.
     *
     * @param nickname the nickname chosen by the player
     * @param color    the colour chosen by the player
     */
    public InitialInfoCommand(String nickname, PrintableColor color) {
        super(CommandType.PICK);
        this.nickname = nickname;
        this.color = color;
    }


    /**
     * This method handles the InitialInfoCommand received by Server in the proper way.
     *
     * @param handler the object that is going to handle the command
     */
    @Override
    public void handleCommand(CommandHandler handler) {
        handler.handle(this);
    }
}
