package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.GamePhase;
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
import java.util.Map;

public class Gui extends View implements Observer<Update> {

    public static final int FONT_REGULAR = 0;
    public static final int FONT_BOLD = 1;

    private final static String PLAYERS_NUMBER_CHOICE = "players_number_choice";
    private final static String WAITING_FOR_MATCH = "waiting_for_match";
    private final static String INITIAL_INFO = "initial_info";
    private final static String GOD_CHOICE = "god_choice";
    private final static String GAME_PREPARATION = "game_preparation";
    private final static String REAL_GAME = "real_game";

    private JFrame frame;

    private final UpdateHandler guiUpdateHandler;
    private final Client client;
    private final Controller controller;

    private int playersNumber;

    private CardLayout mainCardLayout;
    private JPanel mainPanel;

    private GamePhase currentGamePhase;

    private PlayerNumberChoice playerNumberChoiceComponent;
    private WaitingForAMatch waitingForAMatchComponent;
    private InitialInfo initialInfoComponent;
    private GodChoice godsChoiceComponent;
    private GamePreparation gamePreparation;
    private RealGame realGame;


    private Map<String, String> playersGods;
    private Map<String, PrintableColor> playersColors;

    // etc

    private static Gui guiInstance = null;


    public static Gui getInstance(Client client, Controller controller) {

        if(guiInstance == null) {
            guiInstance = new Gui(client, controller);
        }

        return guiInstance;
    }

    public static Gui getInstance() {

        if(guiInstance == null) {
            return new Gui(null, null);
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

    public Controller getController() {
        return this.controller;
    }

    public int getPlayersNumber() {
        return this.playersNumber;
    }

    public void setPlayersColors(Map<String, PrintableColor> playersColors) {
        this.playersColors = playersColors;
    }

    public Map<String, PrintableColor> getPlayersColors() {
        return this.playersColors;
    }

    public void setPlayersGods(Map<String, String> playersGods) {
        this.playersGods = playersGods;
    }

    public Map<String, String> getPlayersGods() {
        return this.playersGods;
    }

    private Gui(Client clientInstance, Controller controllerInstance){
        client = clientInstance;
        controller = controllerInstance;
        this.guiUpdateHandler = new GuiUpdateHandler(this, controller);
    }

    public void start() throws IOException {
        SwingUtilities.invokeLater(() -> {
            try {
                showGui();
            } catch(IOException e) {
                //throw new IOException();
            }

        });
    }

    private void showGui() throws IOException {
        frame = new JFrame("Santorini");

        this.mainPanel = new JPanel();
        this.mainCardLayout = new CardLayout();

        mainPanel.setLayout(mainCardLayout);

        this.playerNumberChoiceComponent = new PlayerNumberChoice();
        this.waitingForAMatchComponent = new WaitingForAMatch();
        this.initialInfoComponent = new InitialInfo();
        this.godsChoiceComponent = new GodChoice();
        this.gamePreparation = new GamePreparation();
        this.realGame = new RealGame();

        mainPanel.add(playerNumberChoiceComponent, PLAYERS_NUMBER_CHOICE);
        mainPanel.add(waitingForAMatchComponent, WAITING_FOR_MATCH);
        mainPanel.add(initialInfoComponent, INITIAL_INFO);
        mainPanel.add(godsChoiceComponent, GOD_CHOICE);
        mainPanel.add(gamePreparation, GAME_PREPARATION);
        mainPanel.add(realGame, REAL_GAME);

       // mainPanel.add(godsChoiceComponent, GOD_CHOICE);

        frame.add(mainPanel);

        /*
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

        /*JPanel currentPanel = new BoardScreen(board.toString());

        frame.add(currentPanel);*/

        //useful for testing WIP panels
        //JPanel currentPanel = new GameManual();
        //frame.add(currentPanel);


        frame.pack();
        frame.setSize(900, 700);
        frame.setMinimumSize(new Dimension(500, 400));
        frame.setIconImage(ImageIO.read(Gui.class.getResource("/images/title_island.png")));
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


    public void startWaitingForMatch() {

        try {

            System.out.println("Players Number: " + playersNumber);

            client.sendPlayersNumber(playersNumber);

            String playerID = client.readPlayerID();

            System.out.println("Player ID: " + playerID);

            controller.setClientPlayerID(playerID);

            client.setupUpdateListener();
            client.getUpdateListener().addObserver(this);

        } catch(IOException e) {
            e.printStackTrace();
        }


        this.mainCardLayout.show(mainPanel, WAITING_FOR_MATCH);
    }


    void startInitialInfoPhase() {
        this.mainCardLayout.show(mainPanel, INITIAL_INFO);
    }

    void startGodChoicePhase() {
        this.mainCardLayout.show(mainPanel, GOD_CHOICE);
    }

    void startGamePreparation() {
        this.currentGamePhase = GamePhase.GAME_PREPARATION;
        this.mainCardLayout.show(mainPanel, GAME_PREPARATION);
    }

    void startRealGame() {
        this.currentGamePhase = GamePhase.REAL_GAME;
        this.mainCardLayout.show(mainPanel, REAL_GAME);
    }

    void showInitialInfoOnTurn() {
        this.initialInfoComponent.showGuiOnTurn();
    }

    void showGodsChoiceOnTurn() {
        this.godsChoiceComponent.showGuiOnTurn();
    }

    void setCurrentGamePhase(GamePhase gamePhase) {
        this.currentGamePhase = gamePhase;
    }

    void onTurnChanged() {
        switch(currentGamePhase) {
            case GAME_PREPARATION:
                this.gamePreparation.changeTurn();
                break;
            case REAL_GAME:
                this.realGame.changeTurn();
                break;
        }
    }

    void onBoardChanged(String board) {
        switch(currentGamePhase) {
            case GAME_PREPARATION:
                this.gamePreparation.setBoard(board);
                break;
            case REAL_GAME:
                this.realGame.setBoard(board);
                break;
        }
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
