package ca.jahed.umlrt.composer.model;

public class UMLRTConnectorEnd extends UMLRTElement {
    UMLRTCapsulePart part;
    UMLRTPort port;

    public void setPort(UMLRTPort port) {
        this.port = port;
    }

    public void setPart(UMLRTCapsulePart part) {
        this.part = part;
    }

    public UMLRTCapsulePart getPart() {
        return part;
    }

    public UMLRTPort getPort() {
        return port;
    }
}
