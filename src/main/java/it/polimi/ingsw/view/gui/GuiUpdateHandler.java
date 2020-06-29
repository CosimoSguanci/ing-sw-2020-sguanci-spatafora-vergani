package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.UpdateHandler;
import it.polimi.ingsw.view.View;

import java.util.List;
import java.util.Map;

/**
 * This class handles all the updates from server to different clients.
 * Every possible updates go to this class before it's viewable from users (in the GUI).
 * BoardUpdate, DisconnectedPlayerUpdate, ErrorUpdate, GamePhaseUpdate,
 * GodsUpdate, InitialInfoUpdate, LoseUpdate, MatchStartedUpdate,
 * ServerUnreachableUpdate, TurnUpdate, WinUpdate are all managed in this class.
 * <p>
 * This class represent the Visitor in the Visitor Pattern, it implements {@link UpdateHandler}
 * to have all the necessary methods to handle all possible updates.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Vergani
 * @see UpdateHandler
 */
public class GuiUpdateHandler extends UpdateHandler {

    /**
     * Instance of the {@link Gui} which this handler has to update
     */
    private final Gui guiInstance;

    /**
     * This is the creator of the class. At the moment of the instance creation
     * an instance of {@link Gui} and {@link Controller}, to which the class refers, are set.
     *
     * @param guiInstance is the instance of {@link Gui} associated to the CliUpdateHandle for updates.
     * @param controller  is the instance of the controller that is observed by the {@link Gui}.
     */
    GuiUpdateHandler(Gui guiInstance, Controller controller) {
        super(controller);
        this.guiInstance = guiInstance;
    }

    /**
     * This method handles {@link GamePhaseUpdate} arrived from the {@link Gui}.
     * Current game phase setter is invoked, and the correct method of {@link Gui} is called
     * to start the new game phase and show the correct content to the user.
     *
     * @param update is the instance of {@link GamePhaseUpdate} from server.
     * @see Gui#startInitialInfoPhase()
     * @see Gui#startGodChoicePhase()
     * @see Gui#startGamePreparation()
     * @see Gui#startRealGame()
     * @see Gui#onMatchFinished(GamePhase)
     */
    public void handle(GamePhaseUpdate update) {

        guiInstance.setCurrentGamePhase(update.newGamePhase);

        switch (update.newGamePhase) {
            case INITIAL_INFO:
                guiInstance.startInitialInfoPhase();
                break;
            case CHOOSE_GODS:
                guiInstance.startGodChoicePhase();
                break;
            case GAME_PREPARATION:
                guiInstance.startGamePreparation();
                break;
            case REAL_GAME:
                guiInstance.startRealGame();
                break;
            case MATCH_ENDED:
            case MATCH_LOST:
                guiInstance.onMatchFinished(update.newGamePhase);

        }
    }

    /**
     * This method handles {@link TurnUpdate} arrived from the {@link Gui}.
     * The update is notified to the client side {@link Controller} to let it keep
     * a reference to the current player in turn. Moreover, the {@link Gui#onTurnChanged()}
     * callback is fired to update the UI.
     *
     * @param update is the instance of {@link TurnUpdate} from server.
     * @see Gui#onTurnChanged()
     */
    public void handle(TurnUpdate update) {
        guiInstance.forwardNotify(update);
        guiInstance.onTurnChanged();
    }

    /**
     * This method handles {@link InitialInfoUpdate} arrived from the {@link Gui}.
     * If initial info for all players have been chosen, this information is showed to
     * the user. Otherwise, the {@link Gui} let the user choose its initial info.
     *
     * @param update is the instance of {@link InitialInfoUpdate} from server.
     * @see Gui#setPlayersColors(Map)
     * @see Gui#showInitialInfoOnTurn()
     */
    public void handle(InitialInfoUpdate update) {

        if (update.getInitialInfo().size() == guiInstance.getPlayersNumber()) {
            guiInstance.setPlayersColors(update.getInitialInfo());
        } else if (controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) {

            List<String> selectedNicknames = extractSelectedNicknames(update);
            List<PrintableColor> selectedColors = extractSelectedColors(update);
            List<PrintableColor> selectableColors = extractSelectableColors(selectedColors);

            guiInstance.setSelectableColors(selectableColors);
            guiInstance.setSelectedNicknames(selectedNicknames);

            guiInstance.showInitialInfoOnTurn();
        }
    }

