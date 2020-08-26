package ca.jahed.umlrt.composer.model;

import ca.jahed.umlrt.composer.model.sm.UMLRTActionCode;

import java.util.LinkedList;
import java.util.List;

public class UMLRTOperation extends UMLRTElement {
    final List<UMLRTParameter> parameters = new LinkedList<>();
    UMLRTActionCode code;
    UMLRTParameter returnParam;

    public void addParameter(UMLRTParameter parameter) {
        parameters.add(parameter);
    }

    public void setCode(UMLRTActionCode code) {
        this.code = code;
    }

    public void setReturnParam(UMLRTParameter returnParam) {
        this.returnParam = returnParam;
    }

    public UMLRTActionCode getCode() {
        return code;
    }

    public UMLRTParameter getReturnParam() {
        return returnParam;
    }

    public List<UMLRTParameter> getParameters() {
        return parameters;
    }
}
