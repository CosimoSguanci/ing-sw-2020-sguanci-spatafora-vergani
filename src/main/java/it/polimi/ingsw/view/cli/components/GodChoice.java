package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.exceptions.WrongPlayerException;
import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.view.cli.Cli;

import java.util.ArrayList;

/**
 * This class deals with CHOOSE_GODS game phase.
 * It manages all the choices made by GodChooser and the other players.
 *
 * @author Andrea Vergani
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 */
public class GodChoice {
    private final Cli cli;

    /**
     * This is the constructor of this class. At the moment of the creation
     * of a single instance of GodChoice the cli associated to it is set
     * @param cli contains reference to the Cli associated
     */
    public GodChoice (Cli cli) {
        this.cli = cli;
    }

    /**
     * This method manages all the commands, during CHOOSE_GODS game phase,
     * received by GodChooser. It checks if the GodChooser select as many
     * valid Gods as many players involved in the match.
     * @param splitCommand is an array of strings which contains, separately,
     *                     all the different words entered in the console.
     *                     It is expected in a particular format, therefore
     *                     it is parsed in this method.
     */
    public void handleIsGodChooserGodsChoice(String[] splitCommand) {
        if (CommandType.parseCommandType(splitCommand[0]) != CommandType.SELECT || splitCommand.length != (cli.getPlayersNumber() + 1)) {
            cli.println("You have to use the SELECT command, and type " + cli.getPlayersNumber() + " names of gods separated by spaces");
            throw new BadCommandException();
        }

        ArrayList<String> chosenGods = new ArrayList<>();

        for (int i = 0; i < cli.getPlayersNumber(); i++) {
            String god = splitCommand[i + 1];

            if (!isValidGod(god, chosenGods)) {
                throw new BadCommandException();
            }

            chosenGods.add(god);
        }

        GodChoiceCommand godChoiceCommand = new GodChoiceCommand(chosenGods); // true
        cli.notify(godChoiceCommand);

        cli.println("Wait for other players to choose their gods...");
    }

    /**
     * This method manages all the commands, during CHOOSE_GODS game phase,
     * received by non-GodChooser players. It checks if the single player
     * select a single valid God.
     * @param splitCommand is an array of strings which contains, separately,
     *                     all the different words entered in the console.
     *                     It is expected in a particular format, therefore
     *                     it is parsed in this method.
     */
    public void handleGodChoice(String[] splitCommand) {
        if (CommandType.parseCommandType(splitCommand[0]) != CommandType.SELECT || splitCommand.length > 2) {
            throw new BadCommandException();
        }

        String god = splitCommand[1];
        if (cli.getSelectableGods() != null) {
            if (!cli.getSelectableGods().contains(god)) {
                throw new BadCommandException();
            }
        } else {throw new WrongPlayerException();}

        ArrayList<String> selected = new ArrayList<>();
        selected.add(god);
        GodChoiceCommand godChoiceCommand = new GodChoiceCommand(selected); // false
        cli.notify(godChoiceCommand);

        cli.println("Wait for other players to choose their gods...");
    }


    /**
     * This method checks if a God chosen by a Client is a valid one.
     * There is a check for the correct name of a God received. Moreover
     * there is a check in order to control if God received is already chosen
     * from a different player.
     * @param god is a String which indicates the name of a God chosen by a client
     * @param chosenGods contains different Gods chosen by other players of the match
     * @return true if the God received is a selectable God and was not already chosen from another player.
     */
    private boolean isValidGod(String god, ArrayList<String> chosenGods) {
        return GodsUtils.isValidGod(god) && (chosenGods == null || !chosenGods.contains(god));
    }
}
