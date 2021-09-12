/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.ui.components;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

public class SignorLabelMore extends JLabel{   
    private final HTMLLabel message;
    private JPopupMenu popupMenu = new JPopupMenu();
    public SignorLabelMore(SignorManager manager, String label, String text) {
        super(label);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setForeground(Color.decode("#6600FF"));
        message = new HTMLLabel(text);
        message.setSize(new Dimension(15, 15));
        message.setPreferredSize(new Dimension(300, message.getPreferredSize().height));
        message.enableHyperlinks(manager);
        message.setBorder(new EmptyBorder(0,5,0,5));
        popupMenu.add(message);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(SignorLabelMore.this, e.getX(), e.getY());
            }
        });
    }

}
