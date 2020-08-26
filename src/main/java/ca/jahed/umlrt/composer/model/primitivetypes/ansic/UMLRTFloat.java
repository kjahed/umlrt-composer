package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTFloat extends UMLRTPrimitiveType {
    private static final UMLRTFloat instance = new UMLRTFloat();

    private UMLRTFloat() {
        setName("float");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTFloat get() {
        return instance;
    }
}