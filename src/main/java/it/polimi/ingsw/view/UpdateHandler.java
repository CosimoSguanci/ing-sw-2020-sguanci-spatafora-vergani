package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.network.client.controller.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This abstract class contains the signature to force the concrete UpdateHandler to implement the methods to handle
 * all possible updates that can be received by the {@link View} from the Server, by using a Visitor Pattern.
 * This class represent in practice the "abstract Visitor", while {@link it.polimi.ingsw.view.cli.CliUpdateHandler} and
 * {@link it.polimi.ingsw.view.gui.GuiUpdateHandler} are the "concrete Visitors".
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Mario Vergani
 */
public abstract class UpdateHandler {

    /**
     * Client's {@link Controller} linked to the {@link View} instance
     */
    protected final Controller controller;

    protected UpdateHandler(Controller controller) {
        this.controller = controller;
    }

    public abstract void handle(GamePhaseUpdate update);

    public abstract void handle(InitialInfoUpdate update);

    public abstract void handle(MatchStartedUpdate update);

    public abstract void handle(GodsUpdate update);

    public abstract void handle(BoardUpdate update);

    public abstract void handle(ErrorUpdate update);

    public abstract void handle(TurnUpdate update);

    public abstract void handle(WinUpdate update);

    public abstract void handle(LoseUpdate update);

    public abstract void handle(ServerUnreachableUpdate update);

    public abstract void handle(DisconnectedPlayerUpdate update);

    /**
     * Utility method used by {@link it.polimi.ingsw.view.cli.CliUpdateHandler} and {@link it.polimi.ingsw.view.gui.GuiUpdateHandler}
     * to take the already selected nicknames contained in {@link InitialInfoUpdate}.
     *
     * @param update the {@link InitialInfoUpdate} notified by the {@link it.polimi.ingsw.view.cli.Cli}
     * @return the list of the already selected nicknames.
     */
    protected List<String> extractSelectedNicknames(InitialInfoUpdate update) {
        return new ArrayList<>(update.getInitialInfo().keySet());
    }

    /**
     * Utility method used by {@link it.polimi.ingsw.view.cli.CliUpdateHandler} and {@link it.polimi.ingsw.view.gui.GuiUpdateHandler}
     * to take the already selected colors from {@link InitialInfoUpdate}.
     *
     * @param update the {@link InitialInfoUpdate} notified by the {@link it.polimi.ingsw.view.cli.Cli}
     * @return the list of the already selected colors.
     */
    protected List<PrintableColor> extractSelectedColors(InitialInfoUpdate update) {
        return new ArrayList<>(update.getInitialInfo().values());
    }

    /**
     * Utility method used by {@link it.polimi.ingsw.view.cli.CliUpdateHandler} and {@link it.polimi.ingsw.view.gui.GuiUpdateHandler}
     * to take the selectable colors from {@link InitialInfoUpdate}.
     *
     * @param selectedColors the colors already selected by other players.
     * @return the list of the colors which are still selectabvle.
     */
    protected List<PrintableColor> extractSelectableColors(List<PrintableColor> selectedColors) {
        return PrintableColor.getColorList().stream().filter(color -> !selectedColors.contains(color)).collect(Collectors.toList());
    }
}
