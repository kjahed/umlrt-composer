package ca.jahed.umlrt.composer.model;

public class UMLRTAttribute extends UMLRTElement {
    UMLRTType type;
    int replication;

    public void setType(UMLRTType type) {
        this.type = type;
    }

    public void setReplication(int replication) {
        this.replication = replication;
    }

    public UMLRTType getType() {
        return type;
    }

    public int getReplication() {
        return replication;
    }
}
