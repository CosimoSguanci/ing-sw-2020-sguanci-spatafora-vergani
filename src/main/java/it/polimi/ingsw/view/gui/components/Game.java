package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.ui.JRoundButton;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class Game extends JPanel {
    private String stdImagePath = "src/main/resources//images/Game/";
    Image backgroundImage = new ImageIcon(stdImagePath + "background.png").getImage();
    private String externalImgPath = "src/main/resources/images/";
    private Font font = Gui.getFont(Gui.FONT_REGULAR, 18);
    private Font turnFont = Gui.getFont(Gui.FONT_REGULAR, 22);
    private JLabel turn;
    private JRoundButton quitButton;
    private BoardScreen board;
    private JPanel northernPanel;
    JLabel title;
    JLabel subtitle;
    private Font titleFont = Gui.getFont(Gui.FONT_BOLD, 24);
    JPanel rightPanel = new JPanel();
    int buttonDim = 70;

    public Game(String boardString) {
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        //info about players. gods and turn in the left side
        JPanel playersGodsTurn = new JPanel();
        playersGodsTurn.setOpaque(false);
        //playersGodsTurn.setForeground(Color.RED);
        playersGodsTurn.setLayout(new BoxLayout(playersGodsTurn, BoxLayout.Y_AXIS));
        this.turn = new JLabel("x's turn");
        this.turn.setHorizontalAlignment(JLabel.CENTER);
        this.turn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        this.turn.setBackground(Color.YELLOW);
        this.turn.setFont(this.turnFont);
        this.turn.setOpaque(true);
        //todo get data from previous choices
        //insert in gods an ArrayList<JLabel>
        JPanel gods = new JPanel();
        gods.setLayout(new BoxLayout(gods, BoxLayout.Y_AXIS));
        JLabel one = new JLabel("x has A");
        one.setFont(this.font);
        JLabel two = new JLabel("y has B");
        two.setFont(this.font);
        JLabel three = new JLabel("z has C");
        three.setFont(this.font);
        gods.add(one);
        gods.add(two);
        gods.add(three);

        gods.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        gods.setBackground(Color.YELLOW);
        gods.setOpaque(true);
        playersGodsTurn.add(Box.createVerticalGlue());
        playersGodsTurn.add(this.turn);
        playersGodsTurn.add(Box.createVerticalStrut(30));
        playersGodsTurn.add(gods);
        playersGodsTurn.add(Box.createVerticalGlue());
        playersGodsTurn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));


        this.add(playersGodsTurn, BorderLayout.WEST);


        //button to quit must be south-west
        ImageIcon quitImg = new ImageIcon(this.externalImgPath + "exit.png");
        quitImg = new ImageIcon(quitImg.getImage().getScaledInstance(this.buttonDim, this.buttonDim, Image.SCALE_SMOOTH));
        this.quitButton = new JRoundButton(quitImg);

        playersGodsTurn.add(this.quitButton);


        //board in the centre
        this.board = new BoardScreen(boardString);
        Border boardBorder = BorderFactory.createLineBorder(Color.WHITE);
        this.board.setBorder(BorderFactory.createCompoundBorder(boardBorder, boardBorder));
        this.board.setOpaque(false);
        this.add(this.board, BorderLayout.CENTER);

        //content of right panel will be specific for subclasses
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }


    public void updateBoard(String boardString) {
        this.board = new BoardScreen(boardString);
        this.revalidate();
    }
}