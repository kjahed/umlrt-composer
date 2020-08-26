package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTUnsignedInt16 extends UMLRTPrimitiveType {
    private static final UMLRTUnsignedInt16 instance = new UMLRTUnsignedInt16();

    private UMLRTUnsignedInt16() {
        setName("uint16_t");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTUnsignedInt16 get() {
        return instance;
    }
}