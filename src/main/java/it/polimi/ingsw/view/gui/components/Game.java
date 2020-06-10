package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.GodInfoActionListener;
import it.polimi.ingsw.view.gui.listeners.QuitButtonListener;
import it.polimi.ingsw.view.gui.ui.JCellButton;
import it.polimi.ingsw.view.gui.ui.JRoundButton;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class Game extends JPanel {
    private String stdImagePath = "src/main/resources//images/Game/";
    Image backgroundImage = new ImageIcon(stdImagePath + "background.png").getImage();
    private String externalImgPath = "src/main/resources/images/";
    private Font font = Gui.getFont(Gui.FONT_REGULAR, 18);
    private Font turnFont = Gui.getFont(Gui.FONT_REGULAR, 22);
    private JLabel turn = new JLabel();
    private JRoundButton quitButton;
    private BoardScreen board;
    private JPanel northernPanel;
    JLabel title;
    JLabel subtitle;
    private Font titleFont = Gui.getFont(Gui.FONT_BOLD, 24);
    JPanel rightPanel;
    int buttonDim = 70;
    protected String boardString;

    //List<JLabel> godsLabels;
    List<JButton> godsButtons;

    Gui gui = Gui.getInstance();

    private String currentPlayerNickname;
    private String nicknameToShow;
    private boolean soundPlayed = false;


    public void changeTurn() {
        this.currentPlayerNickname = gui.getController().getCurrentPlayerNickname();

        if(this.nicknameToShow == null) {
            this.nicknameToShow = this.currentPlayerNickname.length() > 10 ? this.currentPlayerNickname.substring(0, 10) + "..." : this.currentPlayerNickname;
        }

        /*if(currentPlayerNickname.equals(gui.getController().getClientPlayer().getNickname())) {
            turn.setText("Your Turn");
        }
        else {
            turn.setText(this.nicknameToShow + "'s turn");
        }
*/
        if (!currentPlayerNickname.equals(gui.getController().getClientPlayer().getNickname())) {  //not player's turn
            soundPlayed = false;
            turn.setText(this.nicknameToShow + "'s turn");
        } else {  //client's turn
            turn.setText("Your Turn");
            if(!soundPlayed) {
                try {
                    URL defaultSound = getClass().getResource("/turn.wav");
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(defaultSound);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start( );
                    soundPlayed = true;
                } catch (Exception ignored) {}
            }
        }

        this.revalidate();
    }

    BoardScreen getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.boardString = board;

        if(this.boardString != null) {
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

            JButton godButton = new JButton(nicknameResized + " has " + god);
            godButton.setForeground(PrintableColor.convertToColor(color));

            this.godsButtons.add(godButton);
        });

        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        //info about players. gods and turn in the left side
        JPanel playersGodsTurn = new JPanel();
        playersGodsTurn.setOpaque(false);
        //playersGodsTurn.setForeground(Color.RED);
        playersGodsTurn.setLayout(new BoxLayout(playersGodsTurn, BoxLayout.Y_AXIS));

        this.nicknameToShow = this.currentPlayerNickname.length() > 10 ? this.currentPlayerNickname.substring(0, 10) + "..." : this.currentPlayerNickname;

        if(currentPlayerNickname.equals(gui.getController().getClientPlayer().getNickname())) {
            this.turn = new JLabel("Your Turn");
        }
        else {
            this.turn = new JLabel(this.nicknameToShow + "'s turn");
        }

        this.turn.setHorizontalAlignment(JLabel.CENTER);
        this.turn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        this.turn.setBackground(Color.YELLOW);
        this.turn.setFont(this.turnFont);
        this.turn.setOpaque(true);

        JPanel gods = new JPanel();
        gods.setLayout(new BoxLayout(gods, BoxLayout.Y_AXIS));

        /*this.godsLabels.forEach(l -> {
            l.setFont(this.font);
            gods.add(l);
        });*/

        this.godsButtons.forEach(l -> {
            l.setFont(this.font);
            l.addActionListener(new GodInfoActionListener(this));
            gods.add(l);
            l.setAlignmentX(Component.CENTER_ALIGNMENT);
            gods.add(Box.createVerticalStrut(3));
        });

        //gods.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        //gods.setBackground(Color.LIGHT_GRAY);
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
        ImageIcon quitImg = new ImageIcon(this.externalImgPath + "exit.png");
        quitImg = new ImageIcon(quitImg.getImage().getScaledInstance(this.buttonDim, this.buttonDim, Image.SCALE_SMOOTH));
        this.quitButton = new JRoundButton(quitImg);

        this.quitButton.addActionListener(new QuitButtonListener(this));
        playersGodsTurn.add(this.quitButton);
        this.quitButton.setAlignmentX(Component.RIGHT_ALIGNMENT);  //why right alignment? It works, but puts on left side (?)


        //board in the centre
        this.board = new BoardScreen(boardString);
        /*Border boardBorder = BorderFactory.createLineBorder(Color.WHITE);
        this.board.setBorder(BorderFactory.createCompoundBorder(boardBorder, boardBorder));*/
        this.board.setOpaque(false);
        this.add(this.board, BorderLayout.CENTER);

        //content of right panel will be specific for subclasses
        this.rightPanel = new JPanel();
        this.rightPanel.setOpaque(false);
        this.rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        this.add(rightPanel, BorderLayout.EAST);


        //northernPanel with title and subtitle, which will be specific for subclasses
        this.northernPanel = new JPanel();
        //this.northernPanel.setLayout(new BoxLayout(this.northernPanel, BoxLayout.Y_AXIS));
        this.northernPanel.setOpaque(false);
        this.northernPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel insidePanel = new JPanel();
        insidePanel.setLayout(new BoxLayout(insidePanel, BoxLayout.Y_AXIS));
        insidePanel.setOpaque(false);
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JPanel subtitlePanel = new JPanel();
        subtitlePanel.setOpaque(false);
        this.title = new JLabel();
        this.title.setHorizontalAlignment(JLabel.CENTER);
        this.title.setFont(this.titleFont);
        this.title.setOpaque(false);
        this.subtitle = new JLabel();
        this.subtitle.setHorizontalAlignment(JLabel.CENTER);
        this.subtitle.setFont(this.font);
        this.subtitle.setOpaque(false);

        titlePanel.add(this.title);
        insidePanel.add(titlePanel);
        subtitlePanel.add(this.subtitle);
        insidePanel.add(subtitlePanel);
        this.northernPanel.add(insidePanel);
        this.add(this.northernPanel, BorderLayout.NORTH);

        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        this.revalidate();
    }

    abstract void draw();

    protected static java.util.List<JCellButton> twoDArrayToList(JCellButton[][] twoDArray) {
        List <JCellButton> list = new ArrayList<>();
        for (JCellButton[] array : twoDArray) {
            list.addAll(Arrays.asList(array));
        }
        return list;
    }
}
