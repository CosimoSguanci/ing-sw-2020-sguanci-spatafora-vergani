package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.CommandHandler;
import it.polimi.ingsw.model.PrintableColor;


public class InitialInfoCommand extends Command {
    public final String nickname;
    public final PrintableColor color;

    public InitialInfoCommand(String nickname, PrintableColor color) {
        super(CommandType.PICK);
        this.nickname = nickname;
        this.color = color;
    }

    @Override
    public void handleCommand(CommandHandler handler) {
        handler.handle(this);
    }
}
