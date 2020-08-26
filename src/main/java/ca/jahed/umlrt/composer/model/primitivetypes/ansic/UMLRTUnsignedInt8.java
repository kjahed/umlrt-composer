package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTUnsignedInt8 extends UMLRTPrimitiveType {
    private static final UMLRTUnsignedInt8 instance = new UMLRTUnsignedInt8();

    private UMLRTUnsignedInt8() {
        setName("uint8_t");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTUnsignedInt8 get() {
        return instance;
    }
}