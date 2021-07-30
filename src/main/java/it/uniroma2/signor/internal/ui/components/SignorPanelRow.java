/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.ui.components;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import it.uniroma2.signor.internal.managers.SignorManager;

public class SignorPanelRow extends JPanel{
    
    private final SignorManager manager;
    
    public SignorPanelRow (Integer row, Integer columns, SignorManager manager){
        super(new GridLayout(row, columns));
        this.setBackground(Color.white);
        this.setBorder(BorderFactory.createBevelBorder(0, Color.white, Color.white, new Color(82,166,119), Color.white));
        this.manager = manager;
    }     

}
