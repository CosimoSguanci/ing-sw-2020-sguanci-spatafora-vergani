package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.GameManualListener;
import it.polimi.ingsw.view.gui.listeners.GodInfoActionListener;
import it.polimi.ingsw.view.gui.listeners.QuitButtonListener;
import it.polimi.ingsw.view.gui.ui.JCellButton;
import it.polimi.ingsw.view.gui.ui.JGodButton;
import it.polimi.ingsw.view.gui.ui.JRoundButton;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class Game extends JPanel {
    private final String stdImagePath = "/images/Game/";
    private final Image backgroundImage = new ImageIcon(getClass().getResource(stdImagePath + "background.png")).getImage();
    private final Font font = Gui.getFont(Gui.FONT_REGULAR, 18);
    private final Font turnFont = Gui.getFont(Gui.FONT_REGULAR, 22);
    private final Font titleFont = Gui.getFont(Gui.FONT_BOLD, 24);
    protected String boardString;
    private List<JButton> godsButtons;
    private final Gui gui = Gui.getInstance();
    private JLabel turn = new JLabel();
    private BoardScreen board;
    private String currentPlayerNickname;
    private String nicknameToShow;
    protected JPanel rightPanel;
    protected int buttonDim = 70;
    protected JLabel title;
    protected JLabel subtitle;
    private boolean soundPlayed = false;

    protected static java.util.List<JCellButton> twoDArrayToList(JCellButton[][] twoDArray) {
        List<JCellButton> list = new ArrayList<>();
        for (JCellButton[] array : twoDArray) {
            list.addAll(Arrays.asList(array));
        }
        return list;
    }

    public void changeTurn() {
        this.currentPlayerNickname = gui.getController().getCurrentPlayerNickname();

        if (this.nicknameToShow == null) {
            this.nicknameToShow = this.currentPlayerNickname.length() > 10 ? this.currentPlayerNickname.substring(0, 10) + "..." : this.currentPlayerNickname;
        }

        if (!currentPlayerNickname.equals(gui.getController().getClientPlayer().getNickname())) {  //not player's turn
            soundPlayed = false;
            turn.setText(this.nicknameToShow + "'s turn");
        } else {  //client's turn
            turn.setText("Your Turn");
            if (!soundPlayed) {
                View.playOnTurnSound();
                soundPlayed = true;
            }
        }

        this.revalidate();
    }

    BoardScreen getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.boardString = board;

        if (this.boardString != null) {
            this.removeAll();
            this.draw();
            this.revalidate();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    protected void drawCommonBoard() {

        this.currentPlayerNickname = gui.getController().getCurrentPlayerNickname();

        Map<String, String> playersGods = gui.getPlayersGods();
        Map<String, PrintableColor> playersColors = gui.getPlayersColors();

        this.godsButtons = new ArrayList<>();

        playersGods.forEach((player, god) -> {
            PrintableColor color = playersColors.get(player);  //gets player's color

            String nicknameResized = player.length() > 10 ? player.substring(0, 10) + "..." : player;

            JGodButton godButton;

            if (player.equals(gui.getController().getClientPlayer().getNickname())) {
                godButton = new JGodButton("You have " + god, god.toLowerCase());
            } else {
                godButton = new JGodButton(nicknameResized + " has " + god, god.toLowerCase());
            }

            godButton.setForeground(PrintableColor.convertToColor(color));


            this.godsButtons.add(godButton);
        });

        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        //info about players. gods and turn in the left side
        JPanel playersGodsTurn = new JPanel();
        playersGodsTurn.setOpaque(false);
        playersGodsTurn.setLayout(new BoxLayout(playersGodsTurn, BoxLayout.Y_AXIS));

        this.nicknameToShow = this.currentPlayerNickname.length() > 10 ? this.currentPlayerNickname.substring(0, 10) + "..." : this.currentPlayerNickname;

        if (currentPlayerNickname.equals(gui.getController().getClientPlayer().getNickname())) {
            this.turn = new JLabel("Your Turn");
        } else {
            this.turn = new JLabel(this.nicknameToShow + "'s turn");
        }

        this.turn.setHorizontalAlignment(JLabel.CENTER);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2, true);
        this.turn.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        this.turn.setBackground(Color.YELLOW);
        this.turn.setFont(this.turnFont);
        this.turn.setOpaque(true);

        JPanel gods = new JPanel();
        gods.setLayout(new BoxLayout(gods, BoxLayout.Y_AXIS));

        this.godsButtons.forEach(l -> {
            l.setFont(this.font);
            l.addActionListener(new GodInfoActionListener(this));
            gods.add(l);
            l.setAlignmentX(Component.CENTER_ALIGNMENT);
            gods.add(Box.createVerticalStrut(10));
        });

        gods.setOpaque(false);
        playersGodsTurn.add(Box.createVerticalGlue());
        playersGodsTurn.add(this.turn);
        this.turn.setAlignmentX(Component.CENTER_ALIGNMENT);
        playersGodsTurn.add(Box.createVerticalStrut(30));
        playersGodsTurn.add(gods);
        gods.setAlignmentX(Component.CENTER_ALIGNMENT);
        playersGodsTurn.add(Box.createVerticalGlue());
        playersGodsTurn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        this.add(playersGodsTurn, BorderLayout.WEST);

        //button to quit must be south-west
        String externalImgPath = "/images/";

        ImageIcon helpImg = new ImageIcon(getClass().getResource(externalImgPath + "info.png"));
        helpImg = new ImageIcon(helpImg.getImage().getScaledInstance(80, 90, Image.SCALE_SMOOTH));
        JRoundButton helpButton = new JRoundButton(helpImg);
        helpButton.addActionListener(new GameManualListener(this));
        playersGodsTurn.add(helpButton);
        helpButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        ImageIcon quitImg = new ImageIcon(getClass().getResource(externalImgPath + "exit.png"));
        quitImg = new ImageIcon(quitImg.getImage().getScaledInstance(buttonDim, buttonDim, Image.SCALE_SMOOTH));
        JRoundButton quitButton = new JRoundButton(quitImg);
        quitButton.addActionListener(new QuitButtonListener(this));
        playersGodsTurn.add(quitButton);
        quitButton.setAlignmentX(Component.RIGHT_ALIGNMENT);  //why right alignment? It works, but puts on left side (?)


        //board in the centre
        this.board = new BoardScreen(boardString);
        this.board.setOpaque(false);
        this.add(this.board, BorderLayout.CENTER);

        //content of right panel will be specific for subclasses
        this.rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        this.add(rightPanel, BorderLayout.EAST);


        //northernPanel with title and subtitle, which will be specific for subclasses
        JPanel northernPanel = new JPanel();
        northernPanel.setOpaque(false);
        northernPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel insidePanel = new JPanel();
        insidePanel.setLayout(new BoxLayout(insidePanel, BoxLayout.Y_AXIS));
        insidePanel.setOpaque(false);
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JPanel subtitlePanel = new JPanel();
        subtitlePanel.setOpaque(false);
        this.title = new JLabel();
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(this.titleFont);
        title.setOpaque(false);
        this.subtitle = new JLabel();
        subtitle.setHorizontalAlignment(JLabel.CENTER);
        subtitle.setFont(this.font);
        subtitle.setOpaque(false);

        titlePanel.add(title);
        insidePanel.add(titlePanel);
        subtitlePanel.add(subtitle);
        insidePanel.add(subtitlePanel);
        northernPanel.add(insidePanel);
        this.add(northernPanel, BorderLayout.NORTH);

        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        this.revalidate();
    }

    abstract void draw();
}
