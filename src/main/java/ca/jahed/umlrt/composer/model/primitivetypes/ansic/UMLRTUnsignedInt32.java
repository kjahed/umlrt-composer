package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTUnsignedInt32 extends UMLRTPrimitiveType {
    private static final UMLRTUnsignedInt32 instance = new UMLRTUnsignedInt32();

    private UMLRTUnsignedInt32() {
        setName("uint32_t");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTUnsignedInt32 get() {
        return instance;
    }
}