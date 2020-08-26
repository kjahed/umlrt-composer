package ca.jahed.umlrt.composer.model;

public class UMLRTCallEvent extends UMLRTElement {
    UMLRTOperation operation;

    public void setOperation(UMLRTOperation operation) {
        this.operation = operation;
    }

    public UMLRTOperation getOperation() {
        return operation;
    }
}
