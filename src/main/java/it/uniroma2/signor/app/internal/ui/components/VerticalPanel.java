/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.ui.components;

import javax.swing.*;
import java.awt.*;

public class VerticalPanel extends JPanel {

    public VerticalPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
    }

    public VerticalPanel(Color backgroundColor) {
        this();
        setOpaque(true);
        setBackground(backgroundColor);
    }
}
