package ca.jahed.umlrt.composer.model.sm;

import ca.jahed.umlrt.composer.model.UMLRTElement;

import java.util.ArrayList;
import java.util.List;

public class UMLRTTransition extends UMLRTElement {
    final List<UMLRTTrigger> triggers = new ArrayList<>();
    UMLRTGenericState source;
    UMLRTGenericState target;
    UMLRTActionCode guard;
    UMLRTActionCode action;

    public void addTrigger(UMLRTTrigger trigger) {
        triggers.add(trigger);
    }

    public void setGuard(UMLRTActionCode guard) {
        this.guard = guard;
    }

    public void setAction(UMLRTActionCode action) {
        this.action = action;
    }

    public void setSource(UMLRTGenericState source) {
        this.source = source;
    }

    public void setTarget(UMLRTGenericState target) {
        this.target = target;
    }

    public UMLRTGenericState getSource() {
        return source;
    }

    public UMLRTGenericState getTarget() {
        return target;
    }

    public UMLRTActionCode getGuard() {
        return guard;
    }

    public UMLRTActionCode getAction() {
        return action;
    }

    public List<UMLRTTrigger> getTriggers() {
        return triggers;
    }
}
