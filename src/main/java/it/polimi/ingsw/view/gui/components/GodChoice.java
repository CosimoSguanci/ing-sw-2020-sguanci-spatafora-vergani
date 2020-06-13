package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.GodChoiceInfoButtonListener;
import it.polimi.ingsw.view.gui.listeners.GodChoiceJButtonListener;
import it.polimi.ingsw.view.gui.ui.JRoundButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<String> getSelectableGods() {
        return this.selectableGods;
    }

    public void setSelectableGods(List<String> selectableGods) {
        this.selectableGods = selectableGods;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public void setOtherPlayersNicknames(Set<String> opponents) {

        StringBuilder stringBuilder = new StringBuilder();
        String opponentsStr;

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


    private void addListeners() {
        boolean isGodChooser = this.gui.getController().isClientPlayerGodChooser();
        List<String> gods = isGodChooser ? View.getGodsNamesList() : this.selectableGods;
        ArrayList<JButton> godButtons = this.godScreen.getButtons();
        for (int i = 0; i < gods.size(); i++) {
            GodChoiceJButtonListener godChoiceJButtonListener = new GodChoiceJButtonListener(this);
            godButtons.get(i).addActionListener(godChoiceJButtonListener);
        }
    }


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

    private void onGodChoiceSent() {
        this.removeAll();
        this.add(this.loadingComponent);
        this.revalidate();
    }
}
