package ca.jahed.umlrt.composer.model.properties;

import ca.jahed.umlrt.composer.model.UMLRTArtifact;

public class UMLRTArtifactProperties extends UMLRTProperties {
    transient UMLRTArtifact artifact;
    String includeFile;
    String sourceFile;

    public void setArtifact(UMLRTArtifact artifact) {
        this.artifact = artifact;
    }

    public void setIncludeFile(String includeFile) {
        this.includeFile = includeFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public UMLRTArtifact getArtifact() {
        return artifact;
    }

    public String getIncludeFile() {
        return includeFile;
    }

    public String getSourceFile() {
        return sourceFile;
    }
}
