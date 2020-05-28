package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.PlayerNumberChoiceRadioButtonListener;
import it.polimi.ingsw.view.gui.ui.JRoundButton;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerNumberChoice extends JPanel implements ActionListener {

    private JRadioButton button1;
    private JRadioButton button2;
    private JLabel resumeNumber;  //this JLabel needs to be refreshed every time a button is clicked
    private PlayerNumberChoiceRadioButtonListener playerNumberChooseRadioButtonListener;
    private String standardImgPath = "src/main/resources/images/PlayerNumberChoice/";
    private int selectionButtonWidth = 50;
    private int startButtonWidth = 100;
    private Image backgroundImage = new ImageIcon(standardImgPath + "SantoriniBackground.jpg").getImage();
    private Font font = Gui.getFont(Gui.FONT_REGULAR, 20);
    private Font resumeNumberFont = Gui.getFont(Gui.FONT_BOLD, 18);
    private Color textColor = Color.WHITE;
    private Color resumeNumberTextColor = Color.WHITE;

    public PlayerNumberChoice() {
        //LayoutManager layoutManager = new BoxLayout(this, BoxLayout.Y_AXIS);
        //this.setLayout(layoutManager);
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        //question about number of players in the northern part of the frame
        JPanel questionPanel = new JPanel();
        questionPanel.setOpaque(false);
        JLabel questionLabel = new JLabel("How many players do you want in your match?");
        questionLabel.setFont(this.font);
        questionLabel.setForeground(this.textColor);
        questionLabel.setOpaque(false);
        //questionLabel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        questionPanel.add(questionLabel);
        this.add(questionPanel, BorderLayout.NORTH);

        //button for selecting two players
        ImageIcon buttonTwoPlayers = new ImageIcon(standardImgPath + "two.png");
        buttonTwoPlayers = new ImageIcon(buttonTwoPlayers.getImage().getScaledInstance(selectionButtonWidth, -1, Image.SCALE_SMOOTH));
        this.button1 = new JRadioButton(buttonTwoPlayers, true);
        this.button1.setOpaque(false);
        //this.button1.setPreferredSize(new Dimension(30, 30));

        //button for selecting three players
        ImageIcon buttonThreePlayers = new ImageIcon(standardImgPath + "three.png");
        buttonThreePlayers = new ImageIcon(buttonThreePlayers.getImage().getScaledInstance(selectionButtonWidth, -1, Image.SCALE_SMOOTH));
        this.button2 = new JRadioButton(buttonThreePlayers, false);
        this.button2.setOpaque(false);

        //listener for buttons
        this.playerNumberChooseRadioButtonListener = new PlayerNumberChoiceRadioButtonListener(this);
        button1.addActionListener(playerNumberChooseRadioButtonListener);
        button2.addActionListener(playerNumberChooseRadioButtonListener);

        //panel for buttons disposed in the centre of frame
        JPanel buttonAllInfoPanel = new JPanel();
        buttonAllInfoPanel.setLayout(new BorderLayout());
        buttonAllInfoPanel.setOpaque(false);
        JPanel buttonPanel = new JPanel();  //in the centre
        buttonPanel.setOpaque(false);
        JPanel resumeNumberPanel = new JPanel();  //in the south


        resumeNumberPanel.setOpaque(false);
        this.resumeNumber = new JLabel( getPlayerNumberSelected() + " Players");

        this.resumeNumber.setBackground(Color.decode("0x6DAFE0"));

        this.resumeNumber.setOpaque(true);


       Border border = BorderFactory.createLineBorder(Color.WHITE, 2, true);
        this.resumeNumber.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

       //this.resumeNumber.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //this.resumeNumber.setOpaque(true);
        this.resumeNumber.setForeground(this.resumeNumberTextColor);
        this.resumeNumber.setFont(this.resumeNumberFont);
        resumeNumberPanel.add(this.resumeNumber);

        buttonPanel.setBorder(new EmptyBorder(30, 0, 0, 0));
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
        ImageIcon startImg = new ImageIcon("src/main/resources/images/next.png");
        startImg = new ImageIcon(startImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        JRoundButton startButton = new JRoundButton(startImg);
        /*startButton.setContentAreaFilled(false);
        startButton.setOpaque(true);
        startButton.setBorder(null);
        startButton.setBorderPainted(false);*/


        startButton.addActionListener(this);

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BorderLayout());
        innerPanel.setOpaque(false);
        JPanel innerPanel2 = new JPanel();
        innerPanel2.setLayout(new BorderLayout());
        innerPanel2.setOpaque(false);
        innerPanel2.add(startButton, BorderLayout.EAST);
        innerPanel.add(innerPanel2);
        this.add(innerPanel, BorderLayout.SOUTH);


        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }


    public void setPlayerNumberSelected(JRadioButton button) {
        if(button.equals(this.button1)) {
            this.button1.setSelected(true);
            this.button2.setSelected(false);
        }
        else{
            this.button1.setSelected(false);
            this.button2.setSelected(true);

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
        this.resumeNumber.setText(getPlayerNumberSelected() + " Players");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Gui gui = Gui.getInstance(null, null);
        gui.setPlayersNumber(this.getPlayerNumberSelected());
        System.out.println("171");
        gui.startWaitingForMatch();
    }
}
