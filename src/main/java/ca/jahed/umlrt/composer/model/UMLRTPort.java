package ca.jahed.umlrt.composer.model;

public class UMLRTPort extends UMLRTElement {
    UMLRTProtocol protocol;
    boolean conjugated;
    boolean behavior;
    boolean wired;
    boolean notification;
    boolean service;
    boolean publish;
    int replication;
    RegistrationType registrationType;
    String registrationOverride;

    public enum RegistrationType {
        AUTOMATIC,
        APPLICATION,
        AUTOMATIC_LOCKED
    }

    public void setProtocol(UMLRTProtocol protocol) {
        this.protocol = protocol;
    }

    public void setConjugated(boolean conjugated) {
        this.conjugated = conjugated;
    }

    public void setBehavior(boolean behavior) {
        this.behavior = behavior;
    }

    public void setWired(boolean wired) {
        this.wired = wired;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public void setService(boolean service) {
        this.service = service;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public void setReplication(int replication) {
        this.replication = replication;
    }

    public void setRegistrationType(RegistrationType registrationType) {
        this.registrationType = registrationType;
    }

    public void setRegistrationOverride(String registrationOverride) {
        this.registrationOverride = registrationOverride;
    }

    public UMLRTProtocol getProtocol() {
        return protocol;
    }

    public boolean isConjugated() {
        return conjugated;
    }

    public boolean isBehavior() {
        return behavior;
    }

    public boolean isWired() {
        return wired;
    }

    public boolean isNotification() {
        return notification;
    }

    public boolean isService() {
        return service;
    }

    public boolean isPublish() {
        return publish;
    }

    public boolean isRelay() {
        return service && !behavior;
    }

    public int getReplication() {
        return replication;
    }

    public RegistrationType getRegistrationType() {
        return registrationType;
    }

    public String getRegistrationOverride() {
        return registrationOverride;
    }
}
