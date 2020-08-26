package ca.jahed.umlrt.composer.model;

public class UMLRTCapsulePart extends UMLRTElement {
    UMLRTAttribute attribute;
    UMLRTCapsule capsule;
    boolean optional;
    boolean plugin;

    public void setAttribute(UMLRTAttribute attribute) {
        this.attribute = attribute;
    }

    public void setCapsule(UMLRTCapsule capsule) {
        this.capsule = capsule;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public void setPlugin(boolean plugin) {
        this.plugin = plugin;
    }

    public UMLRTAttribute getAttribute() {
        return attribute;
    }

    public UMLRTCapsule getCapsule() {
        return capsule;
    }

    public boolean isOptional() {
        return optional;
    }

    public boolean isPlugin() {
        return plugin;
    }

    public int getReplication() {
        return attribute.getReplication();
    }
}