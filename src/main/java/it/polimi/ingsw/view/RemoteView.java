package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

/**
 * This class is used as link between Server and View. There is an association with the ClientHandler of respective players.
 * RemoteView is observed by Controller and observes Model. For this reason, this class extends Observable and implements Observer.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class RemoteView extends Observable<Command> implements Observer<Update> {

    private final ClientHandler clientHandler;

    /**
     * This is the constructor of the class. At the moment of creation the ClientHandler associated to
     * each Client with a sustainable connection is set in the constructor.
     *
     * @param clientHandler contains a reference to the handler of a specific Client related to particular instance of RemoteView
     */
    public RemoteView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        clientHandler.addObserver(new CommandReceiver());
    }

    /**
     * This method manages the activities when a command is received.
     * It notifies this command to its observers.
     *
     * @param command
     */
    void handleCommand(Command command) {
        notify(command);
    }

    @Override
    public void update(Update update) {
        this.clientHandler.sendUpdate(update);
    }

    /**
     * This private class defines an observer component that once received a command
     * invokes RemoteView handleCommand which notifies the received command.
     */
    private class CommandReceiver implements Observer<Command> {

        @Override
        public void update(Command command) {
            handleCommand(command);
        }

    }
}
