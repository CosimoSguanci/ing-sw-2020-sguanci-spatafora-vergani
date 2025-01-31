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


/**
 * Gui class represents Graphic User Interface part of the project. The game can be played
 * through a command-line interface or a graphic environment; project purposes are those of
 * creating a "complete" Gui, so all what can be done with Cli is possible with Gui too.
 * This class handles general graphic part of the application; then, many components are used
 * to implement particular screens, situations, ...
 * Gui and Cli class share a lot of different aspects, so they both extend View class.
 * This class observes Update because Model's state changes (MVC pattern) arrive through Update,
 * and they determine changes in what is displayed on screen too.
 *
 * @author Andrea Mario Vergani
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 */
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

    /**
     * The constructor creates an instance for Graphic User Interface, connecting it to the
     * specific client. Some images, important for next game phases, are loaded here.
     *
     * @param clientInstance     client to which Gui must be associated
     * @param controllerInstance controller (client-side) of the other parameter
     */
    private Gui(Client clientInstance, Controller controllerInstance) {
        super(clientInstance, controllerInstance);
        this.updateHandler = new GuiUpdateHandler(this, controllerInstance);
        GodScreen.loadImages();
    }

    /**
     * The method returns Gui class' instance, following Singleton design pattern.
     * If no instance exists, it is created and associated with parameters.
     *
     * @param client     client associated to requested Gui
     * @param controller controller (client-side) of the other parameter
     * @return Gui instance (if not existing, it is created)
     */
    public static Gui getInstance(Client client, Controller controller) {
        if (guiInstance == null) {
            guiInstance = new Gui(client, controller);
        }
        return guiInstance;
    }

    /**
     * The method returns local instance of Gui class, following Singleton design pattern.
     * If no instance exists, it is created.
     *
     * @return Gui instance (if not existing, it is created)
     */
    public static Gui getInstance() {
        if (guiInstance == null) {
            return new Gui(null, null);
        }
        return guiInstance;
    }


    /**
     * The method returns the correct font for writing on graphic user interface, based on the
     * parameters. Only some selected fonts are possible: in this way, all Gui components will
     * use coherent fonts.
     *
     * @param fontMode font type (bold or not)
     * @param size     font size
     * @return font with selected type and size; in case of exceptions, a generic font is returned
     */
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


    /**
     * The method creates general graphic user interface for the main frame. After calling this method,
     * a blank frame is visible on screen, with default dimensions; default operations and images
     * to visualize are set, too.
     *
     * @param frame main frame for Gui
     * @throws IOException if an error occurs during image reading
     */
    static void initFrame(JFrame frame) throws IOException {
        frame.pack();
        frame.setPreferredSize(new Dimension(800, 700));
        frame.setMinimumSize(frame.getPreferredSize());
        frame.setIconImage(ImageIO.read(Gui.class.getResource("/images/title_island.png")));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * The method is the getter for Gui main frame.
     *
     * @return Gui main frame
     */
    public JFrame getMainFrame() {
        return frame;
    }

    /**
     * This method returns a reference to the user {@link GodGuiDrawer}, in order
     * to draw specific buttons for the user's god (if necessary).
     *
     * @return a reference to the correct user's {@link GodGuiDrawer}
     */
    public GodGuiDrawer getGodGuiDrawer() {
        return this.godGuiDrawer;
    }

    /**
     * The method sets correspondence between players and Gods, after these ones have been
     * selected. A graphic operation related to this is also performed.
     *
     * @param playersGods correspondence between Gods and players
     */
    @Override
    public void setPlayersGods(Map<String, String> playersGods) {
        super.setPlayersGods(playersGods);
        this.godGuiDrawer = GodsUtils.godsGuiFactory(playersGods.get(controller.getClientPlayer().getNickname()));
    }


    /**
     * The method sets correspondence between players and colors, after these ones have been
     * selected. A graphic operation related to this is also performed.
     *
     * @param playersColors correspondence between colors and players
     */
    @Override
    public void setPlayersColors(Map<String, PrintableColor> playersColors) {
        super.setPlayersColors(playersColors);
        this.godsChoiceComponent.setOtherPlayersNicknames(playersColors.keySet());
    }


    /**
     * The method starts graphic user interface, so this is the entry point for users
     * who want to play a match with graphic environment.
     */
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

    /**
     * The method creates Gui main frame and prepares everything (related to graphics) for
     * next phases. A proper call to initialize main frame is done.
     *
     * @throws IOException if an I/O error occurs in a called-method
     */
    private void showGui() throws IOException {
        frame = new JFrame("Santorini");

        this.mainPanel = new JPanel();
        this.mainCardLayout = new CardLayout();

        mainPanel.setLayout(mainCardLayout);

        initializeComponents();
        frame.add(mainPanel);

        initFrame(frame);
    }

    /**
     * The method handles Updates, which most of the times correspond to graphic changes.
     * In reality, what really manages Updates is a proper update handler for Gui class.
     *
     * @param update update received (originally coming from server)
     */
    @Override
    public void update(Update update) {
        SwingUtilities.invokeLater(() -> update.handleUpdate(this.updateHandler));
    }


    /**
     * The method starts graphic visualization for the phase in which a player waits for
     * other people, in order to start a match. Before this, selected player number for
     * the match is sent to server.
     */
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


    /**
     * The method starts graphic visualization for the phase in which every player must
     * select a nickname and a colour for the match.
     */
    void startInitialInfoPhase() {
        this.currentGamePhase = GamePhase.INITIAL_INFO;
        this.mainCardLayout.show(mainPanel, INITIAL_INFO);
    }

    /**
     * The method starts graphic visualization for the phase in which Gods are selected,
     * following game rules.
     */
    void startGodChoicePhase() {
        this.currentGamePhase = GamePhase.CHOOSE_GODS;
        this.mainCardLayout.show(mainPanel, GOD_CHOICE);
    }

    /**
     * The method starts graphic visualization for the phase in which players must place
     * their workers on initial cells.
     */
    void startGamePreparation() {
        this.currentGamePhase = GamePhase.GAME_PREPARATION;
        this.mainCardLayout.show(mainPanel, GAME_PREPARATION);
    }

    /**
     * The method starts graphic visualization for the phase in which the real match is
     * played, following game rules.
     */
    void startRealGame() {
        this.currentGamePhase = GamePhase.REAL_GAME;
        this.mainCardLayout.show(mainPanel, REAL_GAME);
    }

    /**
     * Callback method called when the Game Phase changes to MATCH_ENDED or MATCH_LOST.
     *
     * @param newGamePhase the new current game phase
     */
    void onMatchFinished(GamePhase newGamePhase) {
        this.currentGamePhase = newGamePhase;
    }

    /**
     * The method handles graphic visualization for nickname and colour effective selection.
     * In fact, the phase is composed of a waiting moment and the real selection, and this
     * method makes a call to manage the latter.
     */
    void showInitialInfoOnTurn() {
        this.initialInfoComponent.showGuiOnTurn();
    }

    /**
     * The method handles graphic visualization for God (or Gods, in case of god-chooser)
     * effective selection. In fact, the phase is composed of a waiting moment and the
     * real selection, and this method makes a call to manage the latter.
     */
    void showGodsChoiceOnTurn() {
        this.godsChoiceComponent.showGuiOnTurn();
    }

    /**
     * The method makes a call to manage in a proper way a change of turn. It is useful
     * when this change does not modify all what is visualized on screen, but just some
     * images/graphic components. For example, this often happens during real match, when
     * the board is always visualized, but on the basis of current turn a player can do
     * (or not) specific things through graphic interface.
     */
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

    /**
     * The method makes a call to manage in a proper way a change of board's state. It is
     * useful when this change does not modify all what is visualized on screen, but just
     * the board itself. For example, during real match, the board often changes.
     *
     * @param board the new JSON-serialized {@link it.polimi.ingsw.model.Board} received from server.
     */
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

    /**
     * The method calls the proper component in order to set the list of selectable colors
     * at the current moment. In fact, when a color is selected by one of the players, no
     * one can select it again during this match.
     *
     * @param selectableColors list of selectable colors at current time
     */
    @Override
    public void setSelectableColors(List<PrintableColor> selectableColors) {
        this.initialInfoComponent.setSelectableColors(selectableColors);
    }

    /**
     * The method calls the proper component in order to set the list of already chosen
     * nicknames for the match. In fact, when a nickname is chosen by one of the players,
     * no one can choose it again during this match.
     *
     * @param selectedNicknames list of already selected nicknames
     */
    @Override
    public void setSelectedNicknames(List<String> selectedNicknames) {
        this.initialInfoComponent.setSelectedNicknames(selectedNicknames);
    }

    /**
     * The method calls the proper component in order to set the list of selectable Gods
     * at the current moment. In fact, only Gods in this list can be chosen by the player
     * who plays the current turn.
     *
     * @param selectableGods list of selectable gods at current time
     */
    @Override
    public void setSelectableGods(List<String> selectableGods) {
        this.godsChoiceComponent.setSelectableGods(selectableGods);
    }

    /**
     * The method creates a dialog to show an error message; this message can derive from
     * invalid commands, game rules not respected, ...
     * The error message contains a brief explanation of what went wrong.
     *
     * @param update update coming form server, which first sees the error and notifies it
     */
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


    /**
     * The method creates a dialog to manage the situation in which one of the players wins.
     * One of the players (the one who won the match) is notified of his/her victory, while
     * the other/others are notified of their loss.
     *
     * @param update update coming form server, which first sees the winning situation
     */
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

    /**
     * The method creates a dialog to manage the situation in which one of the players
     * loses. The player is notified of his/her loss, with a brief explanation about it.
     * In general, if the match continues, the player who loses can decide whether to continue
     * to watch or quit.
     *
     * @param update update coming form server, which first sees the losing situation
     */
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
                if (!update.onePlayerRemaining) {
                    askContinueToWatch();
                } else {
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

            SwingUtilities.invokeLater(() -> {
                ImageIcon icon;
                String imagePath = "/images/RealGame/game-over.png";
                icon = new ImageIcon(getClass().getResource(imagePath));
                icon = new ImageIcon(icon.getImage().getScaledInstance(iconWidth, -1, Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE, icon);
            });
        }

    }

    /**
     * The method creates a dialog to ask the player if he/she wants to play another match.
     * This dialog can be visualized, for example, when a match ends, or when a player
     * wants to leave the match and maybe wants to start another one.
     * Decision by the player is managed in the proper way.
     */
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

    /**
     * The method creates a dialog to manage the situation in which one of the players
     * loses, so (if the match continues) he/she can decide whether to continue to watch
     * or quit. Decision of continuing to watch or quitting is managed in the proper way.
     */
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
     * Builds a dialog which asks a question to the user (examples: "Do you want to continue to watch?",
     * "Do you want to play again?")
     *
     * @param title   the title of the dialog
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

    /**
     * The method creates a dialog to manage the situation in which server is unreachable.
     * In this case, the player is notified with a brief possible explanation of the problem;
     * after that, the game is closed, since it is an online game.
     * Server can be unreachable for a connection problem, because it is down or for other
     * reasons.
     */
    public void showServerUnreachableDialog() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "Cannot communicate to the Server, maybe it's down. Otherwise, check your connection." + System.lineSeparator() + "Quitting...", "Server Unreachable", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        });
    }


    /**
     * The method creates a dialog to manage the situation in which one of the players
     * disconnects from the match. In this case, all other players are notified and the
     * match immediately ends.
     *
     * @param update update coming form server, which first sees the disconnection (through
     *               ping messages)
     */
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

    /**
     * The method performs a reinitialization of all graphic components. It can be useful,
     * for example, when a match ends, and all graphic environment must be re-loaded to
     * manage a new match.
     */
    private void reinitializeComponents() {
        mainPanel.removeAll();
        initializeComponents();
    }

    /**
     * The method initializes all Gui components, related to the different situations and
     * Game-Phases.
     */
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

    /**
     * The method performs a connection reinitialization. It is mainly useful when a
     * match ends, so connection is closed and must be reinitialized in case of new match.
     */
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
