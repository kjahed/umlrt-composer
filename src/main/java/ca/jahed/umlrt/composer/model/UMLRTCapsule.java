package ca.jahed.umlrt.composer.model;

import ca.jahed.umlrt.composer.model.sm.UMLRTStateMachine;

import java.util.ArrayList;
import java.util.List;

public class UMLRTCapsule extends UMLRTElement {
    final List<UMLRTCapsulePart> parts = new ArrayList<>();
    final List<UMLRTPort> ports = new ArrayList<>();
    final List<UMLRTConnector> connectors = new ArrayList<>();
    UMLRTClass baseClass;
    UMLRTStateMachine stateMachine;

    public void addPart(UMLRTCapsulePart capsulePart) {
        parts.add(capsulePart);
    }

    public void addPort(UMLRTPort port) {
        ports.add(port);
    }

    public void addConnector(UMLRTConnector connector) {
        connectors.add(connector);
    }

    public void setBaseClass(UMLRTClass baseClass) {
        this.baseClass = baseClass;
    }

    public void setStateMachine(UMLRTStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    public UMLRTClass getBaseClass() {
        return baseClass;
    }

    public UMLRTStateMachine getStateMachine() {
        return stateMachine;
    }

    public List<UMLRTCapsulePart> getParts() {
        return parts;
    }

    public List<UMLRTPort> getPorts() {
        return ports;
    }

    public List<UMLRTConnector> getConnectors() {
        return connectors;
    }
}
