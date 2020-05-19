package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.GodChoiceJButtonListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GodChoice extends JPanel {
    private Boolean isGodChooser;
    private ArrayList<JButton> buttons = new ArrayList<>();
    private GodChoiceJButtonListener godChoiceJButtonListener;
    private int selectedGodsNumber = 0;
    private int playersNumber;

    public GodChoice(int playersNumber) throws IOException {
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);
        this.playersNumber = playersNumber;

        JPanel titlePanel = new JPanel();
        JPanel possibleGodsPanel = new JPanel();
        JButton continueButton = new JButton("Continue");
        JPanel innerPanel = new JPanel();
        JPanel innerPanel2 = new JPanel();

        titlePanel.add(new JLabel("Select " + playersNumber + " Gods"));
        this.add(titlePanel, BorderLayout.NORTH);
        this.setBorder(BorderFactory.createEmptyBorder(0,20,20,20));
        possibleGodsPanel.setLayout(new GridLayout(4,4));

        ArrayList<ImageIcon> iconGods = new ArrayList<>();
        ArrayList<String> godNames = new ArrayList<>(GodsUtils.getGodsInfo().keySet());
        ImageIcon imageIcon;

        this.godChoiceJButtonListener = new GodChoiceJButtonListener(this);
        for (int i = 0; i < godNames.size(); i++) {
            imageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + godNames.get(i) + ".png")));
            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(30,-1, Image.SCALE_SMOOTH));
            iconGods.add(imageIcon);
            this.buttons.add(new JButton(godNames.get(i), iconGods.get(i)));
            this.buttons.get(i).addActionListener(godChoiceJButtonListener);
            possibleGodsPanel.add(this.buttons.get(i));
        }

        this.isGodChooser = true;
        this.add(possibleGodsPanel);

        this.add(innerPanel, BorderLayout.SOUTH);
        innerPanel.setLayout(new BorderLayout());
        innerPanel2.setLayout(new BorderLayout());
        innerPanel2.add(continueButton, BorderLayout.EAST);
        innerPanel.add(innerPanel2);
        innerPanel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));

    }

    //public GodChoice(int playersNumber, List<String> chosenGods) throws IOException {
    public GodChoice(int playersNumber, List<String> chosenGods) throws IOException {
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        JButton continueButton = new JButton("Continue");
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
                imageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + chosenGods.get(i) + ".png")));
                imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(30,-1, Image.SCALE_SMOOTH));
                iconGods.add(imageIcon);
                //possibleGodsPanel.add(new JButton(chosenGods[i], iconGods.get(i)));

                this.buttons.add(new JButton(chosenGods.get(i), iconGods.get(i)));
                this.buttons.get(i).addActionListener(godChoiceJButtonListener);
                possibleGodsPanel.add(this.buttons.get(i));
            }

        }
        else {
            possibleGodsPanel.setLayout(new GridLayout(1,3));

            ArrayList<ImageIcon> iconGods = new ArrayList<>();
            ImageIcon imageIcon;
            for (int i = 0; i < playersNumber; i++) {
                imageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + chosenGods.get(i) + ".png")));
                imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(30,-1, Image.SCALE_SMOOTH));
                iconGods.add(imageIcon);
                //possibleGodsPanel.add(new JButton(chosenGods[i], iconGods.get(i)));+
                this.buttons.add(new JButton(chosenGods.get(i), iconGods.get(i)));
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
                }//else{ //TooManyGodsSelectedException }
            }
            else {
                button.setSelected(false);
                selectedGodsNumber--;
                System.out.println("Select true, " + selectedGodsNumber);
            }

        }
        else {
            if(!button.isSelected()){
                if(selectedGodsNumber < 1) {
                    button.setSelected(true);
                    System.out.println("Selected true");
                    selectedGodsNumber++;
                }
            }
            else{
                button.setSelected(false);
                System.out.println("Selected false");
                selectedGodsNumber--;
            }
        }

    }
}
