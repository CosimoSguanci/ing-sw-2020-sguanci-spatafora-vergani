package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.ui.JGodButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GodScreen is a class which manages the display of Gods during GodChoice gamePhase.
 * In this class there is a distinction between GodChooser and other players.
 *
 * @author Roberto Spatafora
 * @author Andrea Vergani
 */
public class GodScreen extends JPanel {
    private static final Map<String, Map<String, ImageIcon>> allIconGods = new HashMap<>();
    Gui gui = Gui.getInstance();
    private final JLabel titleLabel;
    private final JPanel rightPanel;
    private final ArrayList<JButton> buttons = new ArrayList<>();

    /**
     * This is the constructor of the class. At the moment of creation a new GridLayout
     * is created. It is a 4x4 GridLayout case player is GodChooser, and an only row
     * with as many columns as the number of players involved in the match. This GriLayout
     * contains buttons which contain Gods images
     *
     * @param playersNumber  is an integer number that contains information about the number of players involved in a match.
     * @param selectableGods is a List of Strings which contains Gods names that can be chosen.
     *                       GodChooser will see all the Gods available in game. Other player see Gods chosen bu GodChooser
     * @throws IOException is used in case in which the image we are looking for is not available in specified path.
     */
    public GodScreen(int playersNumber, List<String> selectableGods) throws IOException {
        boolean isGodChooser = this.gui.getController().isClientPlayerGodChooser();

        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        JPanel titlePanel = new JPanel();
        JPanel possibleGodsPanel = new JPanel();
        this.rightPanel = new JPanel();
        this.rightPanel.setLayout(new BorderLayout());
        this.rightPanel.setOpaque(false);

        this.titleLabel = new JLabel();
        Font titleFont = Gui.getFont(Gui.FONT_BOLD, 20);
        this.titleLabel.setFont(titleFont);
        titlePanel.add(this.titleLabel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        this.add(titlePanel, BorderLayout.NORTH);
        this.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // GridLayout gridLayout = new GridLayout(4, 4);
        // gridLayout.setVgap(10);
        // possibleGodsPanel.setLayout(gridLayout); Uncomment for SINGLE BUTTONS GOD

        GridLayout gridLayout = isGodChooser ? new GridLayout(4, 4) : new GridLayout(1, playersNumber);

        possibleGodsPanel.setLayout(gridLayout);

        ArrayList<ImageIcon> iconGods = new ArrayList<>();
        ImageIcon imageIcon;

        List<String> gods = isGodChooser ? View.getGodsNamesList() : selectableGods;

        /*this.godChoiceJButtonListener = new GodChoiceJButtonListener(this);*/
        for (int i = 0; i < gods.size(); i++) {
            imageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + gods.get(i).toLowerCase() + ".png")));
            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(60, -1, Image.SCALE_SMOOTH));
            iconGods.add(imageIcon);

            JGodButton godBtn = new JGodButton(gods.get(i).toUpperCase(), iconGods.get(i), gods.get(i).toLowerCase());

            Font godLabelFont = Gui.getFont(Gui.FONT_BOLD, 14);
            godBtn.setFont(godLabelFont);
            godBtn.setForeground(Color.BLACK); // Color.decode("0xc8102e")

            godBtn.addComponentListener(new ComponentAdapter() {

                @Override
                public void componentResized(ComponentEvent e) {
                    JButton btn = (JButton) e.getComponent();
                    Dimension size = btn.getSize();

                    String god = btn.getText().toLowerCase();

                    if (size.width > 600 || size.height > 600) {
                        btn.setIcon(allIconGods.get(god).get("very_big"));
                    } else if (size.width > 400 || size.height > 400) {
                        btn.setIcon(allIconGods.get(god).get("big"));
                    } else if (size.width > 260 || size.height > 260) {
                        btn.setIcon(allIconGods.get(god).get("medium_big"));
                    } else if (size.width > 70 || size.height > 70) {
                        btn.setIcon(allIconGods.get(god).get("medium"));
                    } else if (size.width > 40 || size.height > 40) {
                        btn.setIcon(allIconGods.get(god).get("medium_small"));
                    } else {
                        btn.setIcon(allIconGods.get(god).get("small"));

                    }

                }

            });

            this.buttons.add(godBtn);
            //this.buttons.get(i).addActionListener(godChoiceJButtonListener);

            godBtn.setHorizontalTextPosition(JButton.CENTER);
            godBtn.setVerticalTextPosition(JButton.BOTTOM);
            godBtn.setOpaque(false);
            godBtn.setContentAreaFilled(false);

            String operSys = System.getProperty("os.name").toLowerCase();

            godBtn.setBorderPainted(!operSys.contains("mac"));
            possibleGodsPanel.add(godBtn);


          /*  JPanel pannel = new JPanel();
            pannel.setLayout(new BoxLayout(pannel, BoxLayout.Y_AXIS));
            godBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            pannel.add(godBtn);

            pannel.setOpaque(false);

            possibleGodsPanel.add(pannel); */
        }

        //commonGodChoose(possibleGodsPanel, continueButton, innerPanel, innerPanel2);

        titlePanel.setOpaque(false);
        //innerPanel.setOpaque(false);
        //innerPanel2.setOpaque(false);
        possibleGodsPanel.setOpaque(false);

        this.add(possibleGodsPanel, BorderLayout.CENTER);
        this.add(this.rightPanel, BorderLayout.EAST);
    }

    /**
     * This static method has the function to load all the Gods images in different dimension.
     * This function was meant to be in a different thread from the main one of the execution.
     * Different dimensions are used to re-size images after a window re-size.
     */
    public static void loadImages() {
        new Thread(() -> {
            List<String> gods = View.getGodsNamesList();

            gods.forEach(god -> {

                try {

                    ImageIcon smallImageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + god + ".png")));
                    smallImageIcon = new ImageIcon(smallImageIcon.getImage().getScaledInstance(20, -1, Image.SCALE_SMOOTH));

                    ImageIcon mediumSmallImageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + god + ".png")));
                    mediumSmallImageIcon = new ImageIcon(mediumSmallImageIcon.getImage().getScaledInstance(40, -1, Image.SCALE_SMOOTH));

                    ImageIcon mediumImageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + god + ".png")));
                    mediumImageIcon = new ImageIcon(mediumImageIcon.getImage().getScaledInstance(65, -1, Image.SCALE_SMOOTH));

                    ImageIcon mediumBigImageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + god + ".png")));
                    mediumBigImageIcon = new ImageIcon(mediumBigImageIcon.getImage().getScaledInstance(85, -1, Image.SCALE_SMOOTH));

                    ImageIcon bigImageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + god + ".png")));
                    bigImageIcon = new ImageIcon(bigImageIcon.getImage().getScaledInstance(110, -1, Image.SCALE_SMOOTH));

                    ImageIcon veryBigImageIcon = new ImageIcon(ImageIO.read(Gui.class.getResource("/images/GodChoice/" + god + ".png")));
                    veryBigImageIcon = new ImageIcon(veryBigImageIcon.getImage().getScaledInstance(180, -1, Image.SCALE_SMOOTH));

                    Map<String, ImageIcon> m = new HashMap<>();
                    m.put("small", smallImageIcon);
                    m.put("medium_small", mediumSmallImageIcon);
                    m.put("medium", mediumImageIcon);
                    m.put("medium_big", mediumBigImageIcon);
                    m.put("big", bigImageIcon);
                    m.put("very_big", veryBigImageIcon);

                    allIconGods.put(god, m);


                } catch (IOException e) {
                    e.printStackTrace();
                }


            });


        }).start();
    }

    /**
     * This package protected method is a simple setter of the title of the titleLabel of the class.
     * This is a good solution in order to creates instances of this class, distinguish at the moment
     * of the invocation, different contexts and for each set the desired title.
     *
     * @param title
     */
    void setTitle(String title) {
        this.titleLabel.setText(title);
        this.revalidate();
        this.repaint();
    }

    /**
     * This method is a simple getter of the Gods buttons available in this class.
     *
     * @return an ArrayList of JButton which contains as many buttons as many Gods are available in a specific turn of GodChoice game phase.
     */
    ArrayList<JButton> getButtons() {
        return this.buttons;
    }

    /**
     * This method is a getter of the right panel located east in this class.
     *
     * @return a reference to the east panel of the class.
     */
    JPanel getRightPanel() {
        return this.rightPanel;
    }
}
