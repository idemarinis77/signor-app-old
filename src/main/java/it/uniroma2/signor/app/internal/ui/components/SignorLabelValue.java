/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.ui.components;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public class SignorLabelValue extends JLabel{   

    public SignorLabelValue(String label, Boolean bold) {
        super(label);
        this.setForeground(Color.decode("#000000"));
        if(bold)
          this.setFont(new Font(label, Font.BOLD, 10));
        else this.setFont(new Font(label, Font.PLAIN, 10));
    }

}
