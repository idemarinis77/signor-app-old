package it.uniroma2.signor.app.internal.event;

import org.cytoscape.event.CyListener;

public interface SignorNetworkCreatedListener extends CyListener {
    void handleEvent(SignorNetworkCreatedEvent event);
}
