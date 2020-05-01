package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.CommandHandler;
import it.polimi.ingsw.model.PrintableColour;


public class InitialInfoCommand extends Command {
    public final String nickname;
    public final PrintableColour color;

    public InitialInfoCommand(String nickname, PrintableColour color) {
        this.nickname = nickname;
        this.color = color;
    }

    @Override
    public void handleCommand(CommandHandler handler) {
        handler.handle(this);
    }
}
