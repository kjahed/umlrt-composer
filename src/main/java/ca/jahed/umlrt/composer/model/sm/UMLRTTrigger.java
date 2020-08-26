package ca.jahed.umlrt.composer.model.sm;

import ca.jahed.umlrt.composer.model.UMLRTCallEvent;
import ca.jahed.umlrt.composer.model.UMLRTElement;
import ca.jahed.umlrt.composer.model.UMLRTPort;

import java.util.ArrayList;
import java.util.List;

public class UMLRTTrigger extends UMLRTElement {
    final List<UMLRTPort> ports = new ArrayList<>();
    UMLRTCallEvent callEvent;

    public void addPort(UMLRTPort port) {
        ports.add(port);
    }

    public void setCallEvent(UMLRTCallEvent callEvent) {
        this.callEvent = callEvent;
    }

    public UMLRTCallEvent getCallEvent() {
        return callEvent;
    }

    public List<UMLRTPort> getPorts() {
        return ports;
    }
}
