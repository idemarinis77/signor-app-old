package it.uniroma2.signor.app.internal.ui.components;

import org.cytoscape.util.swing.LookAndFeelUtil;
import javax.swing.*;
import java.awt.*;
import it.uniroma2.signor.app.internal.managers.SignorManager;

public class SearchIntearctomePTMQueryComponent extends JTextField {
    private static final long serialVersionUID = 1L;
    private final String DEF_SEARCH_TEXT = "Press search symbol on the right to download the complete interactome →";    
    final int vgap = 1;
    final int hgap = 5;    
    Color msgColor;       
    private final SignorManager manager;

    public SearchIntearctomePTMQueryComponent(SignorManager manager) {
        super();
        this.manager = manager;
        init();    
    }

    void init() {        
        msgColor = UIManager.getColor("Label.disabledForeground");
        setEditable(false);
        setMinimumSize(getPreferredSize());
        setBorder(BorderFactory.createEmptyBorder(vgap, hgap, vgap, hgap));
        setFont(getFont().deriveFont(LookAndFeelUtil.getSmallFontSize()));     
        requestFocusInWindow();
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (getText() == null || getText().trim().isEmpty()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHints(
                    new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
            // Set the font
            g2.setFont(getFont());
            // Get the FontMetrics
            FontMetrics metrics = g2.getFontMetrics(getFont());
            // Determine the X coordinate for the text
            // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
            int y = (metrics.getHeight() / 2) + metrics.getAscent() + vgap;
            // Draw
            g2.setColor(msgColor);
            g2.drawString(DEF_SEARCH_TEXT, hgap, y);
            g2.dispose();
        }
    }
  }
