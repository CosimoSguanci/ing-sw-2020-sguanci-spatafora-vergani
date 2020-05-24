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


        try {

            switch(update.newGamePhase) {
                case INITIAL_INFO:
                    guiInstance.startInitialInfoPhase();
                    break;
                case CHOOSE_GODS:
                    guiInstance.startGodChoicePhase(controller.isClientPlayerGodChooser());
                    break;
            }

        } catch(IOException e) {
            // todo handle
        }
    }

    public void handle(TurnUpdate update) {
        guiInstance.forwardNotify(update);
    //    guiInstance.onTurnChanged();

        /*if (controller.getCurrentPlayerID().equals(controller.getClientPlayerID()) && cliInstance.getCurrentPhase() == GamePhase.GAME_PREPARATION) {
            cliInstance.printGamePreparationInfo();
        }*/
    }

    public void handle(InitialInfoUpdate update) {

        if(controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) {
            List<String> selectedNicknames = new ArrayList<>(update.getInitialInfo().keySet());
            List<PrintableColor> selectedColors = new ArrayList<>(update.getInitialInfo().values());
            List<PrintableColor> selectableColors = PrintableColor.getColorList().stream().filter(color -> !selectedColors.contains(color)).collect(Collectors.toList());

            guiInstance.setSelectableColors(selectableColors);
            guiInstance.setSelectedNicknames(selectedNicknames);

            guiInstance.showInitialInfoOnTurn();
        }
    }

    public void handle(GodsUpdate update) {
        // todo print God Mapping
        if(controller.getCurrentPlayerID().equals(controller.getClientPlayerID())) {
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


    public void handle(MatchStartedUpdate update) {}
    public void handle(BoardUpdate update) {}
    public void handle(ErrorUpdate update) {}

    public void handle(WinUpdate update) {}
    public void handle(LoseUpdate update) {}
    public void handle(ServerUnreachableUpdate update) {}
    public void handle(DisconnectedPlayerUpdate update) {}
}
