package it.polimi.ingsw.view.gui.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * This class defines a circular button.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 * @author Andrea Vergani
 */
public class JRoundButton extends JButton {

    protected Shape shape, base;

    /**
     * This is a constructor of the class. It allows to creates new
     * instances of the class by giving as parameter only an icon which would be used in button.
     *
     * @param icon contains a reference to the button icon.
     */
    public JRoundButton(Icon icon) {
        setModel(new DefaultButtonModel());
        init(null, icon);
        if (icon == null) {
            return;
        }
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setBackground(Color.BLACK);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setAlignmentY(Component.TOP_ALIGNMENT);
        initShape();
    }


    /**
     * This method is used to initialize the circular shape of the button
     */
    private void initShape() {
        if (!getBounds().equals(base)) {
            Dimension s = getPreferredSize();
            base = getBounds();
            shape = new Ellipse2D.Float(0, 0, s.width - 1, s.height - 1);
        }
    }

    /**
     * This method gives information about a preferred size of a button
     *
     * @return a Dimension that would be preferred for the button
     */
    @Override
    public Dimension getPreferredSize() {
        Icon icon = getIcon();
        Insets i = getInsets();
        int iw = Math.max(icon.getIconWidth(), icon.getIconHeight());
        return new Dimension(iw + i.right + i.left, iw + i.top + i.bottom);
    }

    @Override
    public boolean contains(int x, int y) {
        initShape();
        return shape.contains(x, y);
    }
}
