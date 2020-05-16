package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.gui.listeners.PlayerNumberChoiceRadioButtonListener;

import javax.swing.*;
import java.awt.*;

public class PlayerNumberChoice extends JPanel {
    private JRadioButton button1;
    private JRadioButton button2;
    JLabel resumeNumber;
    private PlayerNumberChoiceRadioButtonListener playerNumberChooseRadioButtonListener;
    private String standardImgPath = "src/main/resources/images/PlayerNumberChoice/";
    private int selectionButtonWidth = 60;
    private int startButtonWidth = 100;

    public PlayerNumberChoice() {
        //LayoutManager layoutManager = new BoxLayout(this, BoxLayout.Y_AXIS);
        //this.setLayout(layoutManager);
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        //question about number of players in the northern part of the frame
        JPanel questionPanel = new JPanel();
        JLabel questionLabel = new JLabel("How many players do you want in your match?");
        //questionLabel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        questionPanel.add(questionLabel);
        this.add(questionPanel, BorderLayout.NORTH);

        //button for selecting two players
        ImageIcon buttonTwoPlayers = new ImageIcon(standardImgPath + "2players.png");
        buttonTwoPlayers = new ImageIcon(buttonTwoPlayers.getImage().getScaledInstance(selectionButtonWidth, -1, Image.SCALE_DEFAULT));
        this.button1 = new JRadioButton("2 players", buttonTwoPlayers, true);
        //this.button1.setPreferredSize(new Dimension(30, 30));

        //button for selecting three players
        ImageIcon buttonThreePlayers = new ImageIcon(standardImgPath + "3players.png");
        buttonThreePlayers = new ImageIcon(buttonThreePlayers.getImage().getScaledInstance(selectionButtonWidth, -1, Image.SCALE_DEFAULT));
        this.button2 = new JRadioButton("3 players", buttonThreePlayers, false);

        //listener for buttons
        this.playerNumberChooseRadioButtonListener = new PlayerNumberChoiceRadioButtonListener(this);
        button1.addActionListener(playerNumberChooseRadioButtonListener);
        button2.addActionListener(playerNumberChooseRadioButtonListener);

        //panel for buttons disposed in the centre of frame
        JPanel buttonAllInfoPanel = new JPanel();
        buttonAllInfoPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();  //in the centre
        JPanel resumeNumberPanel = new JPanel();  //in the south
        resumeNumber = new JLabel("Selected: " + getPlayerNumberSelected());
        resumeNumberPanel.add(resumeNumber);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(button1);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(button2);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonAllInfoPanel.add(buttonPanel, BorderLayout.NORTH);
        buttonAllInfoPanel.add(resumeNumberPanel, BorderLayout.CENTER);
        this.add(buttonAllInfoPanel, BorderLayout.CENTER);

        //this part must be south-east
        ImageIcon startImg = new ImageIcon(standardImgPath + "start.png");
        startImg = new ImageIcon(startImg.getImage().getScaledInstance(startButtonWidth, -1, Image.SCALE_DEFAULT));
        JButton startButton = new JButton(startImg);
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BorderLayout());
        JPanel innerPanel2 = new JPanel();
        innerPanel2.setLayout(new BorderLayout());
        innerPanel2.add(startButton, BorderLayout.EAST);
        innerPanel.add(innerPanel2);
        this.add(innerPanel, BorderLayout.SOUTH);


        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }


    public void setPlayerNumberSelected(JRadioButton button) {
        if(button.equals(button1)) {
            button1.setSelected(true);
            button2.setSelected(false);
        }
        else{
            button1.setSelected(false);
            button2.setSelected(true);
        }
    }


    private int getPlayerNumberSelected() {
        if(this.button1.isSelected()) {
            return 2;
        }
        else {
            return 3;
        }
    }


    public void refresh() {
        resumeNumber.setText("Selected: " + getPlayerNumberSelected());
    }
}
