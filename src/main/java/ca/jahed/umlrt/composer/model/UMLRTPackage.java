package ca.jahed.umlrt.composer.model;

import java.util.ArrayList;
import java.util.List;

public class UMLRTPackage extends UMLRTElement {
    final List<UMLRTCapsule> capsules = new ArrayList<>();
    final List<UMLRTProtocol> protocols = new ArrayList<>();
    final List<UMLRTClass> passiveClasses = new ArrayList<>();
    final List<UMLRTEnumeration> enumerations = new ArrayList<>();
    final List<UMLRTArtifact> artifacts = new ArrayList<>();
    final List<UMLRTPackage> packages = new ArrayList<>();

    public void addCapsule(UMLRTCapsule capsule) {
        capsules.add(capsule);
    }

    public void addProtocol(UMLRTProtocol protocol) {
        protocols.add(protocol);
    }

    public void addPassiveClass(UMLRTClass passiveClass) {
        passiveClasses.add(passiveClass);
    }

    public void addEnumeration(UMLRTEnumeration enumeration) {
        enumerations.add(enumeration);
    }

    public void addArtifact(UMLRTArtifact artifact) {
        artifacts.add(artifact);
    }

    public void addPackage(UMLRTPackage pkg) {
        packages.add(pkg);
    }

    public List<UMLRTCapsule> getCapsules() {
        return capsules;
    }

    public List<UMLRTProtocol> getProtocols() {
        return protocols;
    }

    public List<UMLRTClass> getPassiveClasses() {
        return passiveClasses;
    }

    public List<UMLRTEnumeration> getEnumerations() {
        return enumerations;
    }

    public List<UMLRTArtifact> getArtifacts() {
        return artifacts;
    }

    public List<UMLRTPackage> getPackages() {
        return packages;
    }
}
