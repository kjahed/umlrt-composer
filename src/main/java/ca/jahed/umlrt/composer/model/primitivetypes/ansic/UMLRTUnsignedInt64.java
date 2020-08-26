package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTUnsignedInt64 extends UMLRTPrimitiveType {
    private static final UMLRTUnsignedInt64 instance = new UMLRTUnsignedInt64();

    private UMLRTUnsignedInt64() {
        setName("uint64_t");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTUnsignedInt64 get() {
        return instance;
    }
}