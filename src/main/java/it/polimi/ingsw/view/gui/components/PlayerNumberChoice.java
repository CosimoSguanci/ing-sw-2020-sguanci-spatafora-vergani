package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.GameManualListener;
import it.polimi.ingsw.view.gui.listeners.PlayerNumberChoiceRadioButtonListener;
import it.polimi.ingsw.view.gui.ui.JRoundButton;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * PlayerNumberChoice is a class in which the first Gui-component of the game is defined.
 *
 * @author Roberto Spatafora
 * @author Andrea Vergani
 */
public class PlayerNumberChoice extends JPanel implements ActionListener {

    private final JRadioButton button1;
    private final JRadioButton button2;
    private final JLabel resumeNumber;  //this JLabel needs to be refreshed every time a button is clicked
    private final String standardImgPath = "/images/PlayerNumberChoice/";
    private final Image backgroundImage = new ImageIcon(PlayerNumberChoice.class.getResource(standardImgPath + "santoriniBackground.jpg")).getImage();

    /**
     * This is the constructor of the class. At the moment of creation a JPanel is crated.
     * Every user who connect to the server will see this initial screen.
     * This component gives users the possibility to choose how many players
     * he wants in the match. Users can select 2 or 3 players button and then
     * they can confirm their preference by clicking on the continue button.
     */
    public PlayerNumberChoice() {
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        //question about number of players in the northern part of the frame
        JPanel questionPanel = new JPanel();
        questionPanel.setOpaque(false);
        JLabel questionLabel = new JLabel("How many players do you want in your match?");
        Font font = Gui.getFont(Gui.FONT_BOLD, 22);
        questionLabel.setFont(font);
        Color textColor = Color.WHITE;
        questionLabel.setForeground(textColor);
        questionLabel.setOpaque(false);
        questionPanel.add(questionLabel);
        this.add(questionPanel, BorderLayout.NORTH);

        //button for selecting two players
        ImageIcon buttonTwoPlayers = new ImageIcon(getClass().getResource(standardImgPath + "two.png"));
        int selectionButtonWidth = 80;
        buttonTwoPlayers = new ImageIcon(buttonTwoPlayers.getImage().getScaledInstance(selectionButtonWidth, -1, Image.SCALE_SMOOTH));
        this.button1 = new JRadioButton(buttonTwoPlayers, true);
        this.button1.setOpaque(false);

        //button for selecting three players
        ImageIcon buttonThreePlayers = new ImageIcon(getClass().getResource(standardImgPath + "three.png"));
        buttonThreePlayers = new ImageIcon(buttonThreePlayers.getImage().getScaledInstance(selectionButtonWidth, -1, Image.SCALE_SMOOTH));
        this.button2 = new JRadioButton(buttonThreePlayers, false);
        this.button2.setOpaque(false);

        //listener for buttons
        PlayerNumberChoiceRadioButtonListener playerNumberChooseRadioButtonListener = new PlayerNumberChoiceRadioButtonListener(this);
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
        this.resumeNumber = new JLabel(getPlayerNumberSelected() + " Players");

        this.resumeNumber.setBackground(Color.decode("0x6DAFE0"));

        this.resumeNumber.setOpaque(true);


        Border border = BorderFactory.createLineBorder(Color.WHITE, 2, true);
        this.resumeNumber.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        Color resumeNumberTextColor = Color.WHITE;
        this.resumeNumber.setForeground(resumeNumberTextColor);
        Font resumeNumberFont = Gui.getFont(Gui.FONT_BOLD, 22);
        this.resumeNumber.setFont(resumeNumberFont);
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

        //this part must be south-east (continue) and south-west (manual)
        String externalImgPath = "/images/";
        ImageIcon startImg = new ImageIcon(getClass().getResource(externalImgPath + "next.png"));
        startImg = new ImageIcon(startImg.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));

        JRoundButton startButton = new JRoundButton(startImg);

        startButton.addActionListener(this);

        ImageIcon manualImg = new ImageIcon(getClass().getResource(externalImgPath + "info.png"));
        manualImg = new ImageIcon(manualImg.getImage().getScaledInstance(80, -1, Image.SCALE_SMOOTH));

        JRoundButton infoButton = new JRoundButton(manualImg);

        infoButton.addActionListener(new GameManualListener(this));

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BorderLayout());
        innerPanel.setOpaque(false);
        JPanel innerPanel2 = new JPanel();
        innerPanel2.setLayout(new BorderLayout());
        innerPanel2.setOpaque(false);
        innerPanel2.add(startButton, BorderLayout.EAST);
        innerPanel2.add(infoButton, BorderLayout.WEST);
        innerPanel.add(innerPanel2);
        this.add(innerPanel, BorderLayout.SOUTH);

        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /**
     * This private method is a simple getter of the number of players
     * selected for a specific match.
     * @return the integer number which indicates how many player a user wants in his match.
     */
    private int getPlayerNumberSelected() {
        if (this.button1.isSelected()) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * This method manages the activities on a button clicked. From a listener of a button
     * the reference of a button clicked is given as parameter and comparing it with the
     * buttons of the class it manages the button selected and the other one.
     * @param button
     */
    public void setPlayerNumberSelected(JRadioButton button) {
        if (button.equals(this.button1)) {
            this.button1.setSelected(true);
            this.button2.setSelected(false);
        } else {
            this.button1.setSelected(false);
            this.button2.setSelected(true);
        }
    }

    /**
     * This method is used to refresh the JLabel which contains the number
     * of players selected from a user before clicking on the continue button
     */
    public void refresh() {
        this.resumeNumber.setText(getPlayerNumberSelected() + " Players");
    }

    /**
     * This method is used to set the background image of the component
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    /**
     * This method manages the activities on the continueButton.
     * When continue button is clicked a new gui component is generated.
     * @param e contains reference to the event that triggered the listener.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Gui gui = Gui.getInstance();
        gui.setPlayersNumber(this.getPlayerNumberSelected());
        gui.startWaitingForMatch();
    }
}
