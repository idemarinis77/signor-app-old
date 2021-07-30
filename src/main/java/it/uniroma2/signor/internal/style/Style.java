package it.uniroma2.signor.internal.style;

import org.cytoscape.event.CyEventHelper;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.values.NodeShape;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.view.NetworkView;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;


import java.awt.*;
import java.util.Set;

public abstract class Style {
    public static final Color defaultNodeColor = new Color(157, 177, 128);

    protected VisualStyle style;
    protected SignorManager manager;
    protected LoadVizmapFileTaskFactory loadVizmapFileTaskFactory;
    protected CyEventHelper eventHelper;
    protected VisualMappingManager vmm;
    protected VisualMappingFunctionFactory continuousFactory;
    protected VisualMappingFunctionFactory discreteFactory;
    protected VisualMappingFunctionFactory passthroughFactory;

    private boolean newStyle;
    protected DiscreteMapping<String, NodeShape> nodeTypeToShape;
    protected DiscreteMapping<String, Paint> taxIdToNodeColor;

    public Style(SignorManager manager, VisualStyle style) {
        this.manager = manager;
        vmm = manager.utils.getService(VisualMappingManager.class);
        eventHelper = manager.utils.getService(CyEventHelper.class);
        this.style = style;
        newStyle = false;

        continuousFactory = manager.utils.getService(VisualMappingFunctionFactory.class, "(mapping.type=continuous)");
        discreteFactory = manager.utils.getService(VisualMappingFunctionFactory.class, "(mapping.type=discrete)");
        passthroughFactory = manager.utils.getService(VisualMappingFunctionFactory.class, "(mapping.type=passthrough)");
    }

    public Style(SignorManager manager, String filestyle) {
        this.manager = manager;
        this.loadVizmapFileTaskFactory = loadVizmapFileTaskFactory;
        vmm = manager.utils.getService(VisualMappingManager.class);
        Set<VisualStyle> vsSet = manager.utils.getService(LoadVizmapFileTaskFactory.class).loadStyles(getClass().getResourceAsStream(filestyle));
        eventHelper = manager.utils.getService(CyEventHelper.class);
        style = getOrCreateStyle();
        continuousFactory = manager.utils.getService(VisualMappingFunctionFactory.class, "(mapping.type=continuous)");
        discreteFactory = manager.utils.getService(VisualMappingFunctionFactory.class, "(mapping.type=discrete)");
        passthroughFactory = manager.utils.getService(VisualMappingFunctionFactory.class, "(mapping.type=passthrough)");
        if (newStyle) {
           //createStyle();
            registerStyle();
        } 
    }

    private VisualStyle getOrCreateStyle() {
        for (VisualStyle createdStyle : vmm.getAllVisualStyles()) {
            if (createdStyle.getTitle().equals(getStyleName())) {
                newStyle = false;
                return createdStyle;
            }
        }
        newStyle = true;
        return manager.utils.getService(VisualStyleFactory.class).createVisualStyle(getStyleName());
    }
    public void registerStyle() {
        vmm.addVisualStyle(style);
    }

    public void applyStyle(CyNetworkView networkView) {
        vmm.setVisualStyle(style, networkView);
        style.apply(networkView);
        networkView.updateView();
    }

    public String getStyleName(){
        return getStyleViewType().styleName;
    }

    public abstract NetworkView.Type getStyleViewType();
    @Override
    public String toString() {
        return getStyleName();
    }

    public VisualStyle getStyle() {
        return style;
    }

    
}

