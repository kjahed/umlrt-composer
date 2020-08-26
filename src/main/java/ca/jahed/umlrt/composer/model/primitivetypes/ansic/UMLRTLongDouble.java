package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTLongDouble extends UMLRTPrimitiveType {
    private static final UMLRTLongDouble instance = new UMLRTLongDouble();

    private UMLRTLongDouble() {
        setName("long double");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTLongDouble get() {
        return instance;
    }
}