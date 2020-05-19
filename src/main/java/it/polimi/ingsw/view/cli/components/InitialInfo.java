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

public class InitialInfo {

    private final Cli cli;

    public InitialInfo(Cli cli){
        this.cli = cli;
    }

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
            cli.print("Not a valid color");
            throw new BadCommandException();
        }

        PrintableColor actualColor = Enum.valueOf(PrintableColor.class, color.toUpperCase());

        if(!cli.getSelectableColors().contains(actualColor)) {
            throw new InvalidColorException();
        }

        InitialInfoCommand initialInfoCommand = new InitialInfoCommand(nickname, actualColor);
        cli.notify(initialInfoCommand);

        cli.newLine();
        cli.print("Wait for other players to choose their nicknames and colors...");
        cli.newLine();
    }
}
