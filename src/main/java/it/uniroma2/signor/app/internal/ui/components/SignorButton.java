/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.ui.components;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;

public class SignorButton extends JButton{
    public SignorButton(String label){
        super(label);
        this.setBackground(Color.decode("#52a677"));
        this.setBounds(1, 1, 1, 1);
        this.setName(label);
    }
    public SignorButton(ImageIcon icon) {
        super(icon);
        cleanIconButton();
    }
    private void cleanIconButton() {
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        addMouseListener(handMouseShape);
    }
    public MouseListener handMouseShape = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    };
}
