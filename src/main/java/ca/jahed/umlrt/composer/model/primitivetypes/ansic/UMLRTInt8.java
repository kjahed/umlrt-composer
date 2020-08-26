package ca.jahed.umlrt.composer.model.primitivetypes.ansic;

import ca.jahed.umlrt.composer.model.primitivetypes.UMLRTPrimitiveType;
import ca.jahed.umlrt.composer.utils.UMLRTUtils;

public class UMLRTInt8 extends UMLRTPrimitiveType {
    private static final UMLRTInt8 instance = new UMLRTInt8();

    private UMLRTInt8() {
        setName("int8_t");
        setEObj(UMLRTUtils.getPrimitiveType(getName()));
    }

    public static UMLRTInt8 get() {
        return instance;
    }
}