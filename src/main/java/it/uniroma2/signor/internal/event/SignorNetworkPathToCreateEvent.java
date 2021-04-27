package it.uniroma2.signor.internal.event;

import org.cytoscape.event.AbstractCyEvent;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;

public class SignorNetworkPathToCreateEvent extends AbstractCyEvent<SignorManager> {
    private final String pathwaytocreate;

    public SignorNetworkPathToCreateEvent(SignorManager source, String pathwaytocreate) {
        super(source, SignorNetworkPathCreatedListener.class);
        this.pathwaytocreate = pathwaytocreate;
    }
    public String getNewNetwork() {
        return pathwaytocreate;
    }
}
