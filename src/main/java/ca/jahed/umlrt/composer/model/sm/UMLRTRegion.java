package ca.jahed.umlrt.composer.model.sm;

import ca.jahed.umlrt.composer.model.UMLRTElement;

import java.util.ArrayList;
import java.util.List;

public class UMLRTRegion extends UMLRTElement {
    final List<UMLRTGenericState> states = new ArrayList<>();
    final List<UMLRTTransition> transitions = new ArrayList<>();

    public void addState(UMLRTGenericState state) {
        states.add(state);
    }

    public void addTransition(UMLRTTransition transition) {
        transitions.add(transition);
    }

    public List<UMLRTGenericState> getStates() {
        return states;
    }

    public List<UMLRTTransition> getTransitions() {
        return transitions;
    }
}
