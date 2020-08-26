package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTInt32 extends UMLRTPrimitiveType {
    private static final UMLRTInt32 instance = new UMLRTInt32();

    private UMLRTInt32() {
        setName("int32_t");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTInt32 get() {
        return instance;
    }
}