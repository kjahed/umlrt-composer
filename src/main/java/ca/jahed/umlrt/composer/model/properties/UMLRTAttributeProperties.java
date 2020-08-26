package ca.jahed.umlrt.composer.model.properties;

import ca.jahed.umlrt.composer.model.UMLRTAttribute;

public class UMLRTAttributeProperties extends UMLRTProperties {
    transient UMLRTAttribute attribute;
    InitKind initialization;
    Kind kind;
    String size;
    String type;
    boolean pointsToConstType;
    boolean pointsToVolatileType;
    boolean pointsToType;
    boolean isVolatile;

    public enum Kind {
        MEMBER,
        GLOBAL,
        MUTABLE_MEMBER,
        DEFINE
    }

    public enum InitKind {
        ASSIGNMENT,
        CONSTANT,
        CONSTRUCTOR
    }

    public void setAttribute(UMLRTAttribute attribute) {
        this.attribute = attribute;
    }

    public void setInitialization(InitKind initialization) {
        this.initialization = initialization;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPointsToConstType(boolean pointsToConstType) {
        this.pointsToConstType = pointsToConstType;
    }

    public void setPointsToVolatileType(boolean pointsToVolatileType) {
        this.pointsToVolatileType = pointsToVolatileType;
    }

    public void setPointsToType(boolean pointsToType) {
        this.pointsToType = pointsToType;
    }

    public void setVolatile(boolean aVolatile) {
        isVolatile = aVolatile;
    }

    public UMLRTAttribute getAttribute() {
        return attribute;
    }

    public InitKind getInitialization() {
        return initialization;
    }

    public Kind getKind() {
        return kind;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public boolean isPointsToConstType() {
        return pointsToConstType;
    }

    public boolean isPointsToVolatileType() {
        return pointsToVolatileType;
    }

    public boolean isPointsToType() {
        return pointsToType;
    }

    public boolean isVolatile() {
        return isVolatile;
    }
}
