package it.uniroma2.signor.app.internal.event;

import org.cytoscape.event.AbstractCyEvent;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.Network;

public class SignorNetworkCreatedEvent extends AbstractCyEvent<SignorManager> {
    private final Network newNetwork;

    public SignorNetworkCreatedEvent(SignorManager source, Network newNetwork) {
        super(source, SignorNetworkCreatedListener.class);
        this.newNetwork = newNetwork;
        newNetwork.writeSearchNetwork();
    }
    public Network getNewNetwork() {
        return newNetwork;
    }
}
