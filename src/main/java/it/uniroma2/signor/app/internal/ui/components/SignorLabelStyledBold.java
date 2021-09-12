/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.ui.components;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import org.cytoscape.util.swing.OpenBrowser;


public class SignorLabelStyledBold extends JLabel{
     
    private final OpenBrowser openBrowser;
    private URI link;
    private boolean visited = false;
    private static final Color baseColor = new Color(56, 76, 148);
    private static final Color visitedColor = new Color(39, 25, 91);
    private static final Color hoverColor = new Color(129, 20, 20);

    private static final Font DEFAULT_FONT;
    private static final Font BOLD_FONT;
    
    static {
        Font font = UIManager.getFont("Label.font");
        font =(font != null) ? font : new Font("SansSerif", Font.PLAIN, 11);
        Map<TextAttribute, Object> attributes =  new HashMap<>(font.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_DOTTED);
        DEFAULT_FONT = font.deriveFont(attributes);
        BOLD_FONT = DEFAULT_FONT.deriveFont(Font.BOLD);
    }
    public SignorLabelStyledBold (String label){
          this(label, "", null, true);
    }

    public SignorLabelStyledBold(String label, String link, final OpenBrowser openBrowser) {
        this(label, link, openBrowser, false);
    }

    public SignorLabelStyledBold(String label, String link, final OpenBrowser openBrowser, boolean bold) {
        super(label.toUpperCase());
        this.openBrowser = openBrowser;
        if (bold) this.setFont(new Font(label, Font.BOLD, 11));
        if (openBrowser != null) {
            setup(label, URI.create(link));
            setuplistener();
        }
    }

    public SignorLabelStyledBold(ImageIcon icon, String link, final OpenBrowser openBrowser, boolean bold) {
        super(icon);
        this.openBrowser = openBrowser;
        this.link = URI.create(link);
        if (openBrowser != null) setuplistener();
    }
    
    
    public void setup(String t, URI u) {
        link = u;
        setText(t);
        setToolTipText(link.toString());
        setBackground(null);
        setOpaque(false);
        setBorder(null);
        setForeground(baseColor);
    }
    
    public void setuplistener(){
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                open(link);
                visited = true;
                setForeground(visitedColor);
            }

            public void mouseEntered(MouseEvent e) {
                // setText(text,false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setForeground(hoverColor);

            }

            public void mouseExited(MouseEvent e) {
                // setText(text,true);
                setCursor(Cursor.getDefaultCursor());
                if (!visited) {
                    setForeground(baseColor);
                } else {
                    setForeground(visitedColor);
                }
            }
        });
    }

    private void open(URI link) {
        openBrowser.openURL(link.toString());
    }   
}
