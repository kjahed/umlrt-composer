package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTWChar extends UMLRTPrimitiveType {
    private static final UMLRTWChar instance = new UMLRTWChar();

    private UMLRTWChar() {
        setName("wchar_t");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTWChar get() {
        return instance;
    }
}