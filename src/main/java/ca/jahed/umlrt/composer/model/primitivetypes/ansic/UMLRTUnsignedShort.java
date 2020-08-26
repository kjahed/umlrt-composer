package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTUnsignedShort extends UMLRTPrimitiveType {
    private static final UMLRTUnsignedShort instance = new UMLRTUnsignedShort();

    private UMLRTUnsignedShort() {
        setName("unsigned short");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTUnsignedShort get() {
        return instance;
    }
}