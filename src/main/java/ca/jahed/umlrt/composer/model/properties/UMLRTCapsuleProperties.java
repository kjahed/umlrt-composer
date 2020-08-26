package ca.jahed.umlrt.composer.model.properties;

import ca.jahed.umlrt.composer.model.UMLRTCapsule;

public class UMLRTCapsuleProperties extends UMLRTProperties {
    transient UMLRTCapsule capsule;
    String headerPreface;
    String headerEnding;
    String implementationPreface;
    String implementationEnding;
    String publicDeclarations;
    String privateDeclarations;
    String protectedDeclarations;
    boolean generateHeader;
    boolean generateImplementation;

    public UMLRTCapsuleProperties() {
    }

    public void setCapsule(UMLRTCapsule capsule) {
        this.capsule = capsule;
    }

    public void setHeaderPreface(String headerPreface) {
        this.headerPreface = headerPreface;
    }

    public void setHeaderEnding(String headerEnding) {
        this.headerEnding = headerEnding;
    }

    public void setImplementationPreface(String implementationPreface) {
        this.implementationPreface = implementationPreface;
    }

    public void setImplementationEnding(String implementationEnding) {
        this.implementationEnding = implementationEnding;
    }

    public void setPublicDeclarations(String publicDeclarations) {
        this.publicDeclarations = publicDeclarations;
    }

    public void setPrivateDeclarations(String privateDeclarations) {
        this.privateDeclarations = privateDeclarations;
    }

    public void setProtectedDeclarations(String protectedDeclarations) {
        this.protectedDeclarations = protectedDeclarations;
    }

    public void setGenerateHeader(boolean generateHeader) {
        this.generateHeader = generateHeader;
    }

    public void setGenerateImplementation(boolean generateImplementation) {
        this.generateImplementation = generateImplementation;
    }

    public UMLRTCapsule getCapsule() {
        return capsule;
    }

    public String getHeaderPreface() {
        return headerPreface;
    }

    public String getHeaderEnding() {
        return headerEnding;
    }

    public String getImplementationPreface() {
        return implementationPreface;
    }

    public String getImplementationEnding() {
        return implementationEnding;
    }

    public String getPublicDeclarations() {
        return publicDeclarations;
    }

    public String getPrivateDeclarations() {
        return privateDeclarations;
    }

    public String getProtectedDeclarations() {
        return protectedDeclarations;
    }

    public boolean isGenerateHeader() {
        return generateHeader;
    }

    public boolean isGenerateImplementation() {
        return generateImplementation;
    }
}
