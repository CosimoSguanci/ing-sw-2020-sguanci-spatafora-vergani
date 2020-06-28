package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;


public class RemoteView extends Observable<Command> implements Observer<Update> {

    private final ClientHandler clientHandler;

    public RemoteView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        clientHandler.addObserver(new CommandReceiver());
    }

    void handleCommand(Command command) {
        notify(command);
    }

    @Override
    public void update(Update update) {
        clientHandler.sendUpdate(update);
    }

    private class CommandReceiver implements Observer<Command> {

        @Override
        public void update(Command command) {
            handleCommand(command);
        }

    }
}
