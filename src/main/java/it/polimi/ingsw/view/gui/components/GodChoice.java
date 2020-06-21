package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.GodChoiceInfoButtonListener;
import it.polimi.ingsw.view.gui.listeners.GodChoiceJButtonListener;
import it.polimi.ingsw.view.gui.ui.JRoundButton;
import it.polimi.ingsw.view.gui.ui.LoadingComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * GodChoice is a class in which the GUI-component of
 * the GamePreparation game phase is generated. At the beginning of a match
 * GodChooser player is asked to choose as many Gods as the number of player
 * involved in the match; other player will choose a single God, one of those
 * chosen from the GodChooser.
 *
 * @author Andrea Vergani
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 */
public class GodChoice extends JPanel implements ActionListener {

    private final String standardImgPath = "/images/GodChoice/";
    private final Image backgroundImage = new ImageIcon(getClass().getResource(standardImgPath + "title_sky.png")).getImage(); // todo background ok? Or Olympus
    private final Gui gui;
    private final LoadingComponent loadingComponent;
    GodScreen godScreen;
    private int selectedGodsNumber = 0;
    private int playersNumber;
    private List<String> selectedGods;
    private List<String> selectableGods;

    /**
     * This is the constructor of the class. At the moment of creation a Gui
     * instance is associated to the class, and all the player are notified
     * to wait.
     */
    public GodChoice() {

        this.gui = Gui.getInstance();
        this.selectedGods = new ArrayList<>();
        //Set<String> opponents = gui.getPlayersColors().keySet();
        /*String loadingMsg = "You're playing against ";

        int i = 0;

        while(i < opponents.size()) {
            loadingMsg += opponents
            i++;
        }*/ // todo

        this.loadingComponent = new LoadingComponent("Waiting...", Color.BLACK);

        this.add(loadingComponent);
    }

    /**
     * This method is a simple getter of all the God selectable.
     * GodChooser player will see all the names of the Gods available in Santorini Game.
     * Other players will see only the names of the Gods chosen by GodChooser
     * @return a list which contains String with references to available Gods,
     *  based on the player.
     */
    public List<String> getSelectableGods() {
        return this.selectableGods;
    }

    /**
     * This method is used to set the selectableGods List with Gods' name
     * according to different players.
     * @param selectableGods contains a list of God names available.
     */
    public void setSelectableGods(List<String> selectableGods) {
        this.selectableGods = selectableGods;
    }

    /**
     * This method is used to set the background of different buttons.
     * @param g contains a reference to the component we want to set a background image.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    /**
     * This method is used to notify to all the players involved in the match
     * the nickname of the opponents, distinguishing the case with only one or more opponents.
     * @param opponents contains a Set of string with nicknames of player involved in the match
     */
    public void setOtherPlayersNicknames(Set<String> opponents) {

        StringBuilder stringBuilder = new StringBuilder();
        String opponentsStr;

        opponents = opponents.stream().map(opponent -> {
            if(opponent.length() < 10)
                return opponent;
            else
                return opponent.substring(0, 10);
        }).collect(Collectors.toSet());

        if (this.gui.getPlayersNumber() == 3) {
            stringBuilder.append("<html>Waiting... Your opponents are ");
            opponents.stream().filter(p -> !p.toLowerCase().equals(this.gui.getController().getClientPlayer().getNickname().toLowerCase())).forEach(opponent -> stringBuilder.append("<strong>").append(opponent).append("</strong>").append(" and "));
            opponentsStr = stringBuilder.substring(0, stringBuilder.lastIndexOf(" and "));
            opponentsStr += "</html>";

        } else {
            stringBuilder.append("<html>Waiting... Your opponent is ");
            opponents.stream().filter(p -> !p.toLowerCase().equals(this.gui.getController().getClientPlayer().getNickname().toLowerCase())).forEach(opponent -> stringBuilder.append("<strong>").append(opponent).append("</strong></html>"));
            opponentsStr = stringBuilder.toString();
        }

        this.loadingComponent.setLoadingMessage(opponentsStr);
    }

