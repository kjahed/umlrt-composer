package ca.jahed.umlrt.composer.model.properties;

import ca.jahed.umlrt.composer.model.UMLRTOperation;

public class UMLRTOperationProperties extends UMLRTProperties {
    transient UMLRTOperation operation;
    OpKind kind;
    boolean generateDefinition;
    boolean inline;
    boolean polymorphic;

    public enum OpKind {
        MEMBER,
        FRIEND,
        GLOBAL
    }

    public void setOperation(UMLRTOperation operation) {
        this.operation = operation;
    }

    public void setKind(OpKind kind) {
        this.kind = kind;
    }

    public void setGenerateDefinition(boolean generateDefinition) {
        this.generateDefinition = generateDefinition;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public void setPolymorphic(boolean polymorphic) {
        this.polymorphic = polymorphic;
    }

    public UMLRTOperation getOperation() {
        return operation;
    }

    public OpKind getKind() {
        return kind;
    }

    public boolean isGenerateDefinition() {
        return generateDefinition;
    }

    public boolean isInline() {
        return inline;
    }

    public boolean isPolymorphic() {
        return polymorphic;
    }
}
