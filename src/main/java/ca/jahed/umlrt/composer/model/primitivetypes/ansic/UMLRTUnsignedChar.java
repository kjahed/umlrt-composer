package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTUnsignedChar extends UMLRTPrimitiveType {
    private static final UMLRTUnsignedChar instance = new UMLRTUnsignedChar();

    private UMLRTUnsignedChar() {
        setName("unsigned char");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTUnsignedChar get() {
        return instance;
    }
}