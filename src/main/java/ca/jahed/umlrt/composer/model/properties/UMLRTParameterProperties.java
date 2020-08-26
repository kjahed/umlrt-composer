package ca.jahed.umlrt.composer.model.properties;

import ca.jahed.umlrt.composer.model.UMLRTParameter;

public class UMLRTParameterProperties extends UMLRTProperties {
    transient UMLRTParameter parameter;
    String type;
    boolean pointsToConst;
    boolean pointsToVolatile;
    boolean pointsToType;

    public UMLRTParameterProperties() {
    }

    public void setParameter(UMLRTParameter parameter) {
        this.parameter = parameter;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPointsToConst(boolean pointsToConst) {
        this.pointsToConst = pointsToConst;
    }

    public void setPointsToVolatile(boolean pointsToVolatile) {
        this.pointsToVolatile = pointsToVolatile;
    }

    public void setPointsToType(boolean pointsToType) {
        this.pointsToType = pointsToType;
    }

    public UMLRTParameter getParameter() {
        return parameter;
    }

    public String getType() {
        return type;
    }

    public boolean isPointsToConst() {
        return pointsToConst;
    }

    public boolean isPointsToVolatile() {
        return pointsToVolatile;
    }

    public boolean isPointsToType() {
        return pointsToType;
    }
}
