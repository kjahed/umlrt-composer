package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTDouble extends UMLRTPrimitiveType {
    private static final UMLRTDouble instance = new UMLRTDouble();

    private UMLRTDouble() {
        setName("double");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTDouble get() {
        return instance;
    }
}