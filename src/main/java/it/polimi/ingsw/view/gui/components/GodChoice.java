package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.GodChoiceJButtonListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GodChoice extends AbstractInitialChoice implements ActionListener {

    private ArrayList<JButton> buttons = new ArrayList<>();
    private GodChoiceJButtonListener godChoiceJButtonListener;
    private boolean isGodChooser;
    private int selectedGodsNumber = 0;
    private int playersNumber;

    private List<String> selectedGods;
    private List<String> selectableGods;

    public void setSelectableGods(List<String> selectableGods) {
        this.selectableGods = selectableGods;
    }

    public GodChoice(int playersNumber, Controller controller, boolean isGodChooser){
        super(controller);
        this.isGodChooser = isGodChooser;
        this.playersNumber = playersNumber;

        this.selectedGods = new ArrayList<>();

        this.add(new LoadingComponent("Waiting..."));
    }

    private void drawGodChoice() throws IOException {
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(this);
        JPanel innerPanel = new JPanel();
        JPanel innerPanel2 = new JPanel();

        JPanel titlePanel = new JPanel();
        JPanel possibleGodsPanel = new JPanel();
        titlePanel.add(new JLabel("Select a God"));
        this.add(titlePanel, BorderLayout.NORTH);
        this.setBorder(BorderFactory.createEmptyBorder(0,20,20,20));

        this.godChoiceJButtonListener = new GodChoiceJButtonListener(this);
        if (playersNumber == 2) {
            possibleGodsPanel.setLayout(new GridLayout(1,2));

            ArrayList<ImageIcon> iconGods = new ArrayList<>();
            ImageIcon imageIcon;
            JButton button = new JButton();
            for (int i = 0; i < playersNumber; i++) {
                imageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + this.selectableGods.get(i) + ".png")));
                imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(30,-1, Image.SCALE_SMOOTH));
                iconGods.add(imageIcon);
                //possibleGodsPanel.add(new JButton(chosenGods[i], iconGods.get(i)));
                this.buttons.add(new JButton(this.selectableGods.get(i), iconGods.get(i)));
                this.buttons.get(i).addActionListener(godChoiceJButtonListener);
                possibleGodsPanel.add(this.buttons.get(i));
            }

        }
        else {
            possibleGodsPanel.setLayout(new GridLayout(1,3));

            ArrayList<ImageIcon> iconGods = new ArrayList<>();
            ImageIcon imageIcon;
            for (int i = 0; i < playersNumber; i++) {
                imageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + this.selectableGods.get(i) + ".png")));
                imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(30,-1, Image.SCALE_SMOOTH));
                iconGods.add(imageIcon);
                //possibleGodsPanel.add(new JButton(chosenGods[i], iconGods.get(i)));+
                this.buttons.add(new JButton(this.selectableGods.get(i), iconGods.get(i)));
                this.buttons.get(i).addActionListener(godChoiceJButtonListener);
                possibleGodsPanel.add(this.buttons.get(i));
            }

        }

        this.isGodChooser = false;
        this.add(possibleGodsPanel);

        this.add(innerPanel, BorderLayout.SOUTH);
        innerPanel.setLayout(new BorderLayout());
        innerPanel2.setLayout(new BorderLayout());
        innerPanel2.add(continueButton, BorderLayout.EAST);
        innerPanel.add(innerPanel2);
        innerPanel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
    }

    public void setGodChoiceSelected(JButton button) {
        if(isGodChooser){
            if(!button.isSelected()){
                if(selectedGodsNumber < playersNumber){
                    button.setSelected(true);
                    selectedGodsNumber++;
                    System.out.println("Select true, " + selectedGodsNumber);
                    this.selectedGods.add(button.getText());
                }//else{ //TooManyGodsSelectedException }
            }
            else {
                button.setSelected(false);
                selectedGodsNumber--;
                System.out.println("Select true, " + selectedGodsNumber);
                this.selectedGods.remove(button.getText());
            }

        }
        else {
            if(!button.isSelected()){
                if(selectedGodsNumber < 1) {
                    button.setSelected(true);
                    System.out.println("Selected true");
                    selectedGodsNumber++;
                    this.selectedGods.add(button.getText());
                }
            }
            else {
                button.setSelected(false);
                System.out.println("Selected false");
                selectedGodsNumber--;
                this.selectedGods.remove(button.getText());
            }
        }

    }

    private void drawGodChoiceAsGodChooser() throws IOException{
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        JPanel titlePanel = new JPanel();
        JPanel possibleGodsPanel = new JPanel();
        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(this);
        JPanel innerPanel = new JPanel();
        JPanel innerPanel2 = new JPanel();

        titlePanel.add(new JLabel("Select " + playersNumber + " Gods"));
        this.add(titlePanel, BorderLayout.NORTH);
        this.setBorder(BorderFactory.createEmptyBorder(0,20,20,20));
        possibleGodsPanel.setLayout(new GridLayout(4,4));

        ArrayList<ImageIcon> iconGods = new ArrayList<>();
        ImageIcon imageIcon;

        List<String> gods;

        gods = View.getGodsNamesList();

        this.godChoiceJButtonListener = new GodChoiceJButtonListener(this);
        for (int i = 0; i < gods.size(); i++) {
            imageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + gods.get(i) + ".png")));
            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(30,-1, Image.SCALE_SMOOTH));
            iconGods.add(imageIcon);
            this.buttons.add(new JButton(gods.get(i), iconGods.get(i)));
            this.buttons.get(i).addActionListener(godChoiceJButtonListener);
            possibleGodsPanel.add(this.buttons.get(i));
        }

        this.add(possibleGodsPanel);

        this.add(innerPanel, BorderLayout.SOUTH);
        innerPanel.setLayout(new BorderLayout());
        innerPanel2.setLayout(new BorderLayout());
        innerPanel2.add(continueButton, BorderLayout.EAST);
        innerPanel.add(innerPanel2);
        innerPanel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
    }

    @Override
    public void showGuiOnTurn() {

        this.removeAll();

        try {


            if(isGodChooser) {
                drawGodChoiceAsGodChooser();
            } else {
                drawGodChoice();
            }


        } catch(IOException e) {

        }

        this.revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Gui gui = Gui.getInstance(null, null);

        if(isGodChooser && this.selectedGods.size() != playersNumber) {
            JOptionPane.showMessageDialog(gui.getMainFrame(), "Error", "You must select " + playersNumber + " gods!", JOptionPane.ERROR_MESSAGE);
        }
        else if(!isGodChooser && this.selectedGods.size() != 1) {
            JOptionPane.showMessageDialog(gui.getMainFrame(), "Error", "You must select one god!", JOptionPane.ERROR_MESSAGE);
        }
        else {

            GodChoiceCommand godChoiceCommand = new GodChoiceCommand(this.selectedGods);
            gui.notify(godChoiceCommand);

            onGodChoiceSent();
        }
    }

    private void onGodChoiceSent() {
        this.removeAll();

        this.add(new LoadingComponent("Waiting..."));

        this.revalidate();
    }
}
