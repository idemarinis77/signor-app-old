package it.uniroma2.signor.internal.ui.components;

import org.cytoscape.application.swing.search.NetworkSearchTaskFactory;
import org.cytoscape.util.swing.LookAndFeelUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static javax.swing.GroupLayout.Alignment.CENTER;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import org.cytoscape.util.swing.IconManager;
import org.cytoscape.util.swing.TextIcon;
import it.uniroma2.signor.internal.ConfigResources;
import it.uniroma2.signor.internal.utils.IconUtils;
import it.uniroma2.signor.internal.managers.SignorManager;

public class SearchPTHQueryComponent1 extends JPanel {
    private static final long serialVersionUID = 1L;
    private String DEF_SEARCH_TEXT = "← Select pathway from list";
    private static final String PATHWAY_TOOLTIP = "Pathway";
    private static final int BUTTON_SIZE = 200;
    private static final Icon SIGNORPTH_ICON=IconUtils.createImageIcon(ConfigResources.icon_path);
    final int vgap = 1;
    final int hgap = 5;
    //final String tooltip;
    Color msgColor;
    
    private JButton pathwaylist;
    private JScrollPane queryScroll = null;
    private JPopupMenu popup = null;
    private ArrayList<String> pathInfo;
    private String selectedPathway;
    private JTextField queryTextField;
    private SignorManager manager;

    public SearchPTHQueryComponent1(ArrayList<String> pathInfo, SignorManager manager) {
        super();
        this.pathInfo = pathInfo;
        this.manager = manager;
        init();
        //updatePathway();
  
        //tooltip = "Select PATHWAY " + (LookAndFeelUtil.isMac() ? "Command" : "Ctrl") + "+ENTER to run the search";
        //organismManager.addPropertyChangeListener("organisms", evt -> updateOrganisms());
	//organismManager.addPropertyChangeListener("loadRemoteOrganismsException", evt -> updateOrganisms());
    }

