package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.controller.commands.GodChoiceCommand;
import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.network.client.controller.Controller;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.GodChoiceJButtonListener;
import it.polimi.ingsw.view.gui.ui.JRoundButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GodChoice extends JPanel implements ActionListener {

    private final String standardImgPath = "src/main/resources/images/GodChoice/";
    private final Image backgroundImage = new ImageIcon(standardImgPath + "olympus.png").getImage();
    private ArrayList<JButton> buttons = new ArrayList<>();
    private GodChoiceJButtonListener godChoiceJButtonListener;
    private int selectedGodsNumber = 0;

    private int playersNumber;

    private Gui gui;

    private List<String> selectedGods;
    private List<String> selectableGods;

    private Font titleFont = Gui.getFont(Gui.FONT_BOLD, 20);
    private Font godLabelFont = Gui.getFont(Gui.FONT_REGULAR, 14);


    private Map<String, Map<String, ImageIcon>> allIconGods = new HashMap<>(); // todo synchronized

    public void setSelectableGods(List<String> selectableGods) {
        this.selectableGods = selectableGods;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public GodChoice(){

        this.gui = Gui.getInstance();
        this.selectedGods = new ArrayList<>();
        this.add(new LoadingComponent("Waiting...", Color.BLACK));

        new Thread(() -> {
            List<String> gods =  View.getGodsNamesList();

            gods.forEach(god -> {

                try {

                    ImageIcon smallImageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + god + ".png")));
                    smallImageIcon = new ImageIcon(smallImageIcon.getImage().getScaledInstance(40,-1, Image.SCALE_SMOOTH));

                    ImageIcon mediumImageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + god + ".png")));
                    mediumImageIcon = new ImageIcon(mediumImageIcon.getImage().getScaledInstance(70,-1, Image.SCALE_SMOOTH));

                    ImageIcon bigImageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + god + ".png")));
                    bigImageIcon = new ImageIcon(bigImageIcon.getImage().getScaledInstance(120,-1, Image.SCALE_SMOOTH));

                    Map<String, ImageIcon> m = new HashMap<>();
                    m.put("small", smallImageIcon);
                    m.put("medium", mediumImageIcon);
                    m.put("big", bigImageIcon);

                    allIconGods.put(god, m);


                } catch(IOException e) {
                    e.printStackTrace();
                }


            });

        }).start();
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
            commonGodChoose2(possibleGodsPanel, iconGods);

        }
        else {
            possibleGodsPanel.setLayout(new GridLayout(1,3));

            ArrayList<ImageIcon> iconGods = new ArrayList<>();
            ImageIcon imageIcon;
            commonGodChoose2(possibleGodsPanel, iconGods);

        }

        commonGodChoose(possibleGodsPanel, continueButton, innerPanel, innerPanel2);
    }

    private void commonGodChoose2(JPanel possibleGodsPanel, ArrayList<ImageIcon> iconGods) throws IOException {
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

    private void drawGodChoiceAsGodChooser() throws IOException{
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        JPanel titlePanel = new JPanel();
        JPanel possibleGodsPanel = new JPanel();
        ImageIcon startImg = new ImageIcon("src/main/resources/images/done.png");
        startImg = new ImageIcon(startImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        JRoundButton continueButton = new JRoundButton(startImg);
        continueButton.addActionListener(this);
        JPanel innerPanel = new JPanel();
        JPanel innerPanel2 = new JPanel();

        JLabel titleLabel = new JLabel("Select " + playersNumber + " Gods");
        titleLabel.setFont(this.titleFont);
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        this.add(titlePanel, BorderLayout.NORTH);
        this.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));
        possibleGodsPanel.setLayout(new GridLayout(4,4));

      //  possibleGodsPanel.setSize(gui.getMainFrame().getSize().width, gui.getMainFrame().getSize().height);

        ArrayList<ImageIcon> iconGods = new ArrayList<>();
        ImageIcon imageIcon;

        List<String> gods;

        gods = View.getGodsNamesList();

        this.godChoiceJButtonListener = new GodChoiceJButtonListener(this);
        for (int i = 0; i < gods.size(); i++) {
            imageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + gods.get(i) + ".png")));
            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(60,-1, Image.SCALE_SMOOTH));
            iconGods.add(imageIcon);

            JButton godBtn = new JButton(gods.get(i), iconGods.get(i));

            godBtn.setFont(this.godLabelFont);

            godBtn.addComponentListener(new ComponentAdapter() {

                @Override
                public void componentResized(ComponentEvent e) {
                    JButton btn = (JButton) e.getComponent();
                    Dimension size = btn.getSize();
                    Insets insets = btn.getInsets();

                    if(size.width > 300) {
                        btn.setIcon(allIconGods.get(btn.getText()).get("big"));
                    } else if(size.width > 70) {
                        btn.setIcon(allIconGods.get(btn.getText()).get("medium"));
                    } else {
                        btn.setIcon(allIconGods.get(btn.getText()).get("small"));

                    }

                   /* Image scaled = ((ImageIcon)btn.getIcon()).getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
                    btn.setIcon(new ImageIcon(scaled)); */
                }

            });

            this.buttons.add(godBtn);
            this.buttons.get(i).addActionListener(godChoiceJButtonListener);

            godBtn.setHorizontalTextPosition(JButton.CENTER);
            godBtn.setVerticalTextPosition(JButton.BOTTOM);
            possibleGodsPanel.add(godBtn);
        }

        commonGodChoose(possibleGodsPanel, continueButton, innerPanel, innerPanel2);

        titlePanel.setOpaque(false);
        innerPanel.setOpaque(false);
        innerPanel2.setOpaque(false);
        possibleGodsPanel.setOpaque(false);
    }

    private void commonGodChoose(JPanel possibleGodsPanel, JButton continueButton, JPanel innerPanel, JPanel innerPanel2) {
        this.add(possibleGodsPanel);

        this.add(innerPanel, BorderLayout.SOUTH);
        innerPanel.setLayout(new BorderLayout());
        innerPanel2.setLayout(new BorderLayout());
        innerPanel2.add(continueButton, BorderLayout.EAST);
        innerPanel.add(innerPanel2);
        innerPanel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
    }

    public void setGodChoiceSelected(JButton button) {
        if(gui.getController().isClientPlayerGodChooser()){
            if(!button.isSelected()){
                if(selectedGodsNumber < playersNumber){
                    button.setSelected(true);
                    button.setBorder( BorderFactory.createLineBorder(Color.GREEN, 2, true));
                    selectedGodsNumber++;
                    this.selectedGods.add(button.getText());
                }
            }
            else {
                button.setSelected(false);
               button.setBorder(UIManager.getBorder("Button.border"));
                selectedGodsNumber--;
                this.selectedGods.remove(button.getText());
            }

        }
        else {
            if(!button.isSelected()){
                if(selectedGodsNumber < 1) {
                    button.setSelected(true);
                    selectedGodsNumber++;
                    this.selectedGods.add(button.getText());
                }
            }
            else {
                button.setSelected(false);
                selectedGodsNumber--;
                this.selectedGods.remove(button.getText());
            }
        }

    }

    // todo add interface
    public void showGuiOnTurn() {

        this.playersNumber = gui.getPlayersNumber();


        this.removeAll();

        try {


            if(this.gui.getController().isClientPlayerGodChooser()) {
                drawGodChoiceAsGodChooser();
            } else {
                drawGodChoice();
            }


        } catch(IOException e) {

        }


        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        boolean isGodChooser = gui.getController().isClientPlayerGodChooser();

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
        this.add(new LoadingComponent("Waiting...", Color.RED));
        this.revalidate();
    }
}
