package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.listeners.QuitButtonListener;
import it.polimi.ingsw.view.gui.ui.JRoundButton;
import it.polimi.ingsw.view.gui.ui.LoadingComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

/**
 * InitialInfo is a class in which the component during before
 * and during nicknames and colors choice is managed.
 * This class also shows different messages through JDialog to users who
 * make wrong choices.
 *
 * @author Roberto Spatafora
 * @author Andrea Vergani
 */
public class InitialInfo extends JPanel implements ActionListener {

    private static final String loadingMsgBefore = "Waiting for other players...";
    private static final String loadingMsgAfter = "Waiting for other players...";

    private static final String errorDialogTitle = "Error";
    private static final String errorDialogMessage = "You can't use this nickname!" + System.lineSeparator() +
            "Nicknames already used are: ";
    private final String standardImgPath = "/images/InitialInfo/";
    private final Image backgroundImage = new ImageIcon(getClass().getResource(standardImgPath + "backgroundTemple.png")).getImage();
    private final Font font = Gui.getFont(Gui.FONT_REGULAR, 20);
    private final Color textColor = Color.WHITE;
    private final Gui gui;
    private JTextField nicknameTextField;
    private JComboBox<PrintableColor> color;
    private List<PrintableColor> selectableColors;
    private List<String> selectedNicknames;
    private String nickname;


    /**
     * This is the creator of the class. At the moment of creation a reference to Gui
     * is associated to this class and a new component which alert users to wait for
     * opponents is created.
     */
    public InitialInfo() {

        this.gui = Gui.getInstance();
        this.add(new LoadingComponent(loadingMsgBefore, Color.WHITE));
    }

    /**
     * This is a simple setter of the List of printableColors player can choose.
     * @param selectableColors contains colors users can still select.
     */
    public void setSelectableColors(List<PrintableColor> selectableColors) {
        this.selectableColors = selectableColors;

        this.showGuiOnTurn();
    }

    /**
     * This method is a simple setter of the String list selectedNickname,
     * attribute of the class. It contains all the nicknames already chosen from players in a match.
     * @param selectedNicknames is a list of already chosen nicknames in the match.
     */
    public void setSelectedNicknames(List<String> selectedNicknames) {
        this.selectedNicknames = selectedNicknames;
    }

    /**
     * This method is used to set the background of the component layout.
     * @param g contains a reference to the component we want to set a background image.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    /**
     * This method manages the InitialInfo component layout according to the current turn.
     * Gui of current player displays a textfield in which player inputs its nickname and
     * a vertical glue which contains selectable colors.
     * Others players' gui displays a waiting message.
     */
    public void showGuiOnTurn() {

        this.removeAll();

        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);

        //borderLayout for nickname and color, in the north
        JPanel nicknameColorPanel = new JPanel();
        nicknameColorPanel.setLayout(new BorderLayout());
        nicknameColorPanel.setOpaque(false);

        //nickname
        JPanel panelNickname = new JPanel();
        panelNickname.setLayout(new BoxLayout(panelNickname, BoxLayout.Y_AXIS));
        panelNickname.setOpaque(false);
        JPanel centredNickname = new JPanel();
        centredNickname.setOpaque(false);
        JLabel labelNickname = new JLabel("Insert a nickname:");
        labelNickname.setForeground(textColor);
        labelNickname.setFont(font);
        centredNickname.add(labelNickname);
        this.nicknameTextField = new JTextField();
        this.nicknameTextField.setFont(this.font);
        this.nicknameTextField.setHorizontalAlignment(JTextField.CENTER);
        panelNickname.add(centredNickname);
        panelNickname.add(Box.createVerticalGlue());
        panelNickname.add(this.nicknameTextField);

        //color
        JPanel panelColor = new JPanel();
        panelColor.setLayout(new BoxLayout(panelColor, BoxLayout.Y_AXIS));
        panelColor.setOpaque(false);
        JPanel centredColor = new JPanel();
        centredColor.setOpaque(false);
        JLabel labelColor = new JLabel("Choose a color: ");
        labelColor.setForeground(textColor);
        labelColor.setFont(font);
        centredColor.add(labelColor);

        if (selectableColors == null) {
            this.selectableColors = PrintableColor.getColorList();
        }

        PrintableColor[] colors = new PrintableColor[selectableColors.size()];

        colors = this.selectableColors.toArray(colors);

        this.color = new JComboBox<>(colors);

        ((JLabel) this.color.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        panelColor.add(centredColor);
        panelNickname.add(Box.createVerticalGlue());
        panelColor.add(this.color);

        panelNickname.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        nicknameColorPanel.add(panelNickname, BorderLayout.NORTH);
        nicknameColorPanel.add(panelColor, BorderLayout.SOUTH);
        this.add(nicknameColorPanel, BorderLayout.NORTH);


        //button to continue must be south-east
        ImageIcon continueImg = new ImageIcon(getClass().getResource(this.standardImgPath + "button-play-normal.png"));
        int buttonWidth = 60;
        continueImg = new ImageIcon(continueImg.getImage().getScaledInstance(buttonWidth, -1, Image.SCALE_SMOOTH));
        JButton continueButton = new JButton(continueImg);
        continueButton.addActionListener(this);
        JPanel innerPanel = new JPanel();
        JPanel innerPanel2 = new JPanel();
        innerPanel.setLayout(new BorderLayout());
        innerPanel.setOpaque(false);
        innerPanel2.setLayout(new BorderLayout());
        innerPanel2.setOpaque(false);
        innerPanel2.add(continueButton, BorderLayout.EAST);

        //button to quit must be south-west
        String externalImgPath = "/images/";
        ImageIcon quitImg = new ImageIcon(getClass().getResource(externalImgPath + "exit.png"));
        quitImg = new ImageIcon(quitImg.getImage().getScaledInstance(buttonWidth, -1, Image.SCALE_SMOOTH));
        JRoundButton quitButton = new JRoundButton(quitImg);

        quitButton.addActionListener(new QuitButtonListener(this));
        innerPanel2.add(quitButton, BorderLayout.WEST);

        innerPanel.add(innerPanel2);
        this.add(innerPanel, BorderLayout.SOUTH);

        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        this.revalidate();
        this.repaint();
    }

    /**
     * This method manages the activities made on continue button.
     * Case player choose a nickname already chosen from an opponent player
     * none information is sent to the server and the user is notified about a
     * bad nickname chosen. In a positive case it send the info to
     * the server and the game goes on.
     * @param e contains reference to the event that triggered the listener.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        this.nickname = nicknameTextField.getText();

        if (this.selectedNicknames.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(nickname.toLowerCase())) {
            JOptionPane.showMessageDialog(gui.getMainFrame(), errorDialogMessage + View.listToStringBuilder(selectedNicknames), errorDialogTitle, JOptionPane.ERROR_MESSAGE);
        } else {
            PrintableColor color = (PrintableColor) this.color.getSelectedItem();
            InitialInfoCommand initialInfoCommand = new InitialInfoCommand(this.nickname, color);
            gui.notify(initialInfoCommand);

            onInitialInfoSent();
        }
    }

    /**
     * This private method manages the activities once a user choose a valid nickname and a color.
     * It handles the content removal of the component, displays a loading message and set the
     * playerNickname of the Client associated to the Gui with the nickname chosen
     *
     */
    private void onInitialInfoSent() {
        this.removeAll();

        this.add(new LoadingComponent(loadingMsgAfter, Color.WHITE));

        this.gui.getController().setClientPlayerNickname(this.nickname);

        this.revalidate();
        this.repaint();
    }
}