    void init() {
        setBackground(UIManager.getColor("Table.background"));
		
	GroupLayout layout = new GroupLayout(this);
	setLayout(layout);
	layout.setAutoCreateContainerGaps(false);
	layout.setAutoCreateGaps(false);
		
	layout.setHorizontalGroup(layout.createSequentialGroup()
				
                                .addComponent(getQueryTextField(), DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
				
		);
	/*layout.setVerticalGroup(layout.createParallelGroup(CENTER, true)
				.addGap(0, 0, Short.MAX_VALUE)
				.addComponent(getPathwayButton(), DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(getQueryTextField(), DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
				.addGap(0, 0, Short.MAX_VALUE)
	);*/
		
		/*String tooltip = "Press " + (LookAndFeelUtil.isMac() ? "Command" : "Ctrl") + "+ENTER to run the search";
		getQueryTextField().setToolTipText(tooltip);*/
		//getQueryTextArea().setToolTipText(tooltip);
        
        //DI PRIMA
//        msgColor = UIManager.getColor("Label.disabledForeground");
//        //queryTextArea.setEditable(false);
//        setMinimumSize(getPreferredSize());
//        setBorder(BorderFactory.createEmptyBorder(vgap, hgap, vgap, hgap));
//        setFont(getFont().deriveFont(LookAndFeelUtil.getSmallFontSize()));
//
//        addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                showQueryPopup();
//            }
//        });
//        
//        setToolTipText(tooltip);
//        requestFocusInWindow();
        }
        public String getSelectedPathway() {
		return selectedPathway;
	}

        public boolean isReady() {
		return selectedPathway != null;
	}

        private JButton getPathwayButton() {
            if (pathwaylist == null) {
                    pathwaylist = new JButton();
                    pathwaylist.setToolTipText(PATHWAY_TOOLTIP);
                    pathwaylist.setContentAreaFilled(false);
                    pathwaylist.setBorder(BorderFactory.createCompoundBorder(
                                            BorderFactory.createEmptyBorder(1, 1, 1, 0),
                                            BorderFactory.createMatteBorder(0, 0, 0, 1, UIManager.getColor("Separator.foreground"))
                    ));

                    Dimension d = new Dimension(BUTTON_SIZE, Math.max(BUTTON_SIZE, getQueryTextField().getPreferredSize().height));
                    pathwaylist.setMinimumSize(d);
                    
                    pathwaylist.setPreferredSize(d);
                    pathwaylist.addActionListener(evt -> {
                            /*if (!organismManager.isInitialized())
                                    return;

                            if (organismManager.getLoadRemoteOrganismsErrorMessage() == null)*/
                                    showPathwayPopup();
                            /*else // Try to load organisms again...
                                    organismManager.loadRemoteOrganisms();*/
                    });
            }

            return pathwaylist;
	}


        private void updatePathway() {
	    if (selectedPathway == null){
                for (String pathway_in_list: this.pathInfo) {
                    if(pathway_in_list.startsWith("sig_id"))
                            continue;
                    if(selectedPathway == null){
                        selectedPathway = pathway_in_list.split("\t")[0];
                        String selectedPathwayName = pathway_in_list.split("\t")[1];
                        manager.utils.info("Method updatePathway "+selectedPathway+" "+selectedPathwayName);                        
                    }
                    else
                       break;
                }		
            }
        }
	
	private void setSelectedPathway(String pathId, String pathName) {
		boolean changed = (pathId == null && selectedPathway != null)
				|| (pathId != null && !pathId.equals(selectedPathway));
		String oldValue = selectedPathway;
		selectedPathway = pathId;
		try {
                    if (changed) {
                        getPathwayButton().setToolTipText(
                                selectedPathway != null ? 
                                PATHWAY_TOOLTIP + " (" + pathName + ")" : PATHWAY_TOOLTIP
                        );
                        
                        //getOrganismButton().setIcon(selectedPathway != null ? getIcon(selectedOrganism) : null);

                        //getOptionsPanel().update();

                        firePropertyChange("selectedPathway", oldValue, pathId);
                        fireQueryChanged();
                    }
                }
                catch (Exception e){
                    manager.utils.error("Method setSelectedPathway "+e.toString());
                }
	}
	
	/*private void showOrganismsException(String errorMessage) {
		selectedOrganism = null;
		getOrganismButton().setToolTipText("<html>" + errorMessage + "<br><br><b>(Click to try again)</b></html>");
		IconManager iconManager = serviceRegistrar.getService(IconManager.class);
		TextIcon icon = new TextIcon(IconManager.ICON_TIMES_CIRCLE, iconManager.getIconFont(24.0f),
				LookAndFeelUtil.getErrorColor(), ICON_SIZE, ICON_SIZE);
		getOrganismButton().setIcon(icon);
		fireQueryChanged();
	}*/
	
	private void showPathwayPopup() {
		/*Set<Organism> organisms = organismManager.getOrganisms();
		
		if (organisms == null || organisms.isEmpty())
			return;*/
		//String pathway_in_list;
                try{
                    JPopupMenu popup = new JPopupMenu("Pathway List");
                    //popup.setBackground(getBackground());                  
                    Integer i=0;
                    for (String pathway_in_list: this.pathInfo) {
                            if(pathway_in_list.startsWith("sig_id"))
                                continue;
                            if (i == 40){
                                break;
                            }
                            String[] pathway_fields = pathway_in_list.split("\t");
                            String pathName = pathway_fields[1];
                            String pathId = pathway_fields[0];

                            JCheckBoxMenuItem mi = new JCheckBoxMenuItem(pathName , pathId.equals(selectedPathway));
                            
                            LookAndFeelUtil.makeSmall(mi);
                            mi.addActionListener(evt -> setSelectedPathway(pathId, pathName));
                            popup.add(mi);
                            i++;
                    }

                    popup.show(getPathwayButton(), 0, getPathwayButton().getHeight());
                    popup.requestFocus();
                }
                catch (Exception e){
                      manager.utils.error("Method showPathwayPopup error "+e.toString());
                }
        
                //createQueryScroll(popup);
	}
	private void fireQueryChanged() {
		firePropertyChange(NetworkSearchTaskFactory.QUERY_PROPERTY, null, null);
	}
        
        private void createQueryScroll(JPopupMenu popup) {
            
            queryScroll = new JScrollPane(popup);
            queryScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            queryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            LookAndFeelUtil.makeSmall(queryScroll);

        }
        
        private JTextField getQueryTextField() {
		if (queryTextField == null) {
			final Color msgColor = UIManager.getColor("Label.disabledForeground");
			final int vgap = 1;
			final int hgap = 5;
			
			queryTextField = new JTextField() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					
					if (getText() == null || getText().trim().isEmpty()) {
						// Set antialiasing
						Graphics2D g2 = (Graphics2D) g.create();
						g2.setRenderingHints(
								new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
						// Set the font
					    g2.setFont(getFont());
						// Get the FontMetrics
					    FontMetrics metrics = g2.getFontMetrics(getFont());
					    // Determine the X coordinate for the text
					    int x = hgap;
					    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
					    int y = (metrics.getHeight() / 2) + metrics.getAscent() + vgap;
						// Draw
						g2.setColor(msgColor);
						g2.drawString(DEF_SEARCH_TEXT, x, y);
						g2.dispose();
					}
				}
			};
			queryTextField.setEditable(false);
			queryTextField.setMinimumSize(queryTextField.getPreferredSize());
			queryTextField.setBorder(BorderFactory.createEmptyBorder(vgap, hgap, vgap, hgap));
			queryTextField.setFont(queryTextField.getFont().deriveFont(LookAndFeelUtil.getSmallFontSize()));
			
//			queryTextField.addMouseListener(new MouseAdapter() {
//				@Override
//				public void mousePressed(MouseEvent e) {
//					showQueryPopup();
//				}
//			});
			
			// Since we provide our own search component, it should let Cytoscape know
			// when it has been updated by the user, so Cytoscape can give a better
			// feedback to the user of whether or not the whole search component is ready
			// (e.g. Cytoscape may enable or disable the search button)
//			queryTextField.getDocument().addDocumentListener(new DocumentListener() {
//				@Override
//				public void removeUpdate(DocumentEvent e) {
//					fireQueryChanged();
//				}
//				@Override
//				public void insertUpdate(DocumentEvent e) {
//					fireQueryChanged();
//				}
//				@Override
//				public void changedUpdate(DocumentEvent e) {
//					// Nothing to do here...
//				}
//			});
		}
		
		return queryTextField;
	}
    /*@Override
    public void paint(Graphics g) {
        super.paint(g);

        if (queryTextArea.getText() == null || queryTextArea.getText().trim().isEmpty()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHints(
                    new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
            // Set the font
            g2.setFont(getFont());
            // Get the FontMetrics
            FontMetrics metrics = g2.getFontMetrics(getFont());
            // Determine the X coordinate for the text
            // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
            int y = (metrics.getHeight() / 2) + metrics.getAscent() + vgap;
            // Draw
            g2.setColor(msgColor);
            g2.drawString(DEF_SEARCH_TEXT, hgap, y);
            g2.dispose();
        }
    }*/

//    public String getQueryText() {
//        if (queryTextArea == null) return "";
//        return queryTextArea.getText();
//    }

