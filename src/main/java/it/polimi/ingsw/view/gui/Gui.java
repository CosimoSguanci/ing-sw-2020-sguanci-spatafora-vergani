package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.model.ErrorType;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.observer.Observer;
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
    private JFrame frame;
    private CardLayout mainCardLayout;
    private JPanel mainPanel;
    private PlayerNumberChoice playerNumberChoiceComponent;
    private WaitingForAMatch waitingForAMatchComponent;
    private InitialInfo initialInfoComponent;
    private GodChoice godsChoiceComponent;
    private GamePreparation gamePreparation;
    private RealGame realGame;
    private GodGuiDrawer godGuiDrawer;
    boolean showDisconnectedDialog = true;

    private Gui(Client clientInstance, Controller controllerInstance) {
        super(clientInstance, controllerInstance);
        this.updateHandler = new GuiUpdateHandler(this, controllerInstance);
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

    static void initFrame(JFrame frame) throws IOException {
        frame.pack();
        frame.setPreferredSize(new Dimension(800, 700));
        frame.setMinimumSize(frame.getPreferredSize());
        frame.setIconImage(ImageIO.read(Gui.class.getResource("/images/title_island.png")));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JFrame getMainFrame() {
        return frame;
    }

    public GodGuiDrawer getGodGuiDrawer() {
        return this.godGuiDrawer;
    }

    @Override
    public void setPlayersGods(Map<String, String> playersGods) {
        super.setPlayersGods(playersGods);
        this.godGuiDrawer = GodsUtils.godsGuiFactory(playersGods.get(controller.getClientPlayer().getNickname()));
    }

    @Override
    public void setPlayersColors(Map<String, PrintableColor> playersColors) {
        super.setPlayersColors(playersColors);
        this.godsChoiceComponent.setOtherPlayersNicknames(playersColors.keySet());
    }

    @Override
    public void start() {
        SwingUtilities.invokeLater(() -> {
            try {
                showGui();
            } catch (IOException e) {
                new ConnectionError().show();
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

        initFrame(frame);
    }

    @Override
    public void update(Update update) {
        SwingUtilities.invokeLater(() -> update.handleUpdate(this.updateHandler));
    }

    public void startWaitingForMatch() {

        try {

            client.sendPlayersNumber(playersNumber);
            String playerID = client.readPlayerID();
            controller.setClientPlayerID(playerID);
            client.setupUpdateListener();
            client.getUpdateListener().addObserver(this);

        } catch (IOException e) {
            showServerUnreachableDialog();
        }

        this.mainCardLayout.show(mainPanel, WAITING_FOR_MATCH);
    }

    void startInitialInfoPhase() {
        this.currentGamePhase = GamePhase.INITIAL_INFO;
        this.mainCardLayout.show(mainPanel, INITIAL_INFO);
    }

    void startGodChoicePhase() {
        this.currentGamePhase = GamePhase.CHOOSE_GODS;
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

    /**
     * Callback method called when the Game Phase changes to MATCH_ENDED or MATCH_LOST
     * @param newGamePhase the new current game phase
     */
    void onMatchFinished(GamePhase newGamePhase) {
        this.currentGamePhase = newGamePhase;
    }

    void showInitialInfoOnTurn() {
        this.initialInfoComponent.showGuiOnTurn();
    }

    void showGodsChoiceOnTurn() {
        this.godsChoiceComponent.showGuiOnTurn();
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

    @Override
    public void setSelectableColors(List<PrintableColor> selectableColors) {
        this.initialInfoComponent.setSelectableColors(selectableColors);
    }

    @Override
    public void setSelectedNicknames(List<String> selectedNicknames) {
        this.initialInfoComponent.setSelectedNicknames(selectedNicknames);
    }

    @Override
    public void setSelectableGods(List<String> selectableGods) {
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
                    message = "Move Error: you can't perform this move because you can't move to this cell (maybe you've already moved), or your God doesn't let you move in the position you specified!";
                } else if (update.errorType == ErrorType.WRONG_TURN) {
                    message = "Move Error: you can't perform this move because it's not your turn!";
                } else if (update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    message = "Wrong Game Phase: current Game Phase is not Real Game Phase";
                } else if (update.errorType == ErrorType.INVALID_CELL) {
                    message = "Invalid Cell";
                } else if (update.errorType == ErrorType.GENERIC_ERROR) {
                    message = "Generic Error: please, try another command";
                }

                break;

            case BUILD:

                if (update.errorType == ErrorType.DENIED_BY_OPPONENT_GOD) {
                    String inhibitorGod = update.getInhibitorGod().get(GodsUtils.GOD_NAME);
                    message = "Build Error: you can't perform this build because " + inhibitorGod + " doesn't let you build in the position you specified!";
                } else if (update.errorType == ErrorType.DENIED_BY_PLAYER_GOD) {
                    message = "Build Error: you can't perform this build because you can't build in this cell (maybe you haven't moved yet), or your God doesn't let you build in the position you specified!";
                } else if (update.errorType == ErrorType.WRONG_TURN) {
                    message = "Build Error: you can't perform this build because it's not your turn!";
                } else if (update.errorType == ErrorType.WRONG_GAME_PHASE) {
                    message = "Wrong Game Phase: current Game Phase is not Real Game Phase";
                } else if (update.errorType == ErrorType.INVALID_CELL) {
                    message = "Invalid Cell";
                } else if (update.errorType == ErrorType.GENERIC_ERROR) {
                    message = "Generic Error: please, try another command";
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
                } else if (update.errorType == ErrorType.INVALID_CELL) {
                    message = "Invalid Cell";
                } else if (update.errorType == ErrorType.GENERIC_ERROR) {
                    message = "Generic Error: please, try another command";
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
                } else if (update.errorType == ErrorType.INVALID_CELL) {
                    message = "Invalid Cell";
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
        String imagePath = "/images/RealGame/";

        int iconWidth = 70;

        if (update.getWinnerPlayer().getPlayerID().equals(this.controller.getClientPlayerID())) {
            title = "Win";
            message = "You Won!";
            icon = new ImageIcon(getClass().getResource(imagePath + "trophy.png"));
            icon = new ImageIcon(icon.getImage().getScaledInstance(iconWidth, -1, Image.SCALE_SMOOTH));
        } else {
            title = "You Lost";
            message = update.getWinnerPlayer().getNickname() + " Won!";
            icon = new ImageIcon(getClass().getResource(imagePath + "game-over.png"));
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
                String imagePath = "/images/RealGame/game-over.png";
                icon = new ImageIcon(getClass().getResource(imagePath));
                icon = new ImageIcon(icon.getImage().getScaledInstance(iconWidth, -1, Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE, icon);
                if(!update.onePlayerRemaining) {
                    askContinueToWatch();
                }
                else {
                    askPlayAgainDialog();
                }
            });

        } else if (update.onePlayerRemaining) {

            SwingUtilities.invokeLater(() -> {
                String titleWin = "Win";
                String messageWin = "You Won because " + update.getLoserPlayer().getNickname() + " lost!";

                ImageIcon icon = new ImageIcon(getClass().getResource("/images/RealGame/trophy.png"));
                icon = new ImageIcon(icon.getImage().getScaledInstance(iconWidth, -1, Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(null, messageWin, titleWin, JOptionPane.INFORMATION_MESSAGE, icon);
                askPlayAgainDialog();
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
            int res = buildQuestionDialog(title, message);

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
        this.showDisconnectedDialog = false;
        String title = "Continue to watch";
        String message = "Do you want to continue to watch this match?";

        SwingUtilities.invokeLater(() -> {
            int res = buildQuestionDialog(title, message);

            switch (res) {
                case JOptionPane.YES_OPTION:
                    this.realGame.disableButtons();
                    this.showDisconnectedDialog = true;
                    break;
                case JOptionPane.NO_OPTION:
                case JOptionPane.CLOSED_OPTION:
                    askPlayAgainDialog();
                    break;
            }
        });
    }

    /**
     * Builds a dialog which asks a question to the user (examples: "Do you want to continue to watch?"
     * "Do you want to play again?")
     * @param title the title of the dialog
     * @param message the message contained in the dialog
     * @return the choice made by the user
     */
    private int buildQuestionDialog(String title, String message) {
        String imagePath = "/images/RealGame/question.png";
        int iconWidth = 70;
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        icon = new ImageIcon(icon.getImage().getScaledInstance(iconWidth, -1, Image.SCALE_SMOOTH));
        return JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon, null, null);
    }

    public void showServerUnreachableDialog() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "Cannot communicate to the Server, maybe it's down. Otherwise, check your connection." + System.lineSeparator() + "Quitting...", "Server Unreachable", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        });
    }

    public void showDisconnectedPlayerDialog(DisconnectedPlayerUpdate update) {
        String imagePath = "/images/RealGame/player_disconnected.png";
        int iconWidth = 70;

        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
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
            showServerUnreachableDialog();
        }
    }
}
