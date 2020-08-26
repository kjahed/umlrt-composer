package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTLong extends UMLRTPrimitiveType {
    private static final UMLRTLong instance = new UMLRTLong();

    private UMLRTLong() {
        setName("long");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTLong get() {
        return instance;
    }
}