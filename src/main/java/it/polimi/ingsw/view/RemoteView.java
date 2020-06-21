package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.network.server.ClientHandler;

import java.io.IOException;


public class RemoteView extends Observable<Command> implements Observer<Update>{

    private final ClientHandler clientHandler;

    void handleCommand(Command command) {
        notify(command);
    }

    private class CommandReceiver implements Observer<Command> {

        @Override
        public void update(Command command) {
           handleCommand(command);
        }

    }


    public RemoteView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        clientHandler.addObserver(new CommandReceiver());
    }


    @Override
    public void update(Update update) {
        clientHandler.sendUpdate(update);
    }
}
