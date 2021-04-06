/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.style;

import it.uniroma2.signor.internal.view.NetworkView;
import it.uniroma2.signor.internal.managers.SignorManager;

/**
 *
 * @author amministratore
 */
public class SignorPTMStyle extends Style {
    public final static NetworkView.Type type = NetworkView.Type.DEFAULT;
    private SignorManager manager;
    
    public SignorPTMStyle(SignorManager manager, String filename) {
        super(manager, filename);
    }
    
    @Override
    public NetworkView.Type getStyleViewType() {
        return type;
    }
}
