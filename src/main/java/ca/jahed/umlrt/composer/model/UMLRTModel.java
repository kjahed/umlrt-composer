package ca.jahed.umlrt.composer.model;

public class UMLRTModel extends UMLRTPackage {
    UMLRTCapsulePart top;

    public void setTop(UMLRTCapsulePart top) {
        this.top = top;
    }

    public void loadPackage(UMLRTPackage pkg) {
        for (UMLRTPackage nested : pkg.getPackages())
            addPackage(nested);
        for (UMLRTClass passiveClass : pkg.getPassiveClasses())
            addPassiveClass(passiveClass);
        for (UMLRTCapsule capsule : pkg.getCapsules())
            addCapsule(capsule);
        for (UMLRTProtocol protocol : pkg.getProtocols())
            addProtocol(protocol);
        for (UMLRTEnumeration enumeration : pkg.getEnumerations())
            addEnumeration(enumeration);
        for (UMLRTArtifact artifact : pkg.getArtifacts())
            addArtifact(artifact);
    }

    public UMLRTCapsulePart getTop() {
        return top;
    }
}