    /**
     * This method handles {@link GodsUpdate} arrived from the {@link Gui}.
     * If gods for all players have been chosen, this information is showed to
     * the user. Otherwise, the {@link Gui} let the user choose its god.
     *
     * @param update is the instance of {@link GodsUpdate} from server.
     * @see Gui#setPlayersGods(Map)
     * @see Gui#showGodsChoiceOnTurn()
     */
    public void handle(GodsUpdate update) {

        if (update.getSelectedGods().size() == guiInstance.getPlayersNumber()) {
            guiInstance.setPlayersGods(update.getSelectedGods());
        } else if (controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) {
            if (controller.isClientPlayerGodChooser()) {
                List<String> selectableGods = View.getGodsNamesList();
                guiInstance.setSelectableGods(selectableGods);
            } else {
                guiInstance.setSelectableGods(update.getSelectableGods());
            }
            guiInstance.showGodsChoiceOnTurn();
        }
    }

    /**
     * This method handles {@link MatchStartedUpdate} arrived from the {@link Gui}.
     * {@link Gui#onBoardChanged(String)} callback is invoked, in order to update the user
     * interface with the new {@link it.polimi.ingsw.model.Board} associated with the update.
     *
     * @param update is the instance of {@link MatchStartedUpdate} from server.
     * @see Gui#onBoardChanged(String)
     */
    public void handle(MatchStartedUpdate update) {
        guiInstance.onBoardChanged(update.getBoard());
    }

    /**
     * This method handles {@link BoardUpdate} arrived from the {@link Gui}.
     * {@link Gui#onBoardChanged(String)} callback is invoked, in order to update the user
     * interface with the new {@link it.polimi.ingsw.model.Board} associated with the update.
     *
     * @param update is the instance of {@link BoardUpdate} from server.
     * @see Gui#onBoardChanged(String)
     */
    public void handle(BoardUpdate update) {
        guiInstance.onBoardChanged(update.getBoard());
    }

    /**
     * This method handles {@link ErrorUpdate} arrived from the {@link Gui}.
     * If the error was not caused by this player, nothing gets done. Otherwise,
     * the {@link Gui} shows an error dialog to the user.
     *
     * @param update is the instance of {@link ErrorUpdate} from server.
     * @see Gui#showError(ErrorUpdate)
     */
    public void handle(ErrorUpdate update) {
        if (!update.getCurrentPlayer().getPlayerID().equals(controller.getClientPlayerID())) return;

        guiInstance.showError(update);
    }

    /**
     * This method handles {@link WinUpdate} arrived from the {@link Gui}.
     * A win message dialog is shown to the user (the content will change depending on
     * who actually won the match).
     *
     * @param update is the instance of {@link WinUpdate} from server.
     * @see Gui#showWinMessageDialog(WinUpdate)
     */
    public void handle(WinUpdate update) {
        guiInstance.showWinMessageDialog(update);
    }

    /**
     * This method handles {@link LoseUpdate} arrived from the {@link Gui}.
     * A lost message dialog is shown to the user (the content will change depending on
     * how many players are still in game).
     *
     * @param update is the instance of {@link LoseUpdate} from server.
     * @see Gui#showLoseMessageDialog(LoseUpdate)
     */
    public void handle(LoseUpdate update) {
        guiInstance.showLoseMessageDialog(update);
    }

    /**
     * This method handles {@link DisconnectedPlayerUpdate} arrived from the {@link Gui}.
     * if the game phase is not {@link GamePhase#MATCH_ENDED} an error dialog is shown to the user, which
     * will decide to play another match or quit the game.
     *
     * @param update is the instance of {@link DisconnectedPlayerUpdate} from server.
     * @see Gui#showDisconnectedPlayerDialog(DisconnectedPlayerUpdate)
     */
    public void handle(DisconnectedPlayerUpdate update) {
        if (guiInstance.getCurrentPhase() != GamePhase.MATCH_ENDED && guiInstance.showDisconnectedDialog) {
            guiInstance.showDisconnectedPlayerDialog(update);
        }
    }

    /**
     * This method handles {@link ServerUnreachableUpdate} arrived from the {@link it.polimi.ingsw.network.client.UpdateListener}.
     * An error dialog is shown to the user, and the game exits because communication with server it's not possible at the moment.
     *
     * @param update is the instance of {@link ServerUnreachableUpdate} from server.
     * @see Gui#showServerUnreachableDialog()
     */
    public void handle(ServerUnreachableUpdate update) {
        guiInstance.showServerUnreachableDialog();
    }
}
