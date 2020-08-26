package ca.jahed.umlrt.composer.model;

import java.util.ArrayList;
import java.util.List;

public class UMLRTClass extends UMLRTType {
    final List<UMLRTAttribute> attributes = new ArrayList<>();
    final List<UMLRTOperation> operations = new ArrayList<>();

    public void addAttribute(UMLRTAttribute attribute) {
        this.attributes.add(attribute);
    }

    public void addOperation(UMLRTOperation operation) {
        this.operations.add(operation);
    }

    public List<UMLRTAttribute> getAttributes() {
        return attributes;
    }

    public List<UMLRTOperation> getOperations() {
        return operations;
    }
}
