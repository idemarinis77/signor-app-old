/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.ui.components;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public class SignorLabelWarning extends JLabel{   

    public SignorLabelWarning(String label) {
        super(label);
        this.setForeground(Color.decode("#cc0000"));
        this.setFont(new Font(label, Font.ITALIC, 11));
    }

}
