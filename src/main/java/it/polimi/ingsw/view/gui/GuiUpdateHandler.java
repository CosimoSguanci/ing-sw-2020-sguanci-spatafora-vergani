package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.UpdateHandler;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GuiUpdateHandler implements UpdateHandler {

    private Gui guiInstance;
    private Controller controller;

    GuiUpdateHandler(Gui guiInstance, Controller controller) {
        this.guiInstance = guiInstance;
        this.controller = controller;
    }

    public void handle(GamePhaseUpdate update) {

        guiInstance.setCurrentGamePhase(update.newGamePhase);

        switch(update.newGamePhase) {
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
        }
    }

    public void handle(TurnUpdate update) {
        guiInstance.forwardNotify(update);
        guiInstance.onTurnChanged();
    }

    public void handle(InitialInfoUpdate update) {

        if (update.getInitialInfo().size() == guiInstance.getPlayersNumber()) {
            guiInstance.setPlayersColors(update.getInitialInfo());
        }
        else if(controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) {
            List<String> selectedNicknames = new ArrayList<>(update.getInitialInfo().keySet());
            List<PrintableColor> selectedColors = new ArrayList<>(update.getInitialInfo().values());
            List<PrintableColor> selectableColors = PrintableColor.getColorList().stream().filter(color -> !selectedColors.contains(color)).collect(Collectors.toList());

            guiInstance.setSelectableColors(selectableColors);
            guiInstance.setSelectedNicknames(selectedNicknames);

            guiInstance.showInitialInfoOnTurn();
        }
    }

    public void handle(GodsUpdate update) {

        if (update.getSelectedGods().size() == guiInstance.getPlayersNumber()) {
            guiInstance.setPlayersGods(update.getSelectedGods());
        } else if(controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) {
            if(controller.isClientPlayerGodChooser()) {
                List<String> selectableGods = View.getGodsNamesList();
                guiInstance.setSelectableGods(selectableGods);
            }
            else {
                guiInstance.setSelectableGods(update.getSelectableGods());
            }
            guiInstance.showGodsChoiceOnTurn();
        }
    }


    public void handle(MatchStartedUpdate update) {
        guiInstance.onBoardChanged(update.getBoard());
    }

    public void handle(BoardUpdate update) {
        guiInstance.onBoardChanged(update.getBoard());
    }

    public void handle(ErrorUpdate update) {
        if (!update.getCurrentPlayer().getPlayerID().equals(controller.getClientPlayerID())) return;

        guiInstance.showError(update);
    }

    public void handle(WinUpdate update) {
        guiInstance.showWinMessageDialog(update);
    }

    public void handle(LoseUpdate update) {
        guiInstance.showLoseMessageDialog(update);
    }

    public void handle(DisconnectedPlayerUpdate update) {
        guiInstance.showDisconnectedPlayerDialog(update);
    }

    public void handle(ServerUnreachableUpdate update) {
        guiInstance.showServerUnreachableDialog();
    }
}