    /**
     * This method manages all the click on the GodChoice game phase buttons.
     * When a user press a button, the God associated to the button will be made "selected".
     * To unselect a God already chosen a player may clicks a second time on it.
     * @param button contains reference to the button clicked.
     */
    public void setGodChoiceSelected(JButton button) {
        if (gui.getController().isClientPlayerGodChooser()) {
            if (!button.isSelected()) {
                if (selectedGodsNumber < playersNumber) {
                    button.setSelected(true);

                    String operSys = System.getProperty("os.name").toLowerCase();

                    if (operSys.contains("mac")) {
                        button.setBorderPainted(true);
                    }

                    button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2, true));
                    selectedGodsNumber++;
                    this.selectedGods.add(button.getText());
                }
            } else {
                button.setSelected(false);
                button.setBorder(UIManager.getBorder("Button.border"));

                String operSys = System.getProperty("os.name").toLowerCase();

                if (operSys.contains("mac")) {
                    button.setBorderPainted(false);
                }

                selectedGodsNumber--;
                this.selectedGods.remove(button.getText());
            }

        } else {
            if (!button.isSelected()) {
                if (selectedGodsNumber < 1) {
                    button.setSelected(true);

                    String operSys = System.getProperty("os.name").toLowerCase();

                    if (operSys.contains("mac")) {
                        button.setBorderPainted(true);
                    }

                    button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2, true));

                    selectedGodsNumber++;
                    this.selectedGods.add(button.getText());
                }
            } else {
                button.setSelected(false);

                button.setBorder(UIManager.getBorder("Button.border"));

                String operSys = System.getProperty("os.name").toLowerCase();

                if (operSys.contains("mac")) {
                    button.setBorderPainted(false);
                }

                selectedGodsNumber--;
                this.selectedGods.remove(button.getText());
            }
        }

    }

    /**
     * This private method manages the layout during GodChoice game phase.
     * It contains a done button, where players click when they chose.
     * It also contains an information button, which contains information about Gods available.
     */
    private void setSpecific() {
        ImageIcon startImg = new ImageIcon(getClass().getResource("/images/done.png"));
        startImg = new ImageIcon(startImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JRoundButton continueButton = new JRoundButton(startImg);
        continueButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        continueButton.setOpaque(false);
        continueButton.addActionListener(this);

        ImageIcon infoImg = new ImageIcon(getClass().getResource(this.standardImgPath + "information.png"));
        infoImg = new ImageIcon(infoImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JRoundButton infoButton = new JRoundButton(infoImg);
        infoButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoButton.setOpaque(false);
        infoButton.addActionListener(new GodChoiceInfoButtonListener(this));

        //JPanel innerPanel = new JPanel();
        //innerPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.add(infoButton);
        buttonPanel.add(continueButton);
        //innerPanel.add(buttonPanel, BorderLayout.SOUTH);
        this.godScreen.getRightPanel().add(buttonPanel, BorderLayout.SOUTH);


        boolean isGodChooser = this.gui.getController().isClientPlayerGodChooser();
        if (isGodChooser) {
            this.godScreen.setTitle("Select " + playersNumber + " Gods");
        } else {
            this.godScreen.setTitle("Select a God");
        }
    }

    /**
     * This private method is used to add listeners to all the buttons generated
     * and made visible to players during the turn.
     */
    private void addListeners() {
        boolean isGodChooser = this.gui.getController().isClientPlayerGodChooser();
        List<String> gods = isGodChooser ? View.getGodsNamesList() : this.selectableGods;
        ArrayList<JButton> godButtons = this.godScreen.getButtons();
        for (int i = 0; i < gods.size(); i++) {
            GodChoiceJButtonListener godChoiceJButtonListener = new GodChoiceJButtonListener(this);
            godButtons.get(i).addActionListener(godChoiceJButtonListener);
        }
    }

    /**
     * This method manages what to show according to player and selectable Gods.
     * It creates a new GodScreen according to the turn. GodChooser will see
     * all the Gods of the game, other player will see only remaining Gods according to the rules.
     */
    // todo add interface
    public void showGuiOnTurn() {

        this.playersNumber = gui.getPlayersNumber();
        this.removeAll();

        try {
            LayoutManager layoutManager = new BorderLayout();
            this.setLayout(layoutManager);
            this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            this.godScreen = new GodScreen(this.playersNumber, this.selectableGods);
            this.godScreen.setOpaque(false);
            this.add(this.godScreen, BorderLayout.CENTER);
            //drawGodChoice(this, this.gui, this.possibleGodsPanel, this.innerPanel, this.innerPanel2, this.titleLabel, this.buttons);
            setSpecific();
            //commonGodChoose(possibleGodsPanel, continueButton, innerPanel, innerPanel2);
            addListeners();
            this.revalidate();
            this.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.revalidate();
        this.repaint();
    }

    /**
     * This method manages the activities linked to the clicks on the continue button of the class.
     * It manages cases in which GodChooser player tries to select fewer Gods than necessary
     * and the case in which a generic player clicks continue button having no selected a God.
     * @param e contains reference to the continue button.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        boolean isGodChooser = gui.getController().isClientPlayerGodChooser();

        if (isGodChooser && this.selectedGods.size() != playersNumber) {
            JOptionPane.showMessageDialog(gui.getMainFrame(), "You must select " + playersNumber + " gods!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isGodChooser && this.selectedGods.size() != 1) {
            JOptionPane.showMessageDialog(gui.getMainFrame(), "You must select one god!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {

            this.selectedGods = this.selectedGods.stream().map(String::toLowerCase).collect(Collectors.toList());

            GodChoiceCommand godChoiceCommand = new GodChoiceCommand(this.selectedGods);
            gui.notify(godChoiceCommand);

            onGodChoiceSent();
        }
    }

    /**
     * This private method manages the case in which a player finishes
     * the GodChoice phase.
     */
    private void onGodChoiceSent() {
        this.removeAll();
        this.add(this.loadingComponent);
        this.revalidate();
    }
}
