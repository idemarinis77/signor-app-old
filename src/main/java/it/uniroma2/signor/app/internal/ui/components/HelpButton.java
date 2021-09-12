package it.uniroma2.signor.app.internal.ui.components;


import it.uniroma2.signor.app.internal.utils.IconUtils;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HelpButton extends SignorButton {
    private static final ImageIcon helpIcon = IconUtils.createImageIcon("/images/help_small.png");
    private JPopupMenu popupMenu = new JPopupMenu();
    private final HTMLLabel message;
    private String helpText;


    public HelpButton(SignorManager manager,  String helpText) {
        super(helpIcon);
        this.helpText = "<html>" + helpText + "</html>";
        this.setBorder(new EmptyBorder(0, 5, 0, 0));
        message = new HTMLLabel(this.helpText);
        message.setSize(new Dimension(15, 15));
        message.setPreferredSize(new Dimension(300, message.getPreferredSize().height));
        message.enableHyperlinks(manager);
        message.setBorder(new EmptyBorder(0,5,0,5));
        popupMenu.add(message);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(HelpButton.this, e.getX(), e.getY());
            }
        });
    }
    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
        this.message.setText(this.helpText);
    }
}