  /*  private void showQueryPopup() {
        popup = new JPopupMenu();
        if (queryScroll == null) {
            createQueryScroll();
        }
        popup.setBackground(getBackground());
        popup.setLayout(new BorderLayout());
        popup.add(queryScroll, BorderLayout.CENTER);

        popup.addPropertyChangeListener("visible", evt -> {
            if (evt.getNewValue() == Boolean.FALSE)
                updateQueryTextField();
        });

        queryScroll.setPreferredSize(new Dimension(getSize().width, 200));
        popup.setPreferredSize(queryScroll.getPreferredSize());

        popup.show(this, 0, 0);
        popup.requestFocus();
        queryTextArea.requestFocusInWindow();
        queryTextArea.setToolTipText(tooltip);

    }

    private void updateQueryTextField() {
        // String text = query.stream().collect(Collectors.joining(" "));
        // TODO: truncate the text -- no need for this to be the entire string
        String text = queryTextArea.getText();
        if (text.length() > 30)
            text = text.substring(0, 30) + "...";
        queryTextArea.setText(text);
    }
    private void createQueryScroll() {
        queryTextArea = new JTextArea();
        LookAndFeelUtil.makeSmall(queryTextArea);

        // When Ctrl+ENTER (command+ENTER on macOS) is pressed, ask Cytoscape to perform the query
        String ENTER_ACTION_KEY = "ENTER_ACTION_KEY";
        KeyStroke enterKey = KeyStroke.getKeyStroke(
                KeyEvent.VK_ENTER,
                LookAndFeelUtil.isMac() ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK,
                false);
        InputMap inputMap = queryTextArea.getInputMap(JComponent.WHEN_FOCUSED);
        inputMap.put(enterKey, ENTER_ACTION_KEY);

        queryTextArea.getActionMap().put(ENTER_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // System.out.println("\n\nENTER");
                SearchPTHQueryComponent.this.firePropertyChange(NetworkSearchTaskFactory.SEARCH_REQUESTED_PROPERTY, null, null);
                popup.setVisible(false);
            }
        });

        queryScroll = new JScrollPane(queryTextArea);
        queryScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        queryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        LookAndFeelUtil.makeSmall(queryScroll);

    }*/

}
