package ca.jahed.umlrt.composer.model;

import java.util.ArrayList;
import java.util.List;

public class UMLRTProtocol extends UMLRTType {
    final List<UMLRTCallEvent> inputSignals = new ArrayList<>();
    final List<UMLRTCallEvent> outputSignals = new ArrayList<>();

    public void addInputSignal(UMLRTCallEvent signal) {
        inputSignals.add(signal);
    }

    public void addOutputSignal(UMLRTCallEvent signal) {
        outputSignals.add(signal);
    }

    public List<UMLRTCallEvent> getInputSignals() {
        return inputSignals;
    }

    public List<UMLRTCallEvent> getOutputSignals() {
        return outputSignals;
    }
}
