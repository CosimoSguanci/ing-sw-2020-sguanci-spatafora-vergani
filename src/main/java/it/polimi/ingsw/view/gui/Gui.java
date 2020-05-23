package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.UpdateHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.components.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class Gui extends View implements Observer<Update> {

    private static JFrame frame;
    private static JPanel currentPanel;

    private UpdateHandler guiUpdateHandler;
    private Client client;
    private Controller controller;

    private int playersNumber;

    private InitialInfo initialInfoComponent;
    private GodChoice godsChoiceComponent;
    // etc

    private static Gui guiInstance = null;

    public static Gui getInstance(Client client, Controller controller) {

        if(guiInstance == null) {
            guiInstance = new Gui(client, controller);
        }

        return guiInstance;
    }

    public void setPlayersNumber(int playersNumberSelected) {
        playersNumber = playersNumberSelected;
    }

    public JFrame getMainFrame() {
        return frame;
    }

    public Client getClient() {
        return this.client;
    }

    private Gui(Client clientInstance, Controller controllerInstance){
        client = clientInstance;
        controller = controllerInstance;
        this.guiUpdateHandler = new GuiUpdateHandler(this, controller);
    }

    public void start() throws IOException{
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    showGui();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void showGui() throws IOException {
        frame = new JFrame("Santorini");

        currentPanel = new PlayerNumberChoice();

        frame.add(currentPanel);
        //frame.add(new WaitingForAMatch());
        //frame.add(new InitialInfo());
        //frame.add(new GameManual());
        //frame.add(new GodChoice(3));
        //frame.add(new BoardScreen(null));
        //JOptionPane.showMessageDialog(frame, NicknameAlreadyUsed.getMessage(), NicknameAlreadyUsed.title, JOptionPane.ERROR_MESSAGE);

        frame.pack();
        frame.setSize(600, 400);
        frame.setIconImage(ImageIO.read(Gui.class.getResource("/images/InitialInfo/title_island.png")));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void update(Update update) {
        update.handleUpdate(this.guiUpdateHandler);
    }

    void forwardNotify(Update update) { // forwards update to client-side Controller
        notify(update);
    }

    void onTurnChanged() {
        if(currentPanel instanceof AbstractInitialChoice) {
            ((AbstractInitialChoice) currentPanel).onTurnChanged();
        }
    }


    void startInitialInfoPhase() {
        frame.remove(currentPanel);
        this.initialInfoComponent = new InitialInfo(this.controller);
        currentPanel = this.initialInfoComponent;
        frame.add(currentPanel);
        frame.revalidate();
    }

    void startGodChoicePhase(boolean isGodChooser) throws IOException {
        frame.remove(currentPanel);
        this.godsChoiceComponent = new GodChoice(playersNumber, controller, isGodChooser);
        currentPanel = this.godsChoiceComponent;
        frame.add(currentPanel);
        frame.revalidate();
    }

    void showInitialInfoOnTurn() {
        this.initialInfoComponent.showGuiOnTurn();
    }

    void showGodsChoiceOnTurn() {
        this.godsChoiceComponent.showGuiOnTurn();
    }

    public void startWaitingForMatch() {

        try {
            client.sendPlayersNumber(playersNumber);

            String playerID = client.readPlayerID();

            controller.setClientPlayerID(playerID);

            client.setupUpdateListener();
            client.getUpdateListener().addObserver(this);

        } catch(IOException e) {
            e.printStackTrace();
        }



        frame.remove(currentPanel);
        currentPanel = new WaitingForAMatch();
        frame.add(currentPanel);
        frame.revalidate();
    }


    void setSelectableColors(List<PrintableColor> selectableColors) {
        this.initialInfoComponent.setSelectableColors(selectableColors);
    }

    void setSelectedNicknames(List<String> selectedNicknames) {
        this.initialInfoComponent.setSelectedNicknames(selectedNicknames);
    }

    void setSelectableGods(List<String> selectableGods) {
        this.godsChoiceComponent.setSelectableGods(selectableGods);
    }

}
