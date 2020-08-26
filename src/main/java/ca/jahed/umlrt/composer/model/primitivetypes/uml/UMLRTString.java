package ca.jahed.umlrt.composer.model.primitivetypes.uml;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTString extends UMLRTPrimitiveType {
    private static final UMLRTString instance = new UMLRTString();

    private UMLRTString() {
        setName("String");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTString get() {
        return instance;
    }
}