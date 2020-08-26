package ca.jahed.umlrt.composer.model.properties;

public class UMLRTTypeProperties extends UMLRTProperties {
    String name;
    String definitionFile;

    public void setName(String name) {
        this.name = name;
    }

    public void setDefinitionFile(String definitionFile) {
        this.definitionFile = definitionFile;
    }

    public String getName() {
        return name;
    }

    public String getDefinitionFile() {
        return definitionFile;
    }
}
