package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.updates.Update;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.UpdateHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.components.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

public class Gui extends View implements Observer<Update> {

    public static final int FONT_REGULAR = 0;
    public static final int FONT_BOLD = 1;

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

        //USED FOR BOARD'S VISUALIZATION
        Match match = new Match(2);
        Model model = new Model(match);
        Board board = match.getMatchBoard();
        Player player1 = new Player("ID1", model, match);
        player1.setColor(PrintableColor.BLUE);
        Player player2 = new Player("ID2", model, match);
        player2.setColor(PrintableColor.GREEN);
        match.addPlayer(player1);
        match.addPlayer(player2);
        board.getCell(1,3).setLevel(BlockType.LEVEL_THREE);
        board.getCell(4,4).setLevel(BlockType.LEVEL_ONE);
        board.getCell(0,0).setLevel(BlockType.GROUND);
        board.getCell(0,2).setLevel(BlockType.LEVEL_TWO);
        board.getCell(2,4).setLevel(BlockType.DOME);

        board.getCell(1,3).setWorker(player1.getWorkerFirst());
        board.getCell(2,2).setWorker(player1.getWorkerSecond());
        board.getCell(4,3).setWorker(player2.getWorkerFirst());
        board.getCell(1,1).setWorker(player2.getWorkerSecond());

        currentPanel = new BoardScreen(board.toString());


        currentPanel = new PlayerNumberChoice();
        frame.add(currentPanel);
        //frame.add(new WaitingForAMatch());
        //frame.add(new InitialInfo());
        //frame.add(new GameManual());
        //frame.add(new GodChoice(3));
        //frame.add(new BoardScreen(null));
        //JOptionPane.showMessageDialog(frame, NicknameAlreadyUsed.getMessage(), NicknameAlreadyUsed.title, JOptionPane.ERROR_MESSAGE);

        frame.pack();
        frame.setSize(850, 840);
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

    public static Font getFont(int fontMode, int size) {
        try {
            InputStream inputStream;

            switch(fontMode) {
                case FONT_BOLD:
                    inputStream = Gui.class.getResourceAsStream("/fonts/Lato-Bold.ttf");
                    break;
                case FONT_REGULAR:
                default:
                    inputStream = Gui.class.getResourceAsStream("/fonts/Lato-Regular.ttf");
                    break;
            }

            return Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(Font.PLAIN, size);
        } catch (FontFormatException | IOException ex) {
            return new Font(Font.SERIF, Font.BOLD, 14);
        }
    }

}
