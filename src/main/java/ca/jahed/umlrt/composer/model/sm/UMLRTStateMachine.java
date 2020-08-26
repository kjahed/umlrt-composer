package ca.jahed.umlrt.composer.model.sm;

import ca.jahed.umlrt.composer.model.UMLRTElement;

public class UMLRTStateMachine extends UMLRTElement {
    UMLRTRegion region;

    public void setRegion(UMLRTRegion region) {
        this.region = region;
    }

    public UMLRTRegion getRegion() {
        return region;
    }
}
