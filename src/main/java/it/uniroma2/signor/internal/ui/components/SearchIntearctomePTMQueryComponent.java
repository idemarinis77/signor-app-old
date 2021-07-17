package it.uniroma2.signor.internal.ui.components;

import org.cytoscape.application.swing.search.NetworkSearchTaskFactory;
import org.cytoscape.util.swing.LookAndFeelUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static javax.swing.GroupLayout.Alignment.CENTER;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import org.cytoscape.util.swing.IconManager;
import org.cytoscape.util.swing.TextIcon;
import it.uniroma2.signor.internal.ConfigResources;
import it.uniroma2.signor.internal.utils.IconUtils;
import it.uniroma2.signor.internal.managers.SignorManager;

public class SearchIntearctomePTMQueryComponent extends JTextField {
    private static final long serialVersionUID = 1L;
    private String DEF_SEARCH_TEXT = "Press button to get interactome expanded with PTM →";    
    final int vgap = 1;
    final int hgap = 5;    
    Color msgColor;       
    private SignorManager manager;

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
 
        //setToolTipText(tooltip);
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
