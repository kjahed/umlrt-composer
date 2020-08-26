package ca.jahed.umlrt.composer.model.primitivetypes.uml;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTBoolean extends UMLRTPrimitiveType {
    private static final UMLRTBoolean instance = new UMLRTBoolean();

    private UMLRTBoolean() {
        setName("Boolean");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTBoolean get() {
        return instance;
    }
}