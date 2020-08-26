package ca.jahed.umlrt.composer.model.sm;

import ca.jahed.umlrt.composer.model.UMLRTElement;

import java.util.ArrayList;
import java.util.List;

public abstract class UMLRTGenericState extends UMLRTElement {
    final transient List<UMLRTTransition> inTransitions = new ArrayList<>();
    final transient List<UMLRTTransition> outTransitions = new ArrayList<>();

    public void addInTransition(UMLRTTransition transition) {
        inTransitions.add(transition);
    }

    public void addOutTransition(UMLRTTransition transition) {
        outTransitions.add(transition);
    }

    public List<UMLRTTransition> getInTransitions() {
        return inTransitions;
    }

    public List<UMLRTTransition> getOutTransitions() {
        return outTransitions;
    }
}
