package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTShort extends UMLRTPrimitiveType {
    private static final UMLRTShort instance = new UMLRTShort();

    private UMLRTShort() {
        setName("short");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTShort get() {
        return instance;
    }
}