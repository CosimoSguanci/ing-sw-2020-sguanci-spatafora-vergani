package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.exceptions.InvalidColorException;
import it.polimi.ingsw.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.exceptions.WrongPlayerException;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.view.cli.Cli;

import java.util.stream.Collectors;

/**
 * This class deals with INITIAL_INFO game phase. It manages all
 * the choices made by players, including nicknames and colors
 */
public class InitialInfo {

    private final Cli cli;

    /**
     * This is the constructor of this class. At the moment of the creation
     * of a single instance of InitialInfo the cli associated to it is set
     * @param cli contains reference to the Cli associated
     */
    public InitialInfo(Cli cli){
        this.cli = cli;
    }

    /**
     * This method handles all the commands received during INITIAL_INFO game phase.
     * It checks if a specified nickname or color has not already
     * been chosen from another player.
     * @param splitCommand is an array of strings which contains, separately,
     *                     all the different words entered in the console.
     *                     It is expected in a particular format, therefore
     *                     it is parsed in this method.
     */
    public void handleInitialInfoCommand(String[] splitCommand) {
        if (CommandType.parseCommandType(splitCommand[0]) != CommandType.PICK || splitCommand.length != 3) {
            throw new BadCommandException();
        }

        String nickname = splitCommand[1];

        if (cli.getSelectedNicknames() != null) {
            if (cli.getSelectedNicknames().stream().map(String::toLowerCase).collect(Collectors.toList()).contains(nickname)) {
                throw new NicknameAlreadyTakenException();
            }
        } else {
            throw new WrongPlayerException();
        }

        String color = splitCommand[2];

        if(!PrintableColor.isValidColor(color)) {
            cli.println("Not a valid color");
            throw new BadCommandException();
        }

        PrintableColor actualColor = Enum.valueOf(PrintableColor.class, color.toUpperCase());

        if(!cli.getSelectableColors().contains(actualColor)) {
            throw new InvalidColorException();
        }

        InitialInfoCommand initialInfoCommand = new InitialInfoCommand(nickname, actualColor);
        cli.notify(initialInfoCommand);

        cli.newLine();
        cli.println("Wait for other players to choose their nicknames and colors...");
        cli.newLine();
    }
}
