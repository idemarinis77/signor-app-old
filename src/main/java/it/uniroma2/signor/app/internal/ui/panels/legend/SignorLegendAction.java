package it.uniroma2.signor.app.internal.ui.panels.legend;

import java.awt.event.ActionEvent;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;

public class SignorLegendAction extends AbstractCyAction {
	private static final long serialVersionUID = 1L;
	private final CySwingApplication desktopApp;
	private final CytoPanel cytoPanelEast;
	private final SignorLegendPanel legendPanel;
	
	public SignorLegendAction(CySwingApplication desktop, SignorLegendPanel myPanel){
		super("Signor PANEL");
		desktopApp = desktop;
		cytoPanelEast = this.desktopApp.getCytoPanel(CytoPanelName.EAST);
		legendPanel = myPanel;
	}
	
	/**
	 *  actionPerformed(ActionEvent e)
	 *
	 * @param e - The event record provide the command name, source, key modifiers, etc.
	 */
	public void actionPerformed(ActionEvent e) {
		// If the state of the cytoPanelEast is HIDE, show it
		if (cytoPanelEast.getState() == CytoPanelState.HIDE) 
			cytoPanelEast.setState(CytoPanelState.DOCK);

		// Select my panel
		int index = cytoPanelEast.indexOfComponent(legendPanel);
		if (index >= 0)
			cytoPanelEast.setSelectedIndex(index);
	}

}
