package ca.jahed.umlrt.composer.model.primitivetypes.uml;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTInteger extends UMLRTPrimitiveType {
    private static final UMLRTInteger instance = new UMLRTInteger();

    private UMLRTInteger() {
        setName("Integer");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTInteger get() {
        return instance;
    }
}