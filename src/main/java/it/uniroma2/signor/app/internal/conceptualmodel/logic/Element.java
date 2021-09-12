package it.uniroma2.signor.app.internal.conceptualmodel.logic;

import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.Network;

public interface Element {
    boolean isSelected();
    Network getNetwork();
}
