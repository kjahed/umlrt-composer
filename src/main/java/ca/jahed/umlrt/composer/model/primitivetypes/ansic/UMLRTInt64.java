package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTInt64 extends UMLRTPrimitiveType {
    private static final UMLRTInt64 instance = new UMLRTInt64();

    private UMLRTInt64() {
        setName("int64_t");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTInt64 get() {
        return instance;
    }
}