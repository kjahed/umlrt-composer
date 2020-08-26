package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTUnsignedLong extends UMLRTPrimitiveType {
    private static final UMLRTUnsignedLong instance = new UMLRTUnsignedLong();

    private UMLRTUnsignedLong() {
        setName("unsigned long");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTUnsignedLong get() {
        return instance;
    }
}