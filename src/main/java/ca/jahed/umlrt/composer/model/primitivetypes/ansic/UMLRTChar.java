package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTChar extends UMLRTPrimitiveType {
    private static final UMLRTChar instance = new UMLRTChar();

    private UMLRTChar() {
        setName("char");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTChar get() {
        return instance;
    }
}