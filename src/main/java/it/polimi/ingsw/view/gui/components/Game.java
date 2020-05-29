package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.ui.JRoundButton;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class Game extends JPanel {
    private String stdImagePath = "src/main/resources//images/Game/";
    Image backgroundImage = new ImageIcon(stdImagePath + "background.png").getImage();
    private String externalImgPath = "src/main/resources/images/";
    private Font font = Gui.getFont(Gui.FONT_REGULAR, 16);
    private JLabel turn;
    private JRoundButton quitButton;

    public Game() {
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
        this.turn.setFont(this.font);
        this.turn.setOpaque(true);
        JLabel gods = new JLabel("x has A, y has B, z has C");
        gods.setLayout(new BoxLayout(gods, BoxLayout.Y_AXIS));
        //todo get data from previous choices
        gods.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        gods.setBackground(Color.YELLOW);
        gods.setFont(this.font);
        gods.setOpaque(true);
        playersGodsTurn.add(Box.createVerticalGlue());
        playersGodsTurn.add(this.turn);
        playersGodsTurn.add(Box.createVerticalStrut(30));
        playersGodsTurn.add(gods);
        playersGodsTurn.add(Box.createVerticalGlue());


        this.add(playersGodsTurn, BorderLayout.WEST);


        //button to quit must be south-west
        ImageIcon quitImg = new ImageIcon(this.externalImgPath + "exit.png");
        quitImg = new ImageIcon(quitImg.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
        this.quitButton = new JRoundButton(quitImg);

        playersGodsTurn.add(this.quitButton);

        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        this.revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
