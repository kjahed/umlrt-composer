package ca.jahed.umlrt.composer.model;

public class UMLRTConnector extends UMLRTElement {
    UMLRTConnectorEnd end1;
    UMLRTConnectorEnd end2;

    public void setEnd1(UMLRTConnectorEnd end1) {
        this.end1 = end1;
    }

    public void setEnd2(UMLRTConnectorEnd end2) {
        this.end2 = end2;
    }

    public UMLRTConnectorEnd getEnd1() {
        return end1;
    }

    public UMLRTConnectorEnd getEnd2() {
        return end2;
    }
}
