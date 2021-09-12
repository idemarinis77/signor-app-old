/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.view;

public class NetworkView {
        public enum Type {
        DEFAULT("DEFAULT", "SIGNOR - Default"),
        PTM("PTM", "SIGNOR - PTM");
        
        private final String name;
        public final String styleName;

        Type(String name, String styleName) {
            this.name = name;
            this.styleName = styleName;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
