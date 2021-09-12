/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.ui.components;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.BorderFactory;

public class SignorPanelRow extends JPanel{
      
    public SignorPanelRow (Integer row, Integer columns){
        super(new GridLayout(row, columns));
        this.setBackground(Color.white);
        this.setBorder(BorderFactory.createBevelBorder(0, Color.white, Color.white, new Color(82,166,119), Color.white));
    }

}
