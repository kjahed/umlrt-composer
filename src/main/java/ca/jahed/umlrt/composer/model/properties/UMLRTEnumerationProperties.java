package ca.jahed.umlrt.composer.model.properties;

import ca.jahed.umlrt.composer.model.UMLRTEnumeration;

public class UMLRTEnumerationProperties extends UMLRTProperties {
    transient UMLRTEnumeration enumeration;
    String headerPreface;
    String headerEnding;
    String implementationPreface;
    String implementationEnding;
    boolean generate;

    public UMLRTEnumerationProperties() {
    }

    public void setEnumeration(UMLRTEnumeration enumeration) {
        this.enumeration = enumeration;
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

    public void setGenerate(boolean generate) {
        this.generate = generate;
    }

    public UMLRTEnumeration getEnumeration() {
        return enumeration;
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

    public boolean isGenerate() {
        return generate;
    }
}
