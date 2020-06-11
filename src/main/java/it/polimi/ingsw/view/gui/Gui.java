package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.model.ErrorType;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.UpdateHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.components.*;
import it.polimi.ingsw.view.gui.gods.GodGuiDrawer;

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
    private static Gui guiInstance = null;
    private final UpdateHandler guiUpdateHandler;
    private final Client client;
    private final Controller controller;
    private JFrame frame;
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
    private GodGuiDrawer godGuiDrawer;

    private Gui(Client clientInstance, Controller controllerInstance) {
        client = clientInstance;
        controller = controllerInstance;
        this.guiUpdateHandler = new GuiUpdateHandler(this, controller);
        GodScreen.loadImages();
    }

    public static Gui getInstance(Client client, Controller controller) {

        if (guiInstance == null) {
            guiInstance = new Gui(client, controller);
        }

        return guiInstance;
    }

    public static Gui getInstance() {

        if (guiInstance == null) {
            return new Gui(null, null);
        }

        return guiInstance;
    }

    public static Font getFont(int fontMode, int size) {
        try {
            InputStream inputStream;

            switch (fontMode) {
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

    public void setPlayersNumber(int playersNumberSelected) {
        playersNumber = playersNumberSelected;
    }

    public Map<String, PrintableColor> getPlayersColors() {
        return this.playersColors;
    }

    public void setPlayersColors(Map<String, PrintableColor> playersColors) {
        this.playersColors = playersColors;
    }

    public Map<String, String> getPlayersGods() {
        return this.playersGods;
    }

    public void setPlayersGods(Map<String, String> playersGods) {
        this.playersGods = playersGods;
        this.godGuiDrawer = GodsUtils.godsGuiFactory(playersGods.get(controller.getClientPlayer().getNickname()));
    }

    public GodGuiDrawer getGodGuiDrawer() {
        return this.godGuiDrawer;
    }

    @Override
    public void start() {
        SwingUtilities.invokeLater(() -> {
            try {
                showGui();
            } catch (IOException ignored) {
            }
        });
    }

    private void showGui() throws IOException {
        frame = new JFrame("Santorini");

        this.mainPanel = new JPanel();
        this.mainCardLayout = new CardLayout();

        mainPanel.setLayout(mainCardLayout);

        initializeComponents();
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
        frame.setPreferredSize(new Dimension(800, 700));
        frame.setMinimumSize(frame.getPreferredSize());
        frame.setIconImage(ImageIO.read(Gui.class.getResource("/images/title_island.png")));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void update(Update update) {
        SwingUtilities.invokeLater(() -> update.handleUpdate(this.guiUpdateHandler));
    }

    void forwardNotify(Update update) { // forwards update to client-side Controller
        notify(update);
    }

    public void startWaitingForMatch() {

        try {
            client.sendPlayersNumber(playersNumber);
            String playerID = client.readPlayerID();
            controller.setClientPlayerID(playerID);
            client.setupUpdateListener();
            client.getUpdateListener().addObserver(this);

        } catch (IOException e) {
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
        switch (currentGamePhase) {
            case GAME_PREPARATION:
                this.gamePreparation.changeTurn();
                break;
            case REAL_GAME:
                this.realGame.changeTurn();
                break;
        }
    }

    void onBoardChanged(String board) {
        switch (currentGamePhase) {
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

    void showError(ErrorUpdate update) {

        String title = "Error";
        String message = "";

        switch (update.command) {
            case MOVE:

                if (update.errorType == ErrorType.DENIED_BY_OPPONENT_GOD) {

                    String inhibitorGod = update.getInhibitorGod().get(GodsUtils.GOD_NAME);

                    message = "Move Error: you can't perform this move because " + inhibitorGod + " doesn't let you move in the position you specified!";
                } else if (update.errorType == ErrorType.DENIED_BY_PLAYER_GOD) {
                    message = "Move Error: you can't perform this move because your God doesn't let you move in the position you specified!";
                } else if (update.errorType == ErrorType.WRONG_TURN) {
                    message = "Move Error: you can't perform this move because it's not your turn!";
                } else if (update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    message = "Wrong Game Phase: current Game Phase is not Real Game Phase";
                }

                break;

            case BUILD:

                if (update.errorType == ErrorType.DENIED_BY_OPPONENT_GOD) {
                    String inhibitorGod = update.getInhibitorGod().get(GodsUtils.GOD_NAME);
                    message = "Build Error: you can't perform this build because " + inhibitorGod + " doesn't let you build in the position you specified!";
                } else if (update.errorType == ErrorType.DENIED_BY_PLAYER_GOD) {
                    message = "Build Error: you can't perform this build because your God doesn't let you build in the position you specified!";
                } else if (update.errorType == ErrorType.WRONG_TURN) {
                    message = "Build Error: you can't perform this build because it's not your turn!";
                } else if (update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    message = "Wrong Game Phase: current Game Phase is not Real Game Phase";
                }

                break;

            case END_TURN:

                if (update.errorType == ErrorType.DENIED_BY_OPPONENT_GOD) {
                    String inhibitorGod = update.getInhibitorGod().get(GodsUtils.GOD_NAME);
                    message = "End Turn Error: " + inhibitorGod + " doesn't let you end your turn now!";
                } else if (update.errorType == ErrorType.DENIED_BY_PLAYER_GOD) {
                    message = "End Turn Error: " + "you can't end your turn now: maybe you must move or build!";
                } else if (update.errorType == ErrorType.WRONG_TURN) {
                    message = "End Turn Error: " + "you can't end your turn because it's not your turn!";
                } else if (update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    message = "Wrong Game Phase: current Game Phase is not Real Game Phase";
                }

                break;

            case PICK:

                if (update.errorType == ErrorType.ALREADY_TAKEN_NICKNAME) {
                    message = "Nickname Error: already taken nickname";
                } else if (update.errorType == ErrorType.INVALID_COLOR) {
                    message = "Color Error: invalid or already taken color";
                } else if (update.errorType == ErrorType.WRONG_TURN) {
                    message = "Turn Error: Not your turn!";
                } else if (update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    message = "Wrong Game Phase: current Game Phase is not Initial Info Phase";
                }

                break;

            case PLACE:


                if (update.errorType == ErrorType.DENIED_BY_OPPONENT_GOD) {
                    String inhibitorGod = update.getInhibitorGod().get(GodsUtils.GOD_NAME);
                    message = "Game Preparation Error: you can't place your Worker where you specified because " + inhibitorGod + " doesn't allow it";
                } else if (update.errorType == ErrorType.DENIED_BY_PLAYER_GOD) {
                    message = "Game Preparation Error: you can't place your Workers where you specified because your God doesn't allow it";
                } else if (update.errorType == ErrorType.WRONG_TURN) {
                    message = "Game Preparation Error: you can't place your Workers because it's not your turn!";
                } else if (update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    message = "Wrong Game Phase: current Game Phase is not Game Preparation Phase";
                }
                break;

            case SELECT:

                if (update.errorType == ErrorType.WRONG_TURN) {
                    message = "God Choice Error: you can't choose your God because it's not your turn!";
                } else if (update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    message = "Wrong Game Phase: current Game Phase is not Gods Choice Phase";
                } else if (update.errorType == ErrorType.INVALID_GOD) {
                    message = "God Error: invalid God selected, it's not in selectable Gods list!";
                }
                break;
        }

        String finalMessage = message;

        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame, finalMessage, title, JOptionPane.ERROR_MESSAGE));

    }


    void showWinMessageDialog(WinUpdate update) {

        String title;
        String message;
        ImageIcon icon;
        String imagePath = "src/main/resources/images/RealGame/";

        int iconWidth = 70;

        if (update.getWinnerPlayer().getPlayerID().equals(this.controller.getClientPlayerID())) {
            title = "Win";
            message = "You Won!";
            icon = new ImageIcon(imagePath + "trophy.png");
            icon = new ImageIcon(icon.getImage().getScaledInstance(iconWidth, -1, Image.SCALE_SMOOTH));
        } else {
            title = "You Lost";
            message = update.getWinnerPlayer().getNickname() + " Won!";
            icon = new ImageIcon(imagePath + "game-over.png");
            icon = new ImageIcon(icon.getImage().getScaledInstance(iconWidth, -1, Image.SCALE_SMOOTH));
        }

        ImageIcon finalIcon = icon;

        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE, finalIcon);
            askPlayAgainDialog();
        });
    }

    public void showLoseMessageDialog(LoseUpdate update) {
        String title;
        String message;

        int iconWidth = 70;


        if (update.getLoserPlayer().getPlayerID().equals(this.controller.getClientPlayerID())) {

            String loseCauseMsg = "because you can't " + (update.getLoseCause() == LoseUpdate.LoseCause.CANT_MOVE ? "move" : "build") +
                    " with any Worker";

            title = "Lost";
            message = "You Lost " + loseCauseMsg;

            SwingUtilities.invokeLater(() -> {
                ImageIcon icon;
                String imagePath = "src/main/resources/images/RealGame/game-over.png";
                icon = new ImageIcon(imagePath);
                icon = new ImageIcon(icon.getImage().getScaledInstance(iconWidth, -1, Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE, icon);
                askContinueToWatch();
            });

        } else {
            title = "Lost";
            message = update.getLoserPlayer().getNickname() + " Lost!";

            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE));
        }

    }

    public void askPlayAgainDialog() {
        String title = "Play Again";
        String message = "Do you want to play another match?";

        SwingUtilities.invokeLater(() -> {
            int res = JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            switch (res) {
                case JOptionPane.YES_OPTION:
                    this.reinitializeConnection();
                    this.reinitializeComponents();
                    break;
                case JOptionPane.NO_OPTION:
                case JOptionPane.CLOSED_OPTION:
                    System.exit(0);
                    break;
            }
        });
    }

    private void askContinueToWatch() {
        String title = "Continue to watch";
        String message = "Do you want to continue to watch this match?";

        SwingUtilities.invokeLater(() -> {
            int res = JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            switch (res) {
                case JOptionPane.YES_OPTION:
                    this.realGame.disableButtons();
                    break;
                case JOptionPane.NO_OPTION:
                case JOptionPane.CLOSED_OPTION:
                    askPlayAgainDialog();
                    break;
            }
        });
    }

    public void showServerUnreachableDialog() {

        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "Cannot communicate to the Server, maybe it's down. Otherwise, check your connection." + System.lineSeparator() + "Quitting...", "Server Unreachable", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        });
    }

    public void showDisconnectedPlayerDialog(DisconnectedPlayerUpdate update) {
        String imagePath = "src/main/resources/images/RealGame/player_disconnected.png";
        int iconWidth = 70;

        ImageIcon icon = new ImageIcon(imagePath);
        icon = new ImageIcon(icon.getImage().getScaledInstance(iconWidth, -1, Image.SCALE_SMOOTH));

        String player = update.getDisconnectedPlayer().getNickname() != null ? update.getDisconnectedPlayer().getNickname() : "A Player";

        ImageIcon finalIcon = icon;

        if (this.currentGamePhase != GamePhase.MATCH_ENDED) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showOptionDialog(null, player + " disconnected", "Disconnection", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, finalIcon, null, null);
                askPlayAgainDialog();
            });
        }
    }

    private void reinitializeComponents() {
        mainPanel.removeAll();

        initializeComponents();
    }

    private void initializeComponents() {
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
    }

    private void reinitializeConnection() {
        try {
            client.getUpdateListener().setIsActive(false);
            client.reinitializeConnection();
            this.playersNumber = 0;

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("The Game couldn't start, maybe there was some network error or the server isn't available.");
            System.exit(0);
        }

    }
}
