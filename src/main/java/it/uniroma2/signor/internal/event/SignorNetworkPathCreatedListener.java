package it.uniroma2.signor.internal.event;

import org.cytoscape.event.CyListener;

public interface SignorNetworkPathCreatedListener extends CyListener {
    void handleEvent(SignorNetworkCreatedEvent event);
}
