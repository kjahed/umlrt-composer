package ca.jahed.umlrt.composer.model.sm;

import java.util.ArrayList;
import java.util.List;

public class UMLRTCompositeState extends UMLRTState {
    final List<UMLRTPseudoState> entryPoints = new ArrayList<>();
    final List<UMLRTPseudoState> exitPoints = new ArrayList<>();
    UMLRTRegion region;

    public void addEntryPoint(UMLRTPseudoState state) {
        entryPoints.add(state);
    }

    public void addExitPoint(UMLRTPseudoState state) {
        exitPoints.add(state);
    }

    public void setRegion(UMLRTRegion region) {
        this.region = region;
    }

    public List<UMLRTPseudoState> getEntryPoints() {
        return entryPoints;
    }

    public List<UMLRTPseudoState> getExitPoints() {
        return exitPoints;
    }

    public UMLRTRegion getRegion() {
        return region;
    }
}
