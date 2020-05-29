package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.gui.ui.JRoundButton;

import javax.swing.*;
import java.awt.*;

public class RealGame extends Game {
    private String classImagePath = "src/main/resources/images/RealGame/";
    private int dimension = 70;
    private int buttonWidth = 170;
    private int buttonHeight = 80;

    public RealGame(String boardString) {
        super(boardString);

        //need to set rightPanel's layout
        //rightPanel contains buttons to perform a move, a build and to end turn
        ImageIcon moveIcon = new ImageIcon(this.classImagePath + "move.png");
        moveIcon = new ImageIcon(moveIcon.getImage().getScaledInstance(this.dimension, -1, Image.SCALE_SMOOTH));
        JButton moveButton = new JButton("Move", moveIcon);
        moveButton.setMinimumSize(new Dimension(this.buttonWidth, this.buttonHeight));
        moveButton.setPreferredSize(new Dimension(this.buttonWidth, this.buttonHeight));
        moveButton.setMaximumSize(new Dimension(this.buttonWidth, this.buttonHeight));

        ImageIcon buildIcon = new ImageIcon(this.classImagePath + "build.png");
        buildIcon = new ImageIcon(buildIcon.getImage().getScaledInstance(this.dimension, -1, Image.SCALE_SMOOTH));
        JButton buildButton = new JButton("Build", buildIcon);
        buildButton.setMinimumSize(new Dimension(this.buttonWidth, this.buttonHeight));
        buildButton.setPreferredSize(new Dimension(this.buttonWidth, this.buttonHeight));
        buildButton.setMaximumSize(new Dimension(this.buttonWidth, this.buttonHeight));

        ImageIcon endIcon = new ImageIcon(this.classImagePath + "end.png");
        endIcon = new ImageIcon(endIcon.getImage().getScaledInstance(this.dimension, -1, Image.SCALE_SMOOTH));
        JButton endButton = new JButton("End turn", endIcon);
        endButton.setMinimumSize(new Dimension(this.buttonWidth, this.buttonHeight));
        endButton.setPreferredSize(new Dimension(this.buttonWidth, this.buttonHeight));
        endButton.setMaximumSize(new Dimension(this.buttonWidth, this.buttonHeight));

        this.rightPanel.setLayout(new BoxLayout(this.rightPanel, BoxLayout.Y_AXIS));

        this.rightPanel.add(Box.createVerticalGlue());
        this.rightPanel.add(moveButton);
        this.rightPanel.add(Box.createVerticalGlue());
        this.rightPanel.add(buildButton);
        this.rightPanel.add(Box.createVerticalGlue());
        this.rightPanel.add(endButton);
        this.rightPanel.add(Box.createVerticalGlue());


        //setting title and subtitle
        this.title.setText("MATCH");
        this.subtitle.setText("Play, enjoy and become the best!");

        this.revalidate();
    }
}
